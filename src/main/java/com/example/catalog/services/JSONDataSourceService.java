package com.example.catalog.services;
// src/main/java/com/example/catalog/services/JSONDataSourceService.java

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.catalog.model.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class JSONDataSourceService implements DataSourceService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${JSONDataSourceService.data}")
    private String dataDir;

    @Override
    public List<Album> getAlbums() throws IOException {
        JsonNode albumsNode = loadJsonData(dataDir+"/albums.json");
        if (albumsNode == null ) {
            return null;
        }
        List<Album> albums = new ArrayList<>();
        for (JsonNode album:albumsNode){
            albums.add( objectMapper.treeToValue(album, Album.class));
        }
        return albums;
    }

    @Override
    public Album getAlbumById(String AlbumId) throws IOException {
        JsonNode albums = loadJsonData(dataDir + "/albums.json");
        for (JsonNode albumNode : albums) {
            String id = albumNode.get("id").asText();
            if (AlbumId.equals(id)) {
                // Return the album as a response if the ID matches
               return objectMapper.treeToValue(albumNode, Album.class);
            }
        }
        return null;
    }

    @Override
    public boolean addAlbum(Album album) throws IOException {

        //ObjectNode jsonAlbum = objectMapper.valueToTree(album);
        //ObjectNode jsonAlbum = convertStringToObjectNode(album);

        JsonNode root =loadJsonData(dataDir+"/albums.json");
        ((ObjectNode) root).set(album.getId(), objectMapper.valueToTree(album));
        ClassPathResource path= new ClassPathResource(dataDir+"/albums.json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.getFile(), root);
        return true;
    }
    @Override
    public boolean updateAlbum(String albumId, Album updatedAlbum) throws IOException {
        deleteAlbumById(albumId);
        addAlbum(updatedAlbum);
        return true;
    }

    @Override
    public boolean deleteAlbumById(String albumId) throws IOException {
        JsonNode rootNode = loadJsonData(dataDir+"/albums.json");
        ((ObjectNode) rootNode).remove(albumId);
        ClassPathResource path= new ClassPathResource(dataDir+"/albums.json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.getFile(), rootNode);
        return true;
    }

    @Override
    public List<Track> getAlbumTracks(String albumId) throws IOException {
        /*
        JsonNode albumsNode = loadJsonData("data/albums.json");
        JsonNode album = albumsNode.path(albumId);
        JsonNode Tracks = album.get("tracks");
        List<Track> tracks = new ArrayList<>();
        for (JsonNode track:Tracks){
            tracks.addLast( objectMapper.treeToValue(track, Track.class));
        }
        return tracks;
         */
        Album album = getAlbumById(albumId);
        if (album==null){
            Collections.emptyList();
        }
        return album.getTracks();
    }

    @Override
    public boolean addTrackToAlbum(String albumId, Track newTrack) throws IOException {
        Album album = getAlbumById(albumId);
        if (album == null) {
            return false;
        }
        if (!deleteAlbumById(album.getId())){
            return false;
        }
        album.getTracks().add(newTrack);

        return addAlbum(album);
    }

    @Override
    public boolean updateTrack(String albumId, String trackId, Track updatedTrack) throws IOException {
        Album album = getAlbumById(albumId);
        if (album == null) {
            return false;
        }

        List<Track> tracks = album.getTracks();
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getId().equals(trackId)) {
                tracks.set(i, updatedTrack);
                updateAlbum(albumId, album);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteTrackFromAlbum(String albumId, String trackId) throws IOException {
        Album album = getAlbumById(albumId);
        if (album == null) {
            return false;
        }
        if (!deleteAlbumById(album.getId())){
            return false;
        }
        for (Track track:album.getTracks()){
            if (Objects.equals(track.getId(), trackId)){
                album.getTracks().remove(track);
                return addAlbum(album);
            }
        }

        return false;
    }

    @Override
    public Artist getArtistById(String id) throws IOException {
        JsonNode artists = loadJsonData(dataDir+"/popular_artists.json");
        JsonNode artistNode = artists.get(id);
        if (artistNode == null) {
            return null;
        }
        return objectMapper.treeToValue(artistNode, Artist.class);
    }

    @Override
    public List<Artist> getArtists() throws IOException {
        JsonNode artistsNode = loadJsonData(dataDir+"/popular_artists.json");
        if (artistsNode == null || !artistsNode.isArray()) {
            return null;
        }
        List<Artist> artists = new ArrayList<>();
        for (JsonNode artist:artistsNode){
            artists.add( objectMapper.treeToValue(artist, Artist.class));
        }
        return artists;
    }

    @Override
    public boolean addArtist(Artist artist) throws IOException {
        JsonNode root =loadJsonData(dataDir+"/popular_artists.json");
        ((ObjectNode) root).set(artist.getId(), objectMapper.valueToTree(artist));
        ClassPathResource path= new ClassPathResource(dataDir+"/popular_artists.json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.getFile(), root);
        return true;
    }


    @Override
    public boolean updateArtist(String artistId, Artist updatedArtist) throws IOException {
        deleteArtistById(artistId);
        addArtist(updatedArtist);
        return true;
    }


    @Override
    public boolean deleteArtistById(String artistId) throws IOException {
        JsonNode rootNode = loadJsonData(dataDir+"/popular_artists.json");
        ((ObjectNode) rootNode).remove(artistId);
        ClassPathResource path= new ClassPathResource(dataDir+"/popular_artists.json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.getFile(), rootNode);

        return true;
    }

    @Override
    public List<Album> getArtistAlbums(String artistId) throws IOException {

        List<Song> songs= loadSongList();
        List<Album> res = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            if (Objects.equals(songs.get(i).getArtists().get(0).getId(), artistId)) {
                    boolean dup = false;
                    for (Album album:res){
                        if (Objects.equals(album.getName(), songs.get(i).getAlbum().getName())){
                            dup = true;
                        }
                    }
                    if (!dup){
                        res.add(songs.get(i).getAlbum());
                    }
            }
        }
        /*
        JsonNode artistsNode = loadJsonData("data/popular_songs.json");
        JsonNode album = artistsNode.path(artistId);
        JsonNode Albums = album.get("albums");
        List<Track> tracks = new ArrayList<>();
        for (JsonNode track:Tracks){
            tracks.addLast( objectMapper.treeToValue(track, Track.class));
        }
        return tracks;

         */
        return res;
    }

    @Override
    public List<Song> getArtistSongs(String artistId) throws IOException {
        List<Song> songs= loadSongList();
        List<Song> res = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            if (Objects.equals(songs.get(i).getArtists().get(0).getId(), artistId)) {
                res.add(songs.get(i));
            }
        }
        return res;
    }

    @Override
    public List<Song> getAllSongs() throws IOException {
        return loadSongList();
    }

    @Override
    public Song getSongById(String songId) throws IOException {
        List<Song> songs = loadSongList();
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getId().equals(songId)) {
                return songs.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean addSong(Song song) throws IOException {

        JsonNode root =loadJsonData(dataDir+"/popular_songs.json");
        ((ArrayNode) root).add(objectMapper.valueToTree(song));
        ClassPathResource path= new ClassPathResource(dataDir+"/popular_songs.json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.getFile(), root);
        return true;
    }
    @Override
    public boolean updateSong(String songId, Song updatedSong) throws IOException {
        deleteSongById(songId);
        addSong(updatedSong);
        return true;
    }

    @Override
    public boolean deleteSongById(String songId) throws IOException {

        JsonNode rootNode = loadJsonData(dataDir+"/popular_songs.json");

        for (int i = 0; i < ((ArrayNode) rootNode).size(); i++) {
            if (Objects.equals(((ArrayNode) rootNode).get(i).get("id").asText(), songId)){
                ((ArrayNode) rootNode).remove(i);
            }
        }
        ClassPathResource path= new ClassPathResource(dataDir+"/popular_songs.json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.getFile(), rootNode);
        return true;
    }

    private JsonNode loadJsonData(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return objectMapper.readTree(resource.getFile());
    }
    private void saveJsonData(String path, Object data) throws IOException {
        File file = new ClassPathResource(path).getFile();
        objectMapper.writeValue(file, data);
    }
    private Map<String, Album> loadAlbumMap() throws IOException {
        JsonNode albumsNode = loadJsonData(dataDir+"/albums.json");
        return objectMapper.convertValue(albumsNode, new TypeReference<Map<String, Album>>() {});
    }
    private List<Artist> loadArtistList() throws IOException {
        JsonNode artistsNode = loadJsonData(dataDir+"/popular_artists.json");
        return objectMapper.convertValue(artistsNode, new TypeReference<List<Artist>>() {});
    }
    private List<Song> loadSongList() throws IOException {
        JsonNode songsNode = loadJsonData(dataDir+"/popular_songs.json");
        if (songsNode == null ) {
            return null;
        }
        List<Song> songs= new ArrayList<>();
        for (JsonNode song:songsNode){
            songs.add( objectMapper.treeToValue(song, Song.class));
        }
        return songs;
    }
}
