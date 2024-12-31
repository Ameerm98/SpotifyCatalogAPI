package com.example.catalog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.example.catalog.utils.SpotifyUtils.*;
import static org.junit.jupiter.api.Assertions.*;


public class SpotifyUtilsTest {

    @Test
    public void testValidId() {
        assertTrue(isValidId("6rqhFgbbKwnb9MLmUQDhG6")); // valid Spotify ID
        assertTrue(isValidId("1a2B3c4D5e6F7g8H9iJkL0mN")); // valid 24 character ID
        assertTrue(isValidId("a1b2C3d4E5f6G7h8I9jK0L1m2N4fgY")); // valid 30 character ID
    }

    @Test
    public void testValidURI(){
        assertTrue(isValidURI("spotify:artist:ameermasarwa1998"));
        assertTrue(isValidURI("spotify:track:sweetmoments20002207"));
        assertTrue(isValidURI("spotify:playlist:sweetmoments20002207"));
        assertTrue(isValidURI("spotify:album:sweetmoments20002207"));
        assertFalse(isValidURI("spotify:artist:ameerm1998"));
        assertFalse(isValidURI("spotify:track"));
        assertFalse(isValidURI("spotify:sweetmoments20002207"));
        assertFalse(isValidURI("album:sweetmoments20002207"));

    }

    @Test
    public void testSpotifyClient(){
        assertThrows(IllegalArgumentException.class,()->{
            getSpotifyClient("","ab");
        });
        assertThrows(IllegalArgumentException.class,()->{
            getSpotifyClient("ab","");
        });
        assertThrows(UnsupportedOperationException.class,()->{
            getSpotifyClient("ameer","masarwa");
        });




    }
    @Test
    public void testInvalidId() {
        assertFalse(isValidId(null)); // null ID
        assertFalse(isValidId("")); // empty ID
        assertFalse(isValidId("shortID")); // too short ID (less than 15 characters)
        assertFalse(isValidId("thisIDiswaytoolongtobevalidddddd")); // too long ID (more than 30 characters)
        assertFalse(isValidId("!@#$$%^&*()_+")); // invalid characters
        assertFalse(isValidId("1234567890abcdefGHIJKLMNO!@#")); // includes invalid characters
    }

}
