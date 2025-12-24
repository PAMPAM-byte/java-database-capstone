package com.example.controller;

import com.example.dto.Login;
import com.example.model.Patient;
import com.example.service.PatientService;
import com.example.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private Service service;

    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatientDetails(@PathVariable String token) {
        
        // Validate token
        var validation = service.validateToken(token, "patient");
        if (validation.getStatusCodeValue() != 200) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        return patientService.getPatientDetails(token);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        Map<String, String> response = new HashMap<>();
        
        // Validate patient
        if (!service.validatePatient(patient)) {
            response.put("message", "Patient with email id or phone no already exist");
            return ResponseEntity.status(409).body(response);
        }
        
        // Create patient
        int result = patientService.createPatient(patient);
        if (result == 1) {
            response.put("message", "Signup successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Internal server error");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> patientLogin(@RequestBody Login login) {
        return ResponseEntity.ok(service.validatePatientLogin(login));
    }

    @GetMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> getPatientAppointments(
            @PathVariable Long id,
            @PathVariable String token) {
        
        // Validate token
        var validation = service.validateToken(token, "patient");
        if (validation.getStatusCodeValue() != 200) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        return patientService.getPatientAppointment(id, token);
    }

    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointments(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Validate token
        var validation = service.validateToken(token, "patient");
        if (validation.getStatusCodeValue() != 200) {
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        return patientService.filterByCondition(condition, null);
    }
}