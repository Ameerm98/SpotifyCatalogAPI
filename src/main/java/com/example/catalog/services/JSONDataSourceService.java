package com.example.catalog.services;
// src/main/java/com/example/catalog/services/JSONDataSourceService.java

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.example.catalog.model.*;

import java.io.IOException;

@Service
public class JSONDataSourceService implements DataSourceService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Artist getArtistById(String id) throws IOException {
        JsonNode artists = loadJsonData("data/popular_artists.json");
        JsonNode artistNode = artists.get(id);
        if (artistNode == null) {
            return null;
        }
        return objectMapper.treeToValue(artistNode, Artist.class);
    }

    private JsonNode loadJsonData(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return objectMapper.readTree(resource.getFile());
    }
}
