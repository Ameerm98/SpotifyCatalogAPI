package com.example.catalog.controller;

import com.example.catalog.model.Album;
import com.example.catalog.model.Track;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.utils.SpotifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final DataSourceService dataSourceService;

    @Autowired
    public AlbumController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    // ------------------------ GET Requests ------------------------

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() throws IOException {
        List<Album> albums = dataSourceService.getAlbums();
        if (albums==null){
            return new  ResponseEntity(List.of(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(albums,HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
       Album album = dataSourceService.getAlbumById(id);
        if (album == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return new ResponseEntity<>(album,HttpStatus.OK);
    }

    @GetMapping("/{id}/tracks")
    public ResponseEntity<List<Track>> getAlbumTracks(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }

        List<Track> tracks = dataSourceService.getAlbumTracks(id);
        if (tracks==null){
            return new  ResponseEntity(List.of(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tracks,HttpStatus.OK);

    }

    // ------------------------ POST Requests ------------------------

    @PostMapping
    public ResponseEntity<Void> addAlbum(@RequestBody Album album) throws IOException {
        if (album.getId() == null || album.getName() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        boolean res = dataSourceService.addAlbum(album);
        if (!res){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{id}/tracks")
    public ResponseEntity<Void> addTrackToAlbum(@PathVariable String id, @RequestBody Track track) throws IOException {
        if (!SpotifyUtils.isValidId(id) || track.getId() == null || track.getName() == null) {
            return ResponseEntity.badRequest().build();
        }
        boolean res = dataSourceService.addTrackToAlbum(id,track);
        if (!res){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // ------------------------ PUT Requests ------------------------

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAlbum(@PathVariable String id, @RequestBody Album updatedAlbum) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }

        boolean res = dataSourceService.updateAlbum(id, updatedAlbum);
        if (!res){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PutMapping("/{id}/tracks/{trackId}")
    public ResponseEntity<Void> updateTrack(@PathVariable String id, @PathVariable String trackId, @RequestBody Track updatedTrack) throws IOException {
        if (!SpotifyUtils.isValidId(id) || !SpotifyUtils.isValidId(trackId)) {
            return ResponseEntity.badRequest().build();
        }
        boolean res = dataSourceService.updateTrack(id, trackId, updatedTrack);
        if (!res){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // ------------------------ DELETE Requests ------------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        boolean res =  dataSourceService.deleteAlbumById(id);
        if (!res){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}/tracks/{trackId}")
    public ResponseEntity<Void> deleteTrackFromAlbum(@PathVariable String id, @PathVariable String trackId) throws IOException {
        if (!SpotifyUtils.isValidId(id) || !SpotifyUtils.isValidId(trackId)) {
            return ResponseEntity.badRequest().build();
        }
        boolean res =  dataSourceService.deleteTrackFromAlbum(id, trackId);
        if (!res){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
