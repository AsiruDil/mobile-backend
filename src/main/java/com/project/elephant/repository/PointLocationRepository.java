package com.project.elephant.repository;

import com.project.elephant.models.PointLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointLocationRepository extends MongoRepository<PointLocation, String> {
}