import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";

// Define API endpoints
const ADMIN_API = API_BASE_URL + '/admin';
const DOCTOR_API = API_BASE_URL + '/doctor/login';

// Setup button event listeners when page loads
window.onload = function () {
    const adminBtn = document.getElementById('adminLogin');
    if (adminBtn) {
        adminBtn.addEventListener('click', () => {
            openModal('adminLogin');
        });
    }

    const doctorBtn = document.getElementById('doctorLogin');
    if (doctorBtn) {
        doctorBtn.addEventListener('click', () => {
            openModal('doctorLogin');
        });
    }
};

// Admin Login Handler
window.adminLoginHandler = async function () {
    try {
        const username = document.getElementById('adminUsername').value;
        const password = document.getElementById('adminPassword').value;
        
        const admin = { username, password };

        const response = await fetch(ADMIN_API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(admin)
        });

        const data = await response.json();

        if (response.ok) {
            // Extract token and store in localStorage
            localStorage.setItem('token', data.token);
            // Save role
            selectRole('admin');
            alert('Login successful!');
            window.location.href = 'adminDashboard.html';
        } else {
            alert('Invalid credentials!');
        }
    } catch (error) {
        console.error('Login error:', error);
        alert('An unexpected error occurred. Please try again.');
    }
};

// Doctor Login Handler
window.doctorLoginHandler = async function () {
    try {
        const email = document.getElementById('doctorEmail').value;
        const password = document.getElementById('doctorPassword').value;
        
        const doctor = { email, password };

        const response = await fetch(DOCTOR_API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(doctor)
        });

        const data = await response.json();

        if (response.ok) {
            localStorage.setItem('token', data.token);
            selectRole('doctor');
            alert('Login successful!');
            window.location.href = 'doctorDashboard.html';
        } else {
            alert('Invalid credentials!');
        }
    } catch (error) {
        console.error('Login error:', error);
        alert('An unexpected error occurred. Please try again.');
    }
};

// Helper function to save selected role
function selectRole(role) {
    localStorage.setItem('role', role);
}