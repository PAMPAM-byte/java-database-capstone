import { openModal, closeModal } from "./components/modals.js";
import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";

/**
 * --- Initialization ---
 */

// Load doctor cards when the page structure is ready
document.addEventListener('DOMContentLoaded', loadDoctorCards);

// Event binding for opening the "Add Doctor" modal
const addDocBtn = document.getElementById("addDocBtn");
if (addDocBtn) {
    addDocBtn.addEventListener("click", () => openModal('addDoctor'));
}

/**
 * --- Core Logic: Fetching & Rendering ---
 */

async function loadDoctorCards() {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
}

function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");
    if (!contentDiv) return;

    contentDiv.innerHTML = ""; // Clear existing content

    if (doctors && doctors.length > 0) {
        doctors.forEach(doctor => {
            const card = createDoctorCard(doctor);
            contentDiv.appendChild(card);
        });
    } else {
        contentDiv.innerHTML = `<div class="no-data"><p>No doctors found.</p></div>`;
    }
}

/**
 * --- Search and Filter Logic ---
 * Listens for user input to filter the doctor list dynamically
 */

const searchBar = document.getElementById("searchBar");
const filterTime = document.getElementById("filterTime");
const filterSpecialty = document.getElementById("filterSpecialty");

[searchBar, filterTime, filterSpecialty].forEach(element => {
    if (element) {
        element.addEventListener("input", filterDoctorsOnChange);
        element.addEventListener("change", filterDoctorsOnChange);
    }
});

async function filterDoctorsOnChange() {
    const name = searchBar?.value || "";
    const time = filterTime?.value || "";
    const specialty = filterSpecialty?.value || "";

    const doctors = await filterDoctors(name, time, specialty);
    renderDoctorCards(doctors);
}

/**
 * --- Admin Actions: Add Doctor ---
 * Attached to window so the modal's "Save" button can trigger it
 */

window.adminAddDoctor = async function() {
    try {
        // Gather Form Data
        const doctor = {
            name: document.getElementById("doctorName").value,
            specialty: document.getElementById("doctorSpecialty").value,
            email: document.getElementById("doctorEmail").value,
            password: document.getElementById("doctorPassword").value,
            mobile: document.getElementById("doctorMobile").value,
            availability: Array.from(document.querySelectorAll('input[name="availability"]:checked'))
                .map(cb => cb.value)
        };

        // Basic validation
        if (!doctor.name || !doctor.email) {
            alert("Name and Email are required.");
            return;
        }

        // Authentication Check
        const token = localStorage.getItem('token');
        if (!token) {
            alert('Security Error: Please login as admin.');
            return;
        }

        const result = await saveDoctor(doctor, token);

        if (result.success) {
            alert(result.message || 'Doctor added successfully!');
            closeModal('addDoctor');
            // Optimization: instead of reload, just re-fetch cards
            loadDoctorCards(); 
        } else {
            alert(result.message || 'Failed to add doctor');
        }
    } catch (error) {
        console.error('Error adding doctor:', error);
        alert('An error occurred while adding the doctor');
    }
};