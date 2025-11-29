// Register functionality for Fruta Shop
document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const nome = document.getElementById('nome').value;
    const email = document.getElementById('email').value;
    const telefone = document.getElementById('telefone').value;
    const senha = document.getElementById('senha').value;
    const confirmarSenha = document.getElementById('confirmarSenha').value;
    
    // Validate passwords match
    if (senha !== confirmarSenha) {
        showError('As senhas não coincidem!');
        return;
    }
    
    // Validate password length
    if (senha.length < 6) {
        showError('A senha deve ter no mínimo 6 caracteres!');
        return;
    }
    
    const cliente = {
        nome,
        email,
        telefone,
        senha
    };
    
    try {
        const response = await fetch('/api/clientes/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cliente)
        });
        
        const data = await response.json();
        
        if (data.success) {
            showSuccess('Conta criada com sucesso! Redirecionando para o login...');
            setTimeout(() => {
                window.location.href = '/login';
            }, 2000);
        } else {
            showError(data.message || 'Erro ao criar conta. Tente novamente.');
        }
    } catch (error) {
        console.error('Erro ao registrar:', error);
        showError('Erro ao conectar com o servidor. Tente novamente.');
    }
});

function showError(message) {
    const errorAlert = document.getElementById('errorAlert');
    const errorMessage = document.getElementById('errorMessage');
    errorMessage.textContent = message;
    errorAlert.style.display = 'block';
    
    // Hide success if showing
    document.getElementById('successAlert').style.display = 'none';
    
    setTimeout(() => {
        errorAlert.style.display = 'none';
    }, 5000);
}

function showSuccess(message) {
    const successAlert = document.getElementById('successAlert');
    const successMessage = document.getElementById('successMessage');
    successMessage.textContent = message;
    successAlert.style.display = 'block';
    
    // Hide error if showing
    document.getElementById('errorAlert').style.display = 'none';
}
