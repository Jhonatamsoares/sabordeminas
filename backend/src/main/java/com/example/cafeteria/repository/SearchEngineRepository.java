package com.example.cafeteria.repository;

import com.example.cafeteria.model.SearchEngine;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SearchEngineRepository extends MongoRepository<SearchEngine, String> {

}