package com.example.catalog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Song {

    private String id;
    private String name;
    private String uri;
    private int durationMs;
    private int popularity;
    private Album album;
    private List<Artist> artists;

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public int getPopularity() {
        return popularity;
    }

    public Album getAlbum() {
        return album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
