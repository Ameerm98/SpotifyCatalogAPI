package com.example.catalog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import com.example.catalog.utils.SpotifyUtils;
import com.example.catalog.utils.LRUCache;

import javax.swing.table.TableRowSorter;

@RestController
public class CatalogController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private LRUCache<String,JsonNode> cache = new LRUCache<>(10);

    @GetMapping("/popularSongs")
    public ResponseEntity<JsonNode> getPopularSongs() throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
            return new ResponseEntity<>(objectMapper.readTree(resource.getFile()),HttpStatus.OK);
        }catch (Exception e){
            ObjectNode errResopnseMod = objectMapper.createObjectNode();
            errResopnseMod.put("message","internal error");
            errResopnseMod.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errResopnseMod,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/popularArtists")
    public ResponseEntity<JsonNode> getPopularArtists() throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource("data/popular_artists.json");
            return new ResponseEntity<>(objectMapper.readTree(resource.getFile()),HttpStatus.OK);
        } catch (Exception e) {
            ObjectNode errResopnseMod = objectMapper.createObjectNode();
            errResopnseMod.put("message","internal error");
            errResopnseMod.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errResopnseMod,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<JsonNode> getAlbumById(@PathVariable String id) throws IOException {
        try {
            if(id.matches("0+")){
                ObjectNode errResopnseMod = objectMapper.createObjectNode();
                errResopnseMod.put("message","forbidden id");
                errResopnseMod.put("status", HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<>(errResopnseMod,HttpStatus.FORBIDDEN);
            }
            if (! SpotifyUtils.isValidId(id)) {
                ObjectNode errResopnseMod = objectMapper.createObjectNode();
                errResopnseMod.put("message","invalid id");
                errResopnseMod.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errResopnseMod,HttpStatus.BAD_REQUEST);
            }
            JsonNode albumsaved = cache.get(id);
            if(albumsaved != null){
                return new ResponseEntity<>(albumsaved,HttpStatus.OK);
            }
            ClassPathResource resource = new ClassPathResource("data/albums.json");
            JsonNode albums = objectMapper.readTree(resource.getFile());
            JsonNode album = albums.get(id);
            if (album != null) {
                cache.set(id,album);
                return new ResponseEntity<>(album,HttpStatus.OK);
            } else {
                ObjectNode errResopnseMod = objectMapper.createObjectNode();
                errResopnseMod.put("message","Album not found");
                errResopnseMod.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(errResopnseMod,HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            ObjectNode errResopnseMod = objectMapper.createObjectNode();
            errResopnseMod.put("message","internal error");
            errResopnseMod.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errResopnseMod,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

