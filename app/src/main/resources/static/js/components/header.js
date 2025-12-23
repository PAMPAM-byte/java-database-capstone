function renderHeader() {
    // Check if on homepage - don't show header on homepage
    if (window.location.pathname.endsWith("/")) {
        localStorage.removeItem("userRole");
        localStorage.removeItem("token");
        return;
    }

    // Get user role and token from localStorage
    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    // Check for invalid session
    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
        return;
    }

    // Build header content based on role
    let headerContent = "";

    if (role === "admin") {
        headerContent = `
            <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
            <a href="#" onclick="logout()">Logout</a>
        `;
    } else if (role === "doctor") {
        headerContent = `
            <a href="doctorDashboard.html">Home</a>
            <a href="#" onclick="logout()">Logout</a>
        `;
    } else if (role === "patient") {
        headerContent = `
            <a href="#">Login</a>
            <a href="#">Sign Up</a>
        `;
    } else if (role === "loggedPatient") {
        headerContent = `
            <a href="patientDashboard.html">Home</a>
            <a href="#">Appointments</a>
            <a href="#" onclick="logout()">Logout</a>
        `;
    }

    // Inject the header into the page
    const headerDiv = document.getElementById("header");
    if (headerDiv) {
        headerDiv.innerHTML = headerContent;
        attachHeaderButtonListeners();
    }
}

function attachHeaderButtonListeners() {
    // After rendering header, attach event listeners to buttons
    const addDocBtn = document.getElementById("addDocBtn");
    if (addDocBtn) {
        // Check if listener already exists to avoid duplicates
        if (!addDocBtn.dataset.listenerAttached) {
            addDocBtn.addEventListener("click", () => {
                // Logic to open add doctor modal
                const modal = document.getElementById("modal");
                if (modal) {
                    modal.style.display = "flex";
                }
            });
            addDocBtn.dataset.listenerAttached = "true";
        }
    }
}

function logout() {
    // Create logout function
    localStorage.removeItem("token");
    localStorage.removeItem("userRole");
    window.location.href = "/";
}

function logoutPatient() {
    // For patient logout - remove token but keep role as "patient"
    localStorage.removeItem("token");
    localStorage.setItem("userRole", "patient");
    window.location.href = "patientDashboard.html";
}