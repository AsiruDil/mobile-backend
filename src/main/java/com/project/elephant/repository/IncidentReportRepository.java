package com.project.elephant.repository;

import com.project.elephant.models.IncidentReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentReportRepository extends MongoRepository<IncidentReport, String> {
    // Basic CRUD operations like save() are inherited automatically
}
