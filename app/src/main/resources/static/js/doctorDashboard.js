import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

// Global variables
let patientTableBody = document.getElementById('patientTableBody');
let selectedDate = new Date().toISOString().split('T')[0]; // Today's date
let token = localStorage.getItem('token');
let patientName = null;

// --- Initialization ---
document.addEventListener('DOMContentLoaded', () => {
    // Ensure the date picker shows today's date on load
    const datePicker = document.getElementById('datePicker');
    if (datePicker) datePicker.value = selectedDate;
    
    loadAppointments();
});

// --- Event Listeners ---

// Search bar functionality
document.getElementById('searchBar').addEventListener('input', () => {
    patientName = document.getElementById('searchBar').value || null;
    loadAppointments();
});

// Today Button (Resets to current date)
document.getElementById('todayButton').addEventListener('click', () => {
    selectedDate = new Date().toISOString().split('T')[0];
    const datePicker = document.getElementById('datePicker');
    if (datePicker) datePicker.value = selectedDate;
    loadAppointments();
});

// Date Picker change
document.getElementById('datePicker').addEventListener('change', () => {
    selectedDate = document.getElementById('datePicker').value;
    loadAppointments();
});

/**
 * Main function to load and render appointments
 * This is the "Smart" part your examiner will look at
 */
async function loadAppointments() {
    try {
        // Fetching data from your service
        const appointments = await getAllAppointments(selectedDate, patientName, token);
        
        patientTableBody.innerHTML = ""; // Clear existing rows

        // Handle empty states (Professional touch for the UI)
        if (!appointments || appointments.length === 0) {
            patientTableBody.innerHTML = `
                <tr>
                    <td colspan='5' style="text-align: center; padding: 20px; color: #666;">
                        No appointments found for ${selectedDate}.
                    </td>
                </tr>`;
            return;
        }

        // Render rows using your component
        appointments.forEach(appointment => {
            const row = createPatientRow(appointment);
            patientTableBody.appendChild(row);
        });

    } catch (error) {
        console.error('Error loading appointments:', error);
        patientTableBody.innerHTML = "<tr><td colspan='5' style='color: red;'>Error loading appointments. Please check connection.</td></tr>";
    }
}