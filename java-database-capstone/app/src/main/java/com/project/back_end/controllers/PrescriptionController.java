package com.example.controller;

import com.example.model.Prescription;
import com.example.service.PrescriptionService;
import com.example.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private Service service;

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(
            @PathVariable String token,
            @RequestBody Prescription prescription) {
        
        Map<String, String> response = new HashMap<>();
        
        // Validate token
        var validation = service.validateToken(token, "doctor");
        if (validation.getStatusCodeValue() != 200) {
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        return prescriptionService.savePrescription(prescription);
    }

    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Validate token
        var validation = service.validateToken(token, "doctor");
        if (validation.getStatusCodeValue() != 200) {
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        return prescriptionService.getPrescription(appointmentId);
    }
}