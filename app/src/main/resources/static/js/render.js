// Render utility for dynamically creating DOM elements

function render() {
    renderHeader();
    renderFooter();
}

// Call render when DOM is loaded
document.addEventListener('DOMContentLoaded', render);