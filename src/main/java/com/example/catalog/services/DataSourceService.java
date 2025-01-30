package com.example.catalog.services;

import com.example.catalog.model.*;

import java.io.IOException;
// src/main/java/com/example/catalog/services/DataSourceService.java

public interface DataSourceService {
    Artist getArtistById(String id) throws IOException;



}
