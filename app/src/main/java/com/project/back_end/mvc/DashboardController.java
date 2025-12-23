package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import com.example.service.TokenService;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/adminDashboard/{token}")
    public Object adminDashboard(@PathVariable String token) {
        Map<String, Object> result = tokenService.validateToken(token, "admin");
        
        if (result.isEmpty()) {
            return "admin/adminDashboard";
        } else {
            return new RedirectView("http://localhost:8080");
        }
    }

    @GetMapping("/doctorDashboard/{token}")
    public Object doctorDashboard(@PathVariable String token) {
        Map<String, Object> result = tokenService.validateToken(token, "doctor");
        
        if (result.isEmpty()) {
            return "doctor/doctorDashboard";
        } else {
            return new RedirectView("http://localhost:8080");
        }
    }
}