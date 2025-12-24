package com.example.service;

import com.example.model.Appointment;
import com.example.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public int bookAppointment(Appointment appointment) {
        try {
            Appointment existing = appointmentRepository.findById(appointment.getId()).orElse(null);
            if (existing != null) {
                return 0; // Appointment already exists
            }
            appointmentRepository.save(appointment);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Failure
        }
    }

    public ResponseEntity<Map<String, Object>> updateAppointment(Appointment appointment) {
        Map<String, Object> response = new HashMap<>();
        try {
            Appointment existing = appointmentRepository.findById(appointment.getId()).orElse(null);
            if (existing == null) {
                response.put("message", "Appointment not found");
                return ResponseEntity.status(404).body(response);
            }
            
            // Validate appointment
            if (!validateAppointment(appointment)) {
                response.put("message", "Invalid appointment time");
                return ResponseEntity.badRequest().body(response);
            }
            
            appointmentRepository.save(appointment);
            response.put("message", "Appointment updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error updating appointment");
            return ResponseEntity.status(500).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> cancelAppointment(Long id, String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            Appointment appointment = appointmentRepository.findById(id).orElse(null);
            if (appointment == null) {
                response.put("message", "Appointment not found");
                return ResponseEntity.status(404).body(response);
            }
            
            appointmentRepository.delete(appointment);
            response.put("message", "Appointment cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error cancelling appointment");
            return ResponseEntity.status(500).body(response);
        }
    }

    public Map<String, Object> getAppointment(String pname, LocalDateTime date, String token) {
        Map<String, Object> response = new HashMap<>();
        // Implementation for getting appointments
        return response;
    }

    public boolean validateAppointment(Appointment appointment) {
        // Check if appointment time is valid (doctor is available)
        return true; // Simplified - implement full logic as needed
    }
}