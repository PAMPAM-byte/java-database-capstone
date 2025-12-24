package com.example.service;

import com.example.model.Doctor;
import com.example.repository.DoctorRepository;
import com.example.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    public List<String> getDoctorAvailability(Long doctorId, LocalDateTime date) {
        // Fetch appointments for the doctor on the specified date
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        var appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
            doctorId, startOfDay, endOfDay
        );
        
        // Filter out booked slots and return available ones
        List<String> availableSlots = new ArrayList<>();
        // Implementation logic here
        return availableSlots;
    }

    public int saveDoctor(Doctor doctor) {
        try {
            Doctor existing = doctorRepository.findByEmail(doctor.getEmail());
            if (existing != null) {
                return -1; // Doctor already exists
            }
            doctorRepository.save(doctor);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Error
        }
    }

    public int updateDoctor(Doctor doctor) {
        try {
            Doctor existing = doctorRepository.findById(doctor.getId()).orElse(null);
            if (existing == null) {
                return -1; // Not found
            }
            doctorRepository.save(doctor);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Error
        }
    }

    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    public int deleteDoctor(Long id) {
        try {
            Doctor existing = doctorRepository.findById(id).orElse(null);
            if (existing == null) {
                return -1; // Not found
            }
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Error
        }
    }

    public Map<String, String> validateDoctor(String email, String password) {
        Map<String, String> response = new HashMap<>();
        Doctor doctor = doctorRepository.findByEmail(email);
        
        if (doctor != null && doctor.getPassword().equals(password)) {
            String token = tokenService.generateToken(email);
            response.put("token", token);
            response.put("message", "Login successful");
        } else {
            response.put("message", "Invalid credentials");
        }
        return response;
    }

    public List<Doctor> findDoctorsByName(String name) {
        return doctorRepository.findByNameLike(name);
    }

    public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        // Complex filtering logic
        Map<String, Object> response = new HashMap<>();
        return response;
    }
}