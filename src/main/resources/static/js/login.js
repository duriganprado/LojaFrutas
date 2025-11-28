// Login functionality
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('loginForm');
    const csrfInput = document.querySelector('input[name="_csrf"]');
    
    console.log('Login page loaded');
    console.log('CSRF input exists:', !!csrfInput);
    console.log('CSRF token value:', csrfInput ? csrfInput.value : 'NOT FOUND');
    
    // Check if CSRF token exists
    if (!csrfInput || !csrfInput.value || csrfInput.value === '') {
        console.error('CSRF token is missing! Page needs to be loaded through Thymeleaf controller.');
        showError('Erro de segurança. Recarregando página...');
        setTimeout(() => {
            window.location.href = '/login';
        }, 1000);
    }
    
    form.addEventListener('submit', function(e) {
        const csrfToken = document.querySelector('input[name="_csrf"]')?.value;
        console.log('Form submitting with CSRF token:', csrfToken);
        // Let form submit naturally
    });
});

function showError(message) {
    const errorAlert = document.getElementById('errorAlert');
    const errorMessage = document.getElementById('errorMessage');
    errorMessage.textContent = message;
    errorAlert.classList.remove('hidden');
    
    setTimeout(() => {
        errorAlert.classList.add('hidden');
    }, 5000);
}

// Check for error/logout params
const urlParams = new URLSearchParams(window.location.search);
if (urlParams.get('error')) {
    showError('Email ou senha inválidos!');
}
if (urlParams.get('logout')) {
    showError('Você saiu com sucesso!');
    const errorAlert = document.getElementById('errorAlert');
    errorAlert.className = 'bg-green-500 bg-opacity-20 border border-green-500 text-white px-4 py-3 rounded-lg';
}
