package com.example.catalog.controller;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.services.DataSourceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.catalog.utils.SpotifyUtils;
import com.example.catalog.utils.CatalogUtils;
import com.example.catalog.utils.LRUCache;

import javax.swing.table.TableRowSorter;

@RestController
public class ArtistController {

    private final DataSourceService dataSourceService;

    @Autowired
    public ArtistController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    /*
    @GetMapping("/popularArtists")
    public ResponseEntity<JsonNode> getPopularArtists() throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource("data/popular_artists.json");
            return new ResponseEntity<>(objectMapper.readTree(resource.getFile()),HttpStatus.OK);
        } catch (Exception e) {
            ObjectNode errResopnseMod = objectMapper.createObjectNode();
            errResopnseMod.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errResopnseMod.put("message","internal error");
            return new ResponseEntity<>(errResopnseMod,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     */


    @GetMapping("/artists")
    public ResponseEntity<List<Artist>> getAllArtists() throws IOException {

        List<Artist> artists = dataSourceService.getArtists();
        if (artists==null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>( artists,HttpStatus.OK);
    }
    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable String id) throws IOException {
        if (! SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        Artist artist =  dataSourceService.getArtistById(id);
        if (artist==null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>(artist,HttpStatus.OK);

    }


    @PostMapping("/artists")
    public ResponseEntity<Void> addArtist(@RequestBody Artist artist) throws IOException {
        if (artist.getId() == null || artist.getName() == null) {
            return ResponseEntity.badRequest().build(); // Missing required fields
        }

        // Add the artist using the service layer
        boolean res =dataSourceService.addArtist(artist);
        if (res){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Return the created artist with HTTP 201 Created status
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Void> updateArtist(@PathVariable String artistId, @RequestBody Artist artist) throws IOException {
        if (!SpotifyUtils.isValidId(artistId)) {
            return ResponseEntity.badRequest().build();
        }
        boolean res =dataSourceService.updateArtist(artistId,artist);
        if (res){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Void> deleteArtistById(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        // Return HTTP 204 No Content status (successful deletion)
        boolean res =dataSourceService.deleteArtistById(id);
        if (res){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>( HttpStatus.OK);

    }
    @GetMapping("/artists/{id}/albums")
    public ResponseEntity<List<Album>> getArtistAlbums(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        List<Album> albums =  dataSourceService.getArtistAlbums(id);
        if (albums==null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>( albums,HttpStatus.OK);
    }

    @GetMapping("/artists/{id}/songs")
    public ResponseEntity<List<Song>> getArtistSongs(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        List<Song>  songs=  dataSourceService.getArtistSongs(id);
        if (songs==null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>( songs,HttpStatus.OK);
    }

}
