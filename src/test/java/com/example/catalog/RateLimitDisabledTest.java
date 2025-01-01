package com.example.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"rate-limit.enabled=false"})
public class RateLimitDisabledTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String API_ENDPOINT = "/";
    private static final String INTERNAL_ENDPOINT = "/internal";
    private static final String XRateLimitRetryAfterSecondsHeader = "X-Rate-Limit-Retry-After-Seconds";
    private static final String XRateLimitRemaining = "X-Rate-Limit-Remaining";




    @Test
    public void testDisableRateLimit(){
        int allowedRequests = 20;

        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)), "Expected status code to be 200 for All of the requests ,RateLimitDisabled");


        }




    }
}
