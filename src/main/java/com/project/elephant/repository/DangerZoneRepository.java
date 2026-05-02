package com.project.elephant.repository;

import com.project.elephant.models.DangerZone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DangerZoneRepository extends MongoRepository<DangerZone, String> {
}