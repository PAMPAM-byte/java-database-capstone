package com.example.repository;

import com.example.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    @Query(value = "SELECT * FROM appointment WHERE doctor_id = :doctorId " +
                   "AND appointment_time BETWEEN :start AND :end " +
                   "ORDER BY appointment_time", nativeQuery = true)
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
        @Param("doctorId") Long doctorId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
    
    @Query(value = "SELECT * FROM appointment WHERE doctor_id = :doctorId " +
                   "AND patient_id = :patientId AND LOWER(patient_name) LIKE LOWER(CONCAT('%', :patientName, '%')) " +
                   "AND appointment_time BETWEEN :start AND :end " +
                   "ORDER BY appointment_time", nativeQuery = true)
    List<Appointment> findByDoctorIdAndPatientNameContainingIgnoreCaseAndAppointmentTimeBetween(
        @Param("doctorId") Long doctorId,
        @Param("patientId") Long patientId,
        @Param("patientName") String patientName,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM appointment WHERE doctor_id = :doctorId", nativeQuery = true)
    void deleteAllByDoctorId(@Param("doctorId") Long doctorId);
    
    List<Appointment> findByPatientId(Long patientId);
    
    @Query(value = "SELECT * FROM appointment WHERE patient_id = :patientId " +
                   "AND status = :status ORDER BY appointment_time DESC", nativeQuery = true)
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(
        @Param("patientId") Long patientId,
        @Param("status") int status
    );
    
    @Query(value = "SELECT * FROM appointment WHERE LOWER(doctor_name) LIKE LOWER(CONCAT('%', :doctorName, '%')) " +
                   "AND patient_id = :patientId ORDER BY appointment_time", nativeQuery = true)
    List<Appointment> filterByDoctorNameAndPatientId(
        @Param("doctorName") String doctorName,
        @Param("patientId") Long patientId
    );
    
    @Query(value = "SELECT * FROM appointment WHERE LOWER(doctor_name) LIKE LOWER(CONCAT('%', :doctorName, '%')) " +
                   "AND patient_id = :patientId AND status = :status " +
                   "ORDER BY appointment_time", nativeQuery = true)
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(
        @Param("doctorName") String doctorName,
        @Param("patientId") Long patientId,
        @Param("status") int status
    );
}