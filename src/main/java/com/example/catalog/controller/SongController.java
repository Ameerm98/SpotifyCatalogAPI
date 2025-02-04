package com.example.catalog.controller;

import com.example.catalog.model.Song;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.utils.SpotifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tracks")
public class SongController {


    private final DataSourceService dataSourceService;

    @Autowired
    public SongController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    // ------------------------ GET Requests ------------------------

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() throws IOException {

        List<Song> songs = dataSourceService.getAllSongs();
        if (songs==null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>( songs,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }

        Song song =  dataSourceService.getSongById(id);
        if (song==null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>( song,HttpStatus.OK);
    }

    // ------------------------ POST Request ------------------------

    @PostMapping
    public ResponseEntity<Void> addSong(@RequestBody Song song) throws IOException {
        if (song.getId() == null || song.getName() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        boolean res = dataSourceService.addSong(song);
        if (!res){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // ------------------------ PUT Request ------------------------

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSong(@PathVariable String id, @RequestBody Song updatedSong) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        boolean res =dataSourceService.updateSong(id, updatedSong);
        if (!res){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // ------------------------ DELETE Request ------------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        boolean res =dataSourceService.deleteSongById(id);
        if (!res){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
