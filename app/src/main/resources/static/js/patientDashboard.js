import { createDoctorCard } from "./components/doctorCard.js";
import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors } from "./services/doctorServices.js";
import { patientLogin, patientSignup } from "./services/patientServices.js";

// Load doctor cards on page load
document.addEventListener("DOMContentLoaded", () => {
    loadDoctorCards();
});

async function loadDoctorCards() {
    const doctors = await getDoctors();
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";

    if (doctors.length > 0) {
        doctors.forEach(doctor => {
            const card = createDoctorCard(doctor);
            contentDiv.appendChild(card);
        });
    } else {
        contentDiv.innerHTML = "<p>No doctors available</p>";
    }
}

// Bind modal triggers for login and signup
document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById("patientSignup");
    if (btn) btn.addEventListener("click", () => openModal("patientSignup"));

    const loginBtn = document.getElementById("patientLogin");
    if (loginBtn) loginBtn.addEventListener("click", () => openModal("patientLogin"));
});

// Search and Filter Logic
document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);

async function filterDoctorsOnChange() {
    const name = document.getElementById("searchBar").value;
    const time = document.getElementById("filterTime").value;
    const specialty = document.getElementById("filterSpecialty").value;

    const doctors = await filterDoctors(name, time, specialty);
    
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";

    if (doctors.length > 0) {
        doctors.forEach(doctor => {
            const card = createDoctorCard(doctor);
            contentDiv.appendChild(card);
        });
    } else {
        contentDiv.innerHTML = "<p>No doctors found</p>";
    }
}

// Handle Patient Signup
window.signupPatient = async function() {
    try {
        const name = document.getElementById('patientName').value;
        const email = document.getElementById('patientEmail').value;
        const password = document.getElementById('patientPassword').value;
        const phone = document.getElementById('patientPhone').value;
        const address = document.getElementById('patientAddress').value;

        const data = { name, email, password, phone, address };
        const result = await patientSignup(data);

        if (result.success) {
            alert('Signup successful!');
            closeModal('patientSignup');
            location.reload();
        } else {
            alert(result.message);
        }
    } catch (error) {
        console.error('Signup error:', error);
        alert('An error occurred');
    }
};

// Handle Patient Login
window.loginPatient = async function() {
    try {
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;

        const data = { email, password };
        const result = await patientLogin(data);

        if (result.success) {
            alert('Login successful!');
            localStorage.setItem('token', result.token);
            window.location.href = 'loggedPatientDashboard.html';
        } else {
            alert('Invalid credentials');
        }
    } catch (error) {
        console.error('Login error:', error);
        alert('An error occurred');
    }
};