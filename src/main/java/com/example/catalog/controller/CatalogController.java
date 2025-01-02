package com.example.catalog.controller;

import com.example.catalog.model.Artist;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class CatalogController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private LRUCache<String,JsonNode> cache = new LRUCache<>(10);


    @GetMapping("/popularSongs")
    public ResponseEntity<List<JsonNode>> getPopularSongs(@RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "-1") int limit) throws IOException {
        ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
        JsonNode songs = objectMapper.readTree(resource.getFile());

        if (offset==0 && limit==-1){
            limit =songs.size();
        }
        List<JsonNode> songsList = new ArrayList<>();
        int i =0;
        int count = limit;
        for (JsonNode song:songs){
            if(i>=offset && count>0){
                songsList.add(song);
                count-=1;
            }
            i+=1;
        }
        return ResponseEntity.ok(songsList);
    }


/*
    @GetMapping("/popularSongs")
    public ResponseEntity<JsonNode> getPopularSongs() throws IOException {
        try {

            ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
            return new ResponseEntity<>(objectMapper.readTree(resource.getFile()),HttpStatus.OK);
        }catch (Exception e){
            ObjectNode errResopnseMod = objectMapper.createObjectNode();
            errResopnseMod.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errResopnseMod.put("message","internal error");
            return new ResponseEntity<>(errResopnseMod,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */


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

    @GetMapping("/albums/{id}")
    public ResponseEntity<JsonNode> getAlbumById(@PathVariable String id) throws IOException {
        try {
            if(id.matches("0+")){
                ObjectNode errResopnseMod = objectMapper.createObjectNode();
                errResopnseMod.put("status", HttpStatus.FORBIDDEN.value());
                errResopnseMod.put("message","forbidden id");
                return new ResponseEntity<>(errResopnseMod,HttpStatus.FORBIDDEN);
            }
            if (! SpotifyUtils.isValidId(id)) {
                ObjectNode errResopnseMod = objectMapper.createObjectNode();
                errResopnseMod.put("status", HttpStatus.BAD_REQUEST.value());
                errResopnseMod.put("message","invalid id");
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
                errResopnseMod.put("status", HttpStatus.NOT_FOUND.value());
                errResopnseMod.put("message","Album not found");
                return new ResponseEntity<>(errResopnseMod,HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            ObjectNode errResopnseMod = objectMapper.createObjectNode();
            errResopnseMod.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errResopnseMod.put("message","internal error");
            return new ResponseEntity<>(errResopnseMod,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable String id) throws IOException {
        if (! SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }

        ClassPathResource resource = new ClassPathResource("data/popular_artists.json");
        JsonNode artists = objectMapper.readTree(resource.getFile());

        JsonNode artistNode = artists.get(id);
        if (artistNode == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return  ResponseEntity.ok(objectMapper.treeToValue(artistNode, Artist.class));
    }

    @PostMapping("/artists")
    public void addArtist(@RequestBody Artist artist) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode artistNode = objectMapper.valueToTree(artist);  // convert the Artists object to JsonNode
        File file = new File("/Users/ameermasarwa/IdeaProjects/SpotifyCatalogAPI/src/main/resources/data/popular_artists.json");
        JsonNode rootNode = objectMapper.readTree(file);


        if (rootNode.isObject()){
            ObjectNode array = (ObjectNode) rootNode;
            String id = artistNode.get("id").asText();
            array.set(id,artistNode);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file,array);
        }
        // TODO your implementation
    }


    @GetMapping("/popularSongs/filter?name={songName}&minPopularity={minPopularity}")
    public ResponseEntity<List<JsonNode>> filterSongsByName(@PathVariable String songName,@PathVariable String minPopularity) throws IOException {
            CatalogUtils catalog = new CatalogUtils();
            ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
            JsonNode songs = objectMapper.readTree(resource.getFile());
            List<JsonNode> songsfilterdbyname = songs.findValues(songName);
            List<JsonNode> res = catalog.filterSongsByPopularity(songsfilterdbyname,Integer.valueOf(minPopularity));
            return new ResponseEntity<>(res,HttpStatus.OK);
    }
   //  GET
   @GetMapping("/songs/mostRecent")
   public ResponseEntity<JsonNode> mostRecentSong() throws IOException {
       CatalogUtils catalog = new CatalogUtils();
       ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
       JsonNode songs = objectMapper.readTree(resource.getFile());
        List<JsonNode> songsList = new ArrayList<>();
        for (JsonNode song:songs){
            songsList.add(song);
        }
        return new ResponseEntity<>(catalog.getMostRecentSong(songsList),HttpStatus.OK);
   }

    @GetMapping("/songs/longest")
    public ResponseEntity<JsonNode> longestSong() throws IOException {
        CatalogUtils catalog = new CatalogUtils();
        ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
        JsonNode songs = objectMapper.readTree(resource.getFile());
        List<JsonNode> songsList = new ArrayList<>();
        for (JsonNode song:songs){
            songsList.add(song);
        }
        return new ResponseEntity<>(catalog.getLongestSong(songsList),HttpStatus.OK);
    }



}

