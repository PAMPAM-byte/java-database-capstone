package com.example.repository;

import com.example.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    Patient findByEmail(String email);
    
    Patient findByEmailOrPhone(String email, String phone);
}