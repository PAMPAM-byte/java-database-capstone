package com.example.controller;

import com.example.dto.Login;
import com.example.model.Doctor;
import com.example.service.DoctorService;
import com.example.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private Service service;

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Validate token
        var validation = service.validateToken(token, user);
        if (validation.getStatusCodeValue() != 200) {
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        // Get availability
        List<String> availability = doctorService.getDoctorAvailability(doctorId, LocalDateTime.parse(date));
        response.put("availability", availability);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorService.getDoctors();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> addDoctor(
            @PathVariable String token,
            @RequestBody Doctor doctor) {
        
        Map<String, String> response = new HashMap<>();
        
        // Validate token
        var validation = service.validateToken(token, "admin");
        if (validation.getStatusCodeValue() != 200) {
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        // Save doctor
        int result = doctorService.saveDoctor(doctor);
        if (result == 1) {
            response.put("message", "Doctor added to db");
            return ResponseEntity.status(201).body(response);
        } else if (result == -1) {
            response.put("message", "Doctor already exists");
            return ResponseEntity.status(409).body(response);
        } else {
            response.put("message", "Some internal error occurred");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return ResponseEntity.ok(doctorService.validateDoctor(login.getIdentifier(), login.getPassword()));
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @PathVariable String token,
            @RequestBody Doctor doctor) {
        
        Map<String, String> response = new HashMap<>();
        
        // Validate token
        var validation = service.validateToken(token, "admin");
        if (validation.getStatusCodeValue() != 200) {
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        // Update doctor
        int result = doctorService.updateDoctor(doctor);
        if (result == 1) {
            response.put("message", "Doctor updated");
            return ResponseEntity.ok(response);
        } else if (result == -1) {
            response.put("message", "Doctor not found");
            return ResponseEntity.status(404).body(response);
        } else {
            response.put("message", "Some internal error occurred");
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable Long id,
            @PathVariable String token) {
        
        Map<String, String> response = new HashMap<>();
        
        // Validate token
        var validation = service.validateToken(token, "admin");
        if (validation.getStatusCodeValue() != 200) {
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        // Delete doctor
        int result = doctorService.deleteDoctor(id);
        if (result == 1) {
            response.put("message", "Doctor deleted successfully");
            return ResponseEntity.ok(response);
        } else if (result == -1) {
            response.put("message", "Doctor not found with id");
            return ResponseEntity.status(404).body(response);
        } else {
            response.put("message", "Some internal error occurred");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filterDoctors(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {
        
        Map<String, Object> result = service.filterDoctor(name, speciality, time);
        return ResponseEntity.ok(result);
    }
}