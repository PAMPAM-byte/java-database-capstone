package com.example.repository;

import com.example.model.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    
    List<Prescription> findByAppointmentId(Long appointmentId);
}