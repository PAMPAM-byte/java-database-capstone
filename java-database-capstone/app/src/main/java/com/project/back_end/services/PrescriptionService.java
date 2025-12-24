package com.example.service;

import com.example.model.Prescription;
import com.example.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
        Map<String, String> response = new HashMap<>();
        try {
            prescriptionRepository.save(prescription);
            response.put("message", "Prescription saved");
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            response.put("message", "Internal Server Error");
            return ResponseEntity.status(500).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            var prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);
            if (!prescriptions.isEmpty()) {
                response.put("prescription", prescriptions.get(0));
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "No prescription found");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("message", "Internal Server Error");
            return ResponseEntity.status(500).body(response);
        }
    }
}