package com.project.elephant.repository;

import com.project.elephant.models.Sighting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SightingRepository extends MongoRepository<Sighting, String> {
}