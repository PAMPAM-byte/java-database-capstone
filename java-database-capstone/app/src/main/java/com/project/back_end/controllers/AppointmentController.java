package com.example.controller;

import com.example.model.Appointment;
import com.example.service.AppointmentService;
import com.example.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private Service service;

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
            @PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token) {
        
        // Validate token
        var validation = service.validateToken(token, "doctor");
        if (validation.getStatusCodeValue() != 200) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        // Get appointments
        return ResponseEntity.ok(appointmentService.getAppointment(patientName, null, token).getBody());
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(
            @PathVariable String token,
            @RequestBody Appointment appointment) {
        
        Map<String, String> response = new HashMap<>();
        
        // Validate token
        var validation = service.validateToken(token, "patient");
        if (validation.getStatusCodeValue() != 200) {
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        // Validate appointment
        if (service.validateAppointment(appointment) == 0) {
            response.put("message", "Invalid appointment");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Book appointment
        int result = appointmentService.bookAppointment(appointment);
        if (result == 1) {
            response.put("message", "Appointment booked successfully");
            return ResponseEntity.status(201).body(response);
        } else {
            response.put("message", "Failed to book appointment");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, Object>> updateAppointment(
            @PathVariable String token,
            @RequestBody Appointment appointment) {
        
        // Validate token
        var validation = service.validateToken(token, "patient");
        if (validation.getStatusCodeValue() != 200) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> cancelAppointment(
            @PathVariable Long id,
            @PathVariable String token) {
        
        // Validate token
        var validation = service.validateToken(token, "patient");
        if (validation.getStatusCodeValue() != 200) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        return appointmentService.cancelAppointment(id, token);
    }
}