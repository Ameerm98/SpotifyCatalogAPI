package com.example.catalog;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CatalogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetPopularSongs() throws Exception {
        int offset = 0;
        int limit = 2;
        mockMvc.perform(MockMvcRequestBuilders.get("/popularSongs"+"?offset="+offset+"&limit="+limit))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Blinding Lights"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Shape of You"));
    }
/*
    @Test
    public void testGetPopularArtists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/popularArtists"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$",hasSize(90)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1Xyo4u8uXC1ZmMpatF05PJ"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[89].name").value("12345UvClmAT7URs9V3rsp"));
    }

 */
}

