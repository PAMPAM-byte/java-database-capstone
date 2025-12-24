package com.example.service;

import com.example.dto.Login;
import com.example.model.Admin;
import com.example.model.Patient;
import com.example.repository.AdminRepository;
import com.example.repository.DoctorRepository;
import com.example.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();
        
        if (!tokenService.validateToken(token, user)) {
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }
        
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();
        Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
        
        if (admin != null && admin.getPassword().equals(receivedAdmin.getPassword())) {
            String token = tokenService.generateToken(receivedAdmin.getUsername());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
    }

    public int validateAppointment(com.example.model.Appointment appointment) {
        // Validate appointment logic
        return 1;
    }

    public boolean validatePatient(Patient patient) {
        return patientService.validatePatient(patient);
    }

    public Map<String, String> validatePatientLogin(Login login) {
        return patientService.validatePatientLogin(login);
    }
}