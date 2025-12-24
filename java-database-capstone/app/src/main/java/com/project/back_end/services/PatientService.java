package com.example.service;

import com.example.dto.Login;
import com.example.model.Patient;
import com.example.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TokenService tokenService;

    public int createPatient(Patient patient) {
        try {
            Patient existing = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
            if (existing != null) {
                return 0; // Patient already exists
            }
            patientRepository.save(patient);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Failure
        }
    }

    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        Map<String, Object> response = new HashMap<>();
        // Implementation
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        Map<String, Object> response = new HashMap<>();
        // Implementation
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientid) {
        Map<String, Object> response = new HashMap<>();
        // Implementation
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition, String name, long patientid) {
        Map<String, Object> response = new HashMap<>();
        // Implementation
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        Map<String, Object> response = new HashMap<>();
        String email = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(email);
        
        if (patient != null) {
            response.put("patient", patient);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Patient not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    public boolean validatePatient(Patient patient) {
        Patient existing = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
        return existing == null;
    }

    public Map<String, String> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();
        Patient patient = patientRepository.findByEmail(login.getIdentifier());
        
        if (patient != null && patient.getPassword().equals(login.getPassword())) {
            String token = tokenService.generateToken(login.getIdentifier());
            response.put("token", token);
            response.put("message", "Login successful");
        } else {
            response.put("message", "Invalid credentials");
        }
        return response;
    }
}