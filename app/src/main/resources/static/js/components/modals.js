// Open Modal Function
function openModal(modalType) {
    const modal = document.getElementById("modal");
    const modalBody = document.getElementById("modal-body");

    if (modalType === "addDoctor") {
        modalBody.innerHTML = `
            <h3>Add New Doctor</h3>
            <input type="text" id="doctorName" placeholder="Doctor Name" required />
            <input type="text" id="doctorSpecialty" placeholder="Specialty" required />
            <input type="email" id="doctorEmail" placeholder="Email" required />
            <input type="text" id="doctorAvailability" placeholder="Availability (comma-separated)" required />
            <button onclick="addDoctor()">Add Doctor</button>
        `;
    }

    modal.style.display = "flex";
}

// Close Modal Function
function closeModal() {
    const modal = document.getElementById("modal");
    modal.style.display = "none";
}

// Add Doctor Function (Placeholder)
function addDoctor() {
    const name = document.getElementById("doctorName").value;
    const specialty = document.getElementById("doctorSpecialty").value;
    const email = document.getElementById("doctorEmail").value;
    const availability = document.getElementById("doctorAvailability").value.split(",").map(a => a.trim());

    if (!name || !specialty || !email || !availability) {
        alert("Please fill in all fields.");
        return;
    }

    console.log("Adding doctor:", { name, specialty, email, availability });

    // This will call API in next lab
    alert(`Doctor ${name} added successfully!`);
    closeModal();
}