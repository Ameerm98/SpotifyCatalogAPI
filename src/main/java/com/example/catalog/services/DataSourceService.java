package com.example.catalog.services;

import com.example.catalog.model.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

// src/main/java/com/example/catalog/services/DataSourceService.java

public interface DataSourceService {
    List<Album> getAlbums() throws IOException;
    Album getAlbumById(String albumId) throws IOException;
    boolean addAlbum(Album album) throws IOException;
    boolean updateAlbum(String albumId,Album updatedAlbum) throws IOException;
    boolean deleteAlbumById(String albumId) throws IOException;
    List<Track> getAlbumTracks(String albumId) throws IOException;
    boolean addTrackToAlbum(String albumId,Track track) throws IOException;
    boolean updateTrack(String albumId,String trackId,Track updatedTrack) throws IOException;
    boolean deleteTrackFromAlbum(String albumId,String trackId) throws IOException;
    Artist getArtistById(String artistId) throws IOException;
    List<Artist> getArtists() throws IOException;
    boolean addArtist(Artist artist ) throws  IOException;
    boolean updateArtist(String artistId,Artist artist) throws IOException;
    boolean deleteArtistById(String artistId) throws IOException;
    List<Album> getArtistAlbums(String artistId) throws IOException;
    List<Song> getArtistSongs(String artistId) throws IOException;
    List<Song> getAllSongs() throws IOException;
    Song getSongById(String songId) throws IOException;
    boolean addSong(Song song ) throws  IOException;
    boolean updateSong(String songId,Song updatedSong) throws IOException;
    boolean deleteSongById(String songId) throws IOException;

}
