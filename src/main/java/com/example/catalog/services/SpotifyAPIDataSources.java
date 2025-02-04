package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;


@Service
public class SpotifyAPIDataSources implements DataSourceService {

    private static final String BASE_URL = "https://api.spotify.com/v1/";
    private RestTemplate restTemplate = new RestTemplate();
     // Replace with a valid Spotify API

    @Value("${SpotifyAPIDataSources.token}")
    private String accessToken;

    public void setAccessToken(String token){
       this.accessToken = token;
    }
    private HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }
    @Override
    public List<Album> getAlbums() throws IOException {
        String url = BASE_URL + "albums/";
        HttpEntity<String> entity = new HttpEntity<>(getAuthHeaders());

        try {
            ResponseEntity<List<Album>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Album>>() {});
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Album getAlbumById(String albumId) throws IOException {
        String url = "https://api.spotify.com/v1/albums/" + albumId;
        HttpEntity<String> entity = new HttpEntity<>(getAuthHeaders());

        try {
            ResponseEntity<Album> response = restTemplate.exchange(url, HttpMethod.GET, entity, Album.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean addAlbum(Album album) throws IOException {
        return false;
    }

    @Override
    public boolean updateAlbum(String albumId, Album updatedAlbum) throws IOException {
        return false;
    }

    @Override
    public boolean deleteAlbumById(String albumId) throws IOException {
        return false;
    }

    @Override
    public List<Track> getAlbumTracks(String albumId) throws IOException {
        String url = BASE_URL + "albums/" + albumId + "/tracks";
        HttpEntity<String> entity = new HttpEntity<>(getAuthHeaders());

        try {
            ResponseEntity<List<Track>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Track>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean addTrackToAlbum(String albumId, Track track) throws IOException {
        return false;
    }

    @Override
    public boolean updateTrack(String albumId, String trackId, Track updatedTrack) throws IOException {
        return false;
    }

    @Override
    public boolean deleteTrackFromAlbum(String albumId, String trackId) throws IOException {
        return false;
    }

    @Override
    public Artist getArtistById(String artistId) throws IOException {
        String url = BASE_URL + "artists/" + artistId;
        HttpEntity<String> entity = new HttpEntity<>(getAuthHeaders());

        try {
            ResponseEntity<Artist> response = restTemplate.exchange(url, HttpMethod.GET, entity, Artist.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Artist> getArtists() throws IOException {
        String url = BASE_URL + "artists";
        HttpEntity<String> entity = new HttpEntity<>(getAuthHeaders());

        try {
            ResponseEntity<List<Artist>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Artist>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean addArtist(Artist artist) throws IOException {
        return false;
    }

    @Override
    public boolean updateArtist(String artistId, Artist artist) throws IOException {
        return false;
    }

    @Override
    public boolean deleteArtistById(String artistId) throws IOException {
        return false;    }

    @Override
    public List<Album> getArtistAlbums(String artistId) throws IOException {
        String url = BASE_URL + "artists/" + artistId + "/albums";
        HttpEntity<String> entity = new HttpEntity<>(getAuthHeaders());

        try {
            ResponseEntity<List<Album>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Album>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Song> getArtistSongs(String artistId) throws IOException {
        String url = BASE_URL + "artists/" + artistId + "/tracks";
        HttpEntity<String> entity = new HttpEntity<>(getAuthHeaders());
        try {
            ResponseEntity<List<Song>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Song>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Song> getAllSongs() throws IOException {
        String url = BASE_URL + "tracks";
        HttpEntity<String> entity = new HttpEntity<>(getAuthHeaders());
        try {
            ResponseEntity<List<Song>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Song>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Song getSongById(String songId) throws IOException {
        return null;
    }

    @Override
    public boolean addSong(Song song) throws IOException {
        return false;
    }

    @Override
    public boolean updateSong(String songId, Song updatedSong) throws IOException {
        return false;
    }

    @Override
    public boolean deleteSongById(String songId) throws IOException {
        return false;
    }
}
