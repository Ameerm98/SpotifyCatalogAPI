package com.example.catalog.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Instant;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimit implements HandlerInterceptor {

    @Value("${rate-limit.algo}")
    private String rateLimitAlgo;

    @Value("${rate-limit.rpm}")
    private String rateLimitRPM;
    private long timeWindowsmillis =60*1000 ;
    private ConcurrentHashMap<String, Interval> clientsrequests = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,CircularBuffer> clientsrequestsmoving = new ConcurrentHashMap<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();
        //String res = request.getContextPath();
        if (Objects.equals(request.getRequestURI(), "/internal")){
            return true;
        }

        return isAllowed(clientIp,response,currentTime);
    }
    private boolean isAllowed(String clientIp,HttpServletResponse response,long currentTime) {
        // TODO your implementation ...
        if (Objects.equals(rateLimitAlgo, "fixed")) {
            Interval interval = clientsrequests.computeIfAbsent(clientIp, k -> new Interval(0, 0));
            synchronized (interval) {
                long currentInterval = (currentTime-interval.timeCreated) / timeWindowsmillis;
                if (interval.intervalNum != currentInterval) {
                    interval.intervalNum = currentInterval;
                    while (currentTime-interval.timeCreated>timeWindowsmillis){
                        interval.timeCreated+=timeWindowsmillis;
                    }
                    interval.requestCount = 0;
                }
                if (interval.requestCount < Integer.parseInt(rateLimitRPM)) {
                    interval.requestCount += 1;
                    response.setHeader("X-Rate-Limit-Remaining", Integer.toString(Integer.parseInt(rateLimitRPM)-interval.requestCount));
                    return true;
                } else {
                    response.setStatus(429);
                    response.setHeader("X-Rate-Limit-Remaining", Integer.toString(0));
                    response.setHeader("X-Rate-Limit-Retry-After-Seconds", Long.toString((timeWindowsmillis-(currentTime-interval.timeCreated)) / 1000));
                    return false;
                }

            }
        } else {
            CircularBuffer slideWindow = clientsrequestsmoving.computeIfAbsent(clientIp,k -> new CircularBuffer(Integer.parseInt(rateLimitRPM)));
            synchronized (slideWindow){
                long oldestRequestTime = slideWindow.peek();
                if (slideWindow.size<slideWindow.capacity || (currentTime-oldestRequestTime>timeWindowsmillis)){
                    slideWindow.add(currentTime);
                    response.setHeader("X-Rate-Limit-Remaining", Integer.toString(slideWindow.capacity-slideWindow.size));
                    return true;

                }else {
                    response.setStatus(429);
                    response.setHeader("X-Rate-Limit-Remaining", Integer.toString(0));
                    response.setHeader("X-Rate-Limit-Retry-After-Seconds", Long.toString((timeWindowsmillis-(currentTime-oldestRequestTime)) / 1000));
                    return false;
                }
            }
        }
    }
    public static class CircularBuffer {
        private final long[] buffer;
        private final int capacity;
        private int head = 0;
        private int tail = 0;
        private int size = 0;

        @SuppressWarnings("unchecked")
        public CircularBuffer(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Buffer capacity must be greater than 0");
            }
            this.capacity = capacity;
            this.buffer = new long[capacity];
        }


        public void add(long element) {
            buffer[head] = element;
            head = (head + 1) % capacity;
            if (size == capacity) {
                tail = (tail + 1) % capacity;
            } else {
                size++;
            }
        }

        public long remove() {
            if (isEmpty()) {
                throw new IllegalStateException("Buffer is empty");
            }
            long element = buffer[tail];
            buffer[tail] = 0;
            tail = (tail + 1) % capacity;
            size--;
            return element;
        }


        public long peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Buffer is empty");
            }
            return buffer[tail];
        }


        public boolean isEmpty() {
            return size == 0;
        }


        public boolean isFull() {
            return size == capacity;
        }

        public int size() {
            return size;
        }

        public int capacity() {
            return capacity;
        }
    }

    private static class Interval{
        long intervalNum;
        long timeCreated;
        int requestCount;

        Interval(long statTime,int requestCount){
            this.intervalNum = statTime;
            this.timeCreated = System.currentTimeMillis();
            this.requestCount = requestCount;


        }
    }
}