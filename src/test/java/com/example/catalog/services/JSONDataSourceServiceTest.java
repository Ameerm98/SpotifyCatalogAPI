package com.example.catalog.services;

import com.example.catalog.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // Ensures we're using the test profile
@SpringBootTest
public class JSONDataSourceServiceTest {

    @Autowired
    private JSONDataSourceService jsonDataSourceService;
    private Album newAlbum ;
    private Artist newArtist;
    private Track newTrack;
    private  Album updatedAlbum;
    private Artist updatedArtist;
    private Song newSong;
    private Song updatedSong;
    private Artist testArtist;


    @BeforeEach
    public void setUp(){
        ///  new album created
        newAlbum = new Album();
        newAlbum.setId("new_test_album");
        newAlbum.setName("New Test Album");
        newAlbum.setTotalTracks(2);
        newAlbum.setReleaseDate("2025-02-01");

        // Create and set images for the album
        Image image = new Image();
        image.setUrl("http://example.com/image.jpg");
        image.setHeight(300);
        image.setWidth(300);
        newAlbum.setImages(Arrays.asList(image));

        // Create and set tracks for the album
        Track track1 = new Track();
        track1.setId("track1");
        track1.setName("Track One");
        track1.setUri("http://example.com/track1");
        track1.setDurationMs(210000); // 3:30 minutes
        track1.setExplicit(false);

        Track track2 = new Track();
        track2.setId("track2");
        track2.setName("Track Two");
        track2.setUri("http://example.com/track2");
        track2.setDurationMs(240000); // 4 minutes
        track2.setExplicit(true);

        newAlbum.setTracks(Arrays.asList(track1, track2));



        /// updated Album
        updatedAlbum = new Album();
        updatedAlbum.setId("new_test_album");
        updatedAlbum.setName("Updated Test Album");
        updatedAlbum.setTotalTracks(0);
        updatedAlbum.setReleaseDate("2025-03-01");



        /// new Track Created
        newTrack = new Track();
        newTrack.setId("new_track_id");
        newTrack.setName("New Track");
        newTrack.setDurationMs(200);



        ///  new Artist Added

        // Create a new artist
        newArtist = new Artist();
        newArtist.setId("4dpARuHxo51G3z7JY4O3Qx");  // Adele's Artist ID
        newArtist.setName("Adele");

// Set followers count
        newArtist.setFollowers(39319887);

// Set popularity score
        newArtist.setPopularity(92);

// Set URI (Spotify URI for Adele)
        newArtist.setUri("spotify:artist:4dpARuHxo51G3z7JY4O3Qx");

// Set genres
        List<String> genres = new ArrayList<>();
        genres.add("pop");
        genres.add("british soul");
        genres.add("soul");
        newArtist.setGenres(genres);

// Create a list of images (this is a simplified version with a few image sizes)
        List<Image> images = new ArrayList<>();

        Image image1 = new Image();
        image1.setUrl("ab6761610000e5eb9e528993a2820267b97f6aae.jpeg");
        image1.setHeight(640);
        image1.setWidth(640);

        Image image2 = new Image();
        image2.setUrl("ab676161000051749e528993a2820267b97f6aae.jpeg");
        image2.setHeight(320);
        image2.setWidth(320);

        Image image3 = new Image();
        image3.setUrl("ab6761610000f1789e528993a2820267b97f6aae.jpeg");
        image3.setHeight(160);
        image3.setWidth(160);

// Add images to the list
        images.add(image1);
        images.add(image2);
        images.add(image3);

// Set images for the artist
        newArtist.setImages(images);



        /// updated Artist Creation
        updatedArtist = new Artist();

        // Set new details for updatedArtist
        updatedArtist.setId("4dpARuHxo51G3z7JY4O3Qx");  // Same as Adele's Artist ID (for update)
        updatedArtist.setName("Adele (Updated)");

        // Updated follower count
        updatedArtist.setFollowers(40000000); // Suppose her followers grew

        // Updated popularity score
        updatedArtist.setPopularity(95); // Her popularity increased

        // Keeping the same URI for Adele
        updatedArtist.setUri("spotify:artist:4dpARuHxo51G3z7JY4O3Qx");

        // Add more genres or change genres if needed
        List<String> updatedGenres = new ArrayList<>();
        updatedGenres.add("pop");
        updatedGenres.add("british soul");
        updatedGenres.add("soul");
        updatedGenres.add("r&b"); // Added new genre
        updatedArtist.setGenres(updatedGenres);

        // Create updated image list with possibly new/updated images
        List<Image> updatedImages = new ArrayList<>();

        // Adding new updated images
        Image updatedImage1 = new Image();
        updatedImage1.setUrl("ab6761610000e5eb9e528993a2820267b97f6aae_updated.jpeg");
        updatedImage1.setHeight(640);
        updatedImage1.setWidth(640);

        Image updatedImage2 = new Image();
        updatedImage2.setUrl("ab676161000051749e528993a2820267b97f6aae_updated.jpeg");
        updatedImage2.setHeight(320);
        updatedImage2.setWidth(320);

        Image updatedImage3 = new Image();
        updatedImage3.setUrl("ab6761610000f1789e528993a2820267b97f6aae_updated.jpeg");
        updatedImage3.setHeight(160);
        updatedImage3.setWidth(160);

        // Add updated images to the list
        updatedImages.add(updatedImage1);
        updatedImages.add(updatedImage2);
        updatedImages.add(updatedImage3);

        // Set images for the updated artist
        updatedArtist.setImages(updatedImages);

        // Setup a new Song
        newSong = new Song();
        newSong.setId("new_song_id");
        newSong.setName("New Song");
        newSong.setDurationMs(210000);
        newSong.setUri("http://example.com/newsong");

        // Create a test artist for the song
        testArtist = new Artist();
        testArtist.setId("artist_id_123");
        testArtist.setName("Test Artist");
        newSong.setArtists(List.of(testArtist));

        // Setup updated song
        updatedSong = new Song();
        updatedSong.setId("new_song_id");
        updatedSong.setName("Updated Song");
        updatedSong.setDurationMs(250000); // 4:10 minutes
        updatedSong.setUri("http://example.com/updatedsong");
        updatedSong.setArtists(List.of(testArtist));

    }

    @Test
    public void testGetArtistById() throws IOException {
        // Test with a valid artist ID
        Artist artist = jsonDataSourceService.getArtistById("1Xyo4u8uXC1ZmMpatF05PJ");
        assertNotNull(artist);
        assertEquals("The Weeknd", artist.getName());

        // Test with an invalid artist ID
        artist = jsonDataSourceService.getArtistById("invalid_id");
        assertNull(artist);
    }

    @Test
    public void testGetAlbums() throws IOException {
        // Test fetching albums
        List<Album> albums = jsonDataSourceService.getAlbums();
        assertNotNull(albums);
        //assertEquals(92, albums.size()); // Check that albums are returned
        assertEquals("After Hours", albums.get(0).getName()); // First album name

        // Simulate an empty file and ensure the service handles it correctly
        // (You could adjust your JSON or mock the `loadJsonData` method here if required)
    }

    @Test
    public void testGetAlbumById() throws IOException {
        // Test fetching a specific album by ID
        Album album = jsonDataSourceService.getAlbumById("4yP0hdKOZPNshxUOjY0cZj");
        assertNotNull(album);
        assertEquals("After Hours", album.getName());

        // Test with a non-existing album ID
        album = jsonDataSourceService.getAlbumById("non_existing_album");
        assertNull(album);
    }

    @Test
    public void testAddAlbum() throws IOException {
        // Create a new album

        // Add the album
        boolean response = jsonDataSourceService.addAlbum(newAlbum);
        assertTrue(response);
        // Verify the album is added by retrieving it by ID
        Album albumResponse = jsonDataSourceService.getAlbumById("new_test_album");
        assertNotNull(albumResponse);
        assertEquals("New Test Album", albumResponse.getName());
        assertEquals("2025-02-01", albumResponse.getReleaseDate());
        assertEquals(2, albumResponse.getTotalTracks());
        assertNotNull(albumResponse.getImages());
        assertFalse(albumResponse.getImages().isEmpty());
        assertNotNull(albumResponse.getTracks());
        assertEquals(2, albumResponse.getTracks().size());
        jsonDataSourceService.deleteAlbumById("new_test_album");
    }



    /// ////not tested yet
    @Test
    public void testUpdateAlbum() throws IOException {
        // Create an album to update

        jsonDataSourceService.addAlbum(newAlbum);
        // Update the album
        boolean response = jsonDataSourceService.updateAlbum("new_test_album", updatedAlbum);
        assertTrue(response);// Status code for successful update is NO_CONTENT (204)

        // Verify the album is updated
        Album albumResponse = jsonDataSourceService.getAlbumById("new_test_album");
        assertNotNull(albumResponse);
        assertEquals("Updated Test Album", albumResponse.getName());
        assertEquals(0, albumResponse.getTotalTracks());
        jsonDataSourceService.deleteAlbumById("new_test_album");

    }

    @Test
    public void testDeleteAlbumById() throws IOException {
        // Add Album for testing delete
        jsonDataSourceService.addAlbum(newAlbum);
        // Assuming an album with ID "test_album_id" exists
        boolean response = jsonDataSourceService.deleteAlbumById("new_test_album");
        assertTrue(response); // Check for successful deletion

        // Verify the album is deleted
        Album albumResponse = jsonDataSourceService.getAlbumById("new_test_album");
        assertNull(albumResponse);
    }

    @Test
    public void testGetArtistAlbums() throws IOException {
        // Test that we can fetch albums for a specific artist
        List<Album> albums = jsonDataSourceService.getArtistAlbums("1Xyo4u8uXC1ZmMpatF05PJ");
        assertNotNull(albums);
        assertFalse(albums.isEmpty()); // Ensure there are albums for the artist
        assertEquals("After Hours",albums.get(0).getName());
        assertEquals("Starboy",albums.get(1).getName());
        assertEquals("Beauty Behind The Madness",albums.get(2).getName());
    }

    @Test
    public void testGetArtistSongs() throws IOException {
        List<Song> songs = jsonDataSourceService.getArtistSongs("1Xyo4u8uXC1ZmMpatF05PJ");
        assertNotNull(songs);
        assertFalse(songs.isEmpty()); // Ensure there are albums for the artist
        assertEquals("Blinding Lights",songs.get(0).getName());
        assertEquals("Starboy",songs.get(1).getName());
        assertEquals("Die For You",songs.get(2).getName());
        assertEquals("The Hills",songs.get(3).getName());

    }

    @Test
    public void testAddTrackToAlbum() throws IOException {

        jsonDataSourceService.addAlbum(newAlbum);
        // Create a new track to add to an album


        // Add the track to the album
        boolean response = jsonDataSourceService.addTrackToAlbum("new_test_album", newTrack);
        assertTrue(response);
        // Fetch the album and verify the track is added
        Album albumResponse = jsonDataSourceService.getAlbumById("new_test_album");
        assertNotNull(albumResponse);
        assertNotSame("new_track_id", albumResponse.getTracks().get(2).getId()); // Ensure the album has tracks

        jsonDataSourceService.deleteAlbumById("new_test_album");
    }

    @Test
    public void testDeleteTrackFromAlbum() throws IOException {

        jsonDataSourceService.addAlbum(newAlbum);
        // Create a new track to add to an album


        // Add the track to the album
        boolean response = jsonDataSourceService.addTrackToAlbum("new_test_album", newTrack);
        assertTrue(response);

        // Delete an existing track from the album
        response = jsonDataSourceService.deleteTrackFromAlbum("new_test_album", "new_track_id");
        assertTrue(response);

        // Fetch the album and ensure the track is deleted
        Album albumResponse = jsonDataSourceService.getAlbumById("new_test_album");
        assertNotNull(albumResponse);
        assertTrue(albumResponse.getTracks().stream()
                .noneMatch(track -> track.getId().equals("new_track_id"))); // Verify the track is no longer in the album

        jsonDataSourceService.deleteAlbumById("new_test_album");
    }

    @Test
    public void testAddArtist() throws IOException {


        // Add the artist
        boolean response = jsonDataSourceService.addArtist(newArtist);
        assertTrue(response);

        // Verify the artist is added
        Artist artistResponse = jsonDataSourceService.getArtistById("4dpARuHxo51G3z7JY4O3Qx");
        assertNotNull(artistResponse);
        assertEquals("Adele", artistResponse.getName());

        // delete the artist added
        jsonDataSourceService.deleteArtistById(newArtist.getId());
    }

    @Test
    public void testDeleteArtistById() throws IOException {

        boolean response = jsonDataSourceService.addArtist(newArtist);
        assertTrue(response);
        // Delete an existing artist
        response = jsonDataSourceService.deleteArtistById("4dpARuHxo51G3z7JY4O3Qx");
        assertTrue(response);

        // Verify the artist is deleted
        Artist artistResponse = jsonDataSourceService.getArtistById("4dpARuHxo51G3z7JY4O3Qx");
        assertNull(artistResponse);
    }

    @Test
    public void testUpdateArtist() throws IOException {
        boolean response = jsonDataSourceService.addArtist(newArtist);
        assertTrue(response);


        // Update the artist in the service (assuming this method is available)
        response = jsonDataSourceService.updateArtist("4dpARuHxo51G3z7JY4O3Qx", updatedArtist);

        // Assert that the response indicates a successful update
        assertTrue(response, "Artist should be updated successfully.");

        // Verify the artist is updated
        Artist artistResponse = jsonDataSourceService.getArtistById("4dpARuHxo51G3z7JY4O3Qx");
        assertNotNull(artistResponse);
        assertEquals("Adele (Updated)", artistResponse.getName());
        assertEquals(40000000, artistResponse.getFollowers());
        assertEquals(95, artistResponse.getPopularity());
        assertTrue(artistResponse.getGenres().contains("r&b"), "Artist genres should include 'r&b'.");

        // Cleanup: Delete the artist after the test
        jsonDataSourceService.deleteArtistById("4dpARuHxo51G3z7JY4O3Qx");

    }
    @Test
    public void testGetAllSongs() throws IOException {
        // Add some songs to the service
        jsonDataSourceService.addSong(newSong);
        Song anotherSong = new Song();
        anotherSong.setId("another_song_id");
        anotherSong.setName("Another Song");
        anotherSong.setDurationMs(180000); // 3 minutes
        anotherSong.setUri("http://example.com/anotherSong");
        anotherSong.setArtists(List.of(testArtist));
        jsonDataSourceService.addSong(anotherSong);

        // Test fetching all songs
        List<Song> songs = jsonDataSourceService.getAllSongs();
        assertNotNull(songs);
        assertFalse(songs.isEmpty()); // Ensure there are songs
        assertEquals(102,songs.size());
        assertTrue(songs.stream().anyMatch(song -> song.getName().equals("New Song")));
        assertTrue(songs.stream().anyMatch(song -> song.getName().equals("Another Song")));

        // Cleanup
        jsonDataSourceService.deleteSongById("new_song_id");
        jsonDataSourceService.deleteSongById("another_song_id");
    }

    @Test
    public void testGetSongById() throws IOException {

        // Test that we can fetch songs for a specific artist
        Song song = jsonDataSourceService.getSongById("0VjIjW4GlUZAMYd2vXMi3b");
        assertNotNull(song);
        assertEquals("Blinding Lights", song.getName());
        assertEquals(87,song.getPopularity());
    }



    @Test
    public void testAddSong() throws IOException {
        // Add a new song
        boolean response = jsonDataSourceService.addSong(newSong);
        assertTrue(response);

        // Verify the song is added by retrieving it by ID
        Song songResponse = jsonDataSourceService.getSongById("new_song_id");
        assertNotNull(songResponse);
        assertEquals("New Song", songResponse.getName());
        assertEquals("http://example.com/newsong", songResponse.getUri());

        // Cleanup
        jsonDataSourceService.deleteSongById("new_song_id");
    }

    @Test
    public void testUpdateSong() throws IOException {
        // Add the song to update
        jsonDataSourceService.addSong(newSong);

        // Update the song
        boolean response = jsonDataSourceService.updateSong("new_song_id", updatedSong);
        assertTrue(response);

        // Verify the song is updated
        Song songResponse = jsonDataSourceService.getSongById("new_song_id");
        assertNotNull(songResponse);
        assertEquals("Updated Song", songResponse.getName());
        assertEquals(250000, songResponse.getDurationMs()); // 4:10 minutes
        assertEquals("http://example.com/updatedsong", songResponse.getUri());

        // Cleanup
        jsonDataSourceService.deleteSongById("new_song_id");
    }


    @Test
    public void testDeleteSongById() throws IOException {
        // Add the song for testing delete
        jsonDataSourceService.addSong(newSong);

        // Delete the song
        boolean response = jsonDataSourceService.deleteSongById("new_song_id");
        assertTrue(response);

        // Verify the song is deleted
        Song songResponse = jsonDataSourceService.getSongById("new_song_id");
        assertNull(songResponse);

    }


}
