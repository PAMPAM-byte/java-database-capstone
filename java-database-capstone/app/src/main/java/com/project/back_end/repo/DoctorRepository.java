package com.example.repository;

import com.example.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    Doctor findByEmail(String email);
    
    @Query(value = "SELECT * FROM doctor WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))", nativeQuery = true)
    List<Doctor> findByNameLike(@Param("name") String name);
    
    @Query(value = "SELECT * FROM doctor WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%')) " +
                   "AND LOWER(specialty) LIKE LOWER(CONCAT('%', :specialty, '%'))", nativeQuery = true)
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
        @Param("name") String name,
        @Param("specialty") String specialty
    );
    
    @Query(value = "SELECT * FROM doctor WHERE LOWER(specialty) = LOWER(:specialty)", nativeQuery = true)
    List<Doctor> findBySpecialtyIgnoreCase(@Param("specialty") String specialty);
}