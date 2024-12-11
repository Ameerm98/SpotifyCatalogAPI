package com.example.catalog.utils;

import java.util.regex.Pattern;

public class SpotifyUtils {

    private static final Pattern SPOTIFY_URI_PATTERN = Pattern.compile(
            "^spotify:(track|album|artist|playlist):[0-9a-zA-Z]{15,30}$"
    );

    /**
     * Validates if the given URI is a valid Spotify URI.
     * A valid Spotify URI is a string that starts with `spotify:`,
     * followed by a resource type such as `track`, `album`, `artist`, or `playlist`,
     * and ends with a 15-30 characters Base62 identifier.
     *
     * @param uri The URI to validate.
     * @return True if the URI is valid, false otherwise.
     */
    public static boolean isValidURI(String uri) {
        if (uri == null || uri.isEmpty()) {
            return false;
        }
        return SPOTIFY_URI_PATTERN.matcher(uri).matches();
    }

    /**
     * Validates if a given string is a valid Spotify ID.
     *
     * @param id The string to validate.
     * @return true if the string is a valid Spotify ID, false otherwise.
     */
    public static boolean isValidId(String id) {
        if(id == null || id.isEmpty() || id.length()<15 || id.length()>30){
            return false;
        }
        return id.matches("[0-9a-zA-Z]*");
    }

    /**
     * Connect with the real Spotify API.
     * Currently, it throws an UnsupportedOperationException to indicate that this feature is not yet implemented.
     *
     * @param clientId The client ID for the Spotify API.
     * @param clientSecret The client secret for the Spotify API.
     * @throws IllegalArgumentException if the client ID or secret is invalid.
     *
     * @throws UnsupportedOperationException as this feature is not yet implemented.
     */
    public static void getSpotifyClient(String clientId, String clientSecret) {
        if (clientId == null || clientId.isEmpty() || clientSecret == null || clientSecret.isEmpty()) {
            throw new IllegalArgumentException("Invalid client ID or secret.");
        }

        throw new UnsupportedOperationException("Spotify API client is not yet implemented.");
    }

}
