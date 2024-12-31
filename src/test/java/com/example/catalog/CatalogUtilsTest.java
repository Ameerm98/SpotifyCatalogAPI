package com.example.catalog;
import static org.junit.jupiter.api.Assertions.*;
import com.example.catalog.utils.CatalogUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

class CatalogUtilsTest {

    private CatalogUtils catalogUtils;
    private List<JsonNode> songs;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        catalogUtils = new CatalogUtils();
        objectMapper = new ObjectMapper();

        // Sample song data for testing. TODO - Add more songs
        String jsonData = """
                    [
                        {
                          "duration_ms": 200040,
                          "name": "Blinding Lights",
                          "popularity": 87,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2023-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 222040,
                          "name": "Sweet Heart",
                          "popularity": 55,
                          "album": {
                            "name": "Hearts",
                            "release_date": "1998-02-25",
                            "total_tracks": 9
                          },
                          "artists": [
                            {
                              "name": "cwby"
                            }
                          ]
                        },
                        {
                          "duration_ms": 172723,
                          "name": "Stay With Me",
                          "popularity": 1,
                          "album": {
                            "name": "In The Lonely Hour",
                            "release_date": "2014-01-01",
                            "total_tracks": 10
                          },
                          "artists": [
                            {
                              "name": "Sam Smith"
                            }
                          ]
                        },
                        {
                          "duration_ms": 153529,
                          "name": "Daydreaming",
                          "popularity": 46,
                          "album": {
                            "name": "Daydreaming",
                            "release_date": "2020-02-25",
                            "total_tracks": 1
                          },
                          "artists": [
                            {
                              "name": "Bruklin"
                            }
                          ]
                        },
                        {
                          "duration_ms": 153529,
                          "name": "DayHunter",
                          "popularity": 46,
                          "album": {
                            "name": "Daydreaming",
                            "release_date": "2020-02-25",
                            "total_tracks": 1
                          },
                          "artists": [
                            {
                              "name": "Bruklin"
                            }
                          ]
                        }
                    ]
                """;
        songs = new ArrayList<>();
        objectMapper.readTree(jsonData).forEach(songs::add);
    }


    @Test
    public void testSortSongsByName(){
       List<JsonNode> res =  catalogUtils.sortSongsByName(songs);
       JsonNode s = res.get(0);
       String basestr = s.get("name").asText();
       for (JsonNode song:res){
           String str = song.get("name").asText();
           int rescmp = basestr.compareTo(str);
           assert (rescmp <0 || rescmp==0 ):"Songs List is Not Sorted";
           basestr=str;
       }
    }

    @Test
    public void testFilterSongsByPopularity(){
        Random rnd = new Random();
        int randnum = rnd.nextInt()+1;
        List<JsonNode> res =  catalogUtils.filterSongsByPopularity(songs,randnum);
        for (JsonNode song:res){
            assert song.get("popularity").asInt() >=randnum :"SongsList not filtered as needed ";

        }
        randnum = rnd.nextInt()+1;
        res =  catalogUtils.filterSongsByPopularity(songs,randnum);
        for (JsonNode song:res){
            assert song.get("popularity").asInt() >=randnum :"SongsList not filtered as needed ";

        }
    }


    @Test
    public void testSongExistByName(){
        assertTrue(catalogUtils.doesSongExistByName(songs,"Blinding Lights"));
        assertTrue(catalogUtils.doesSongExistByName(songs,"Sweet Heart"));
        assertTrue(catalogUtils.doesSongExistByName(songs,"Stay With Me"));
        assertFalse(catalogUtils.doesSongExistByName(songs,"Blinding"));
        assertFalse(catalogUtils.doesSongExistByName(songs,"KEEFO"));



    }


    @Test
    public void testcountSongsArtist(){
        assert catalogUtils.countSongsByArtist(songs,"The Weeknd")==1:"returned songscount by artist is uncorrect";
        assert catalogUtils.countSongsByArtist(songs,"Bruklin")==2:"returned songscount by artist is uncorrect";
        assert catalogUtils.countSongsByArtist(songs,"Mahmoud")==0:"returned songscount by artist is uncorrect";

    }


    @Test
    public void testLongestSong(){
        JsonNode retSong = catalogUtils.getLongestSong(songs);
        for(JsonNode song:songs){
            assert retSong.get("duration_ms").asLong() >= song.get("duration_ms").asLong();
        }
    }


    @Test
    public void testSongByYear(){
        List<JsonNode> retSongList = catalogUtils.getSongByYear(songs,2020);
        for(JsonNode song: songs){
            if (song.get("album").get("release_date").asText().substring(0, 4).equals(String.valueOf(2020))){
                assert retSongList.contains(song):"retSongList missing song released in the given year";
            }
        }
    }


    @Test
    public void testMostRecentSong() throws ParseException {
        JsonNode retSong = catalogUtils.getMostRecentSong(songs);
        assertEquals("2023-03-20",retSong.get("album").get("release_date").asText());
        /*
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        JsonNode retSong = catalogUtils.getMostRecentSong(songs);
        Date releaseDateOfRecentSong = simpleDateFormat.parse(retSong.get("album").get("release_date").asText());
        for (JsonNode song:songs){
            Date currSongReleaseDate = simpleDateFormat.parse(song.get("album").get("release_date").asText());
            assert (currSongReleaseDate.before(releaseDateOfRecentSong) || currSongReleaseDate.equals(releaseDateOfRecentSong)) : "song returned is not the mostrecent";
        }
        */
    }


}