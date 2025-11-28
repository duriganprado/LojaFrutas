let currentUser = null;
let allAnimals = [];
let allFormularios = [];
let currentAnimalImage = null;

// Initialize
document.addEventListener('DOMContentLoaded', async () => {
    await checkAuth();
    initializeTabs();
    initializeFilters();
    initializeImagePreview();
});

// Check authentication
async function checkAuth() {
    try {
        console.log('Checking authentication...');
        const response = await fetch('/api/usuarios/me');
        console.log('Auth response status:', response.status);
        
        if (!response.ok) {
            console.error('Auth failed, redirecting to login');
            window.location.href = '/login';
            return;
        }

        currentUser = await response.json();
        console.log('Current user:', currentUser);
        document.getElementById('userNameDisplay').textContent = currentUser.nome;
        
        await loadAnimals();
        await loadFormularios();
    } catch (error) {
        console.error('Auth error:', error);
        window.location.href = '/login';
    }
}

// Logout
async function logout() {
    try {
        const csrfToken = document.querySelector('meta[name="_csrf"]')?.content || 
                         document.querySelector('input[name="_csrf"]')?.value;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || '_csrf';
        
        const response = await fetch('/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                [csrfHeader]: csrfToken
            }
        });
        
        window.location.href = '/login?logout=true';
    } catch (error) {
        console.error('Logout error:', error);
        window.location.href = '/login';
    }
}

// Tabs functionality
function initializeTabs() {
    const tabButtons = document.querySelectorAll('.tab-btn');
    
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            tabButtons.forEach(btn => btn.classList.remove('active'));
            document.querySelectorAll('.tab-pane').forEach(pane => pane.classList.add('hidden'));
            
            button.classList.add('active');
            const tabName = button.getAttribute('data-tab');
            document.getElementById(`${tabName}-tab`).classList.remove('hidden');
            
            if (tabName === 'adocao') loadAnimaisDisponiveis();
            else if (tabName === 'formularios') loadFormularios();
        });
    });
}

// Initialize filters
function initializeFilters() {
    document.getElementById('searchAnimal').addEventListener('input', filterAnimals);
    document.getElementById('filterEspecie').addEventListener('change', filterAnimals);
}

// Filter animals
function filterAnimals() {
    const search = document.getElementById('searchAnimal').value.toLowerCase();
    const especie = document.getElementById('filterEspecie').value;
    
    const filtered = allAnimals.filter(animal => {
        const matchSearch = animal.nome.toLowerCase().includes(search);
        const matchEspecie = !especie || animal.especie === especie;
        return matchSearch && matchEspecie;
    });
    
    displayAnimals(filtered);
}

// Load all animals
async function loadAnimals() {
    try {
        const response = await fetch('/api/animals');
        if (response.ok) {
            allAnimals = await response.json();
            displayAnimals(allAnimals);
        }
    } catch (error) {
        console.error('Erro ao carregar animais:', error);
    }
}

// Display animals
function displayAnimals(animals) {
    const grid = document.getElementById('animalsGrid');
    
    if (animals.length === 0) {
        grid.innerHTML = `
            <div class="col-span-full text-center py-12 text-white">
                <i class="fas fa-paw text-6xl mb-4 opacity-50"></i>
                <p class="text-xl">Nenhum animal encontrado</p>
            </div>
        `;
        return;
    }
    
    grid.innerHTML = animals.map(animal => `
        <div class="glass-card rounded-xl overflow-hidden shadow-lg hover:shadow-2xl transition duration-300">
            <div class="h-48 bg-gradient-to-br from-indigo-100 to-purple-100 flex items-center justify-center overflow-hidden">
                ${animal.fotoUrl 
                    ? `<img src="${animal.fotoUrl}" alt="${animal.nome}" class="w-full h-full object-cover">` 
                    : `<i class="fas fa-${getAnimalIconClass(animal.especie)} text-6xl text-indigo-300"></i>`
                }
            </div>
            <div class="p-4 bg-white bg-opacity-60">
                <h3 class="text-xl font-bold text-gray-900 mb-2">${animal.nome}</h3>
                <div class="text-sm text-gray-800 space-y-1 mb-3">
                    <p><i class="fas fa-paw mr-2 text-indigo-600"></i><strong>Espécie:</strong> ${animal.especie}</p>
                    ${animal.raca ? `<p><i class="fas fa-dna mr-2 text-purple-600"></i><strong>Raça:</strong> ${animal.raca}</p>` : ''}
                    <p><i class="fas fa-birthday-cake mr-2 text-pink-600"></i><strong>Idade:</strong> ${animal.idade} ${animal.idade === 1 ? 'ano' : 'anos'}</p>
                    ${animal.sexo ? `<p><i class="fas fa-venus-mars mr-2 text-blue-600"></i><strong>Sexo:</strong> ${animal.sexo}</p>` : ''}
                </div>
                ${animal.descricao ? `<p class="text-sm text-gray-700 mb-3 line-clamp-2">${animal.descricao}</p>` : ''}
                <div class="flex flex-wrap gap-2 mb-4">
                    ${animal.vacinado ? '<span class="px-2 py-1 bg-green-100 text-green-700 text-xs rounded-full"><i class="fas fa-syringe mr-1"></i>Vacinado</span>' : ''}
                    ${animal.castrado ? '<span class="px-2 py-1 bg-blue-100 text-blue-700 text-xs rounded-full"><i class="fas fa-cut mr-1"></i>Castrado</span>' : ''}
                    ${animal.disponivelAdocao 
                        ? '<span class="px-2 py-1 bg-purple-100 text-purple-800 text-xs rounded-full font-semibold"><i class="fas fa-check mr-1"></i>Disponível</span>' 
                        : '<span class="px-2 py-1 bg-gray-200 text-gray-800 text-xs rounded-full font-semibold"><i class="fas fa-times mr-1"></i>Adotado</span>'}
                </div>
                <div class="flex gap-2">
                    <button onclick="editAnimal(${animal.id})" class="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white py-2 rounded-lg text-sm transition">
                        <i class="fas fa-edit mr-1"></i>Editar
                    </button>
                    <button onclick="deleteAnimal(${animal.id})" class="flex-1 bg-red-600 hover:bg-red-700 text-white py-2 rounded-lg text-sm transition">
                        <i class="fas fa-trash mr-1"></i>Excluir
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

// Get animal icon class
function getAnimalIconClass(especie) {
    const icons = {
        'Cachorro': 'dog',
        'Gato': 'cat',
        'Pássaro': 'dove',
        'Coelho': 'rabbit',
        'Outro': 'paw'
    };
    return icons[especie] || 'paw';
}

// Initialize image preview
function initializeImagePreview() {
    const input = document.getElementById('animalFoto');
    input.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            currentAnimalImage = file;
            const reader = new FileReader();
            reader.onload = (event) => {
                document.getElementById('imagePreview').innerHTML = 
                    `<img src="${event.target.result}" class="w-full h-full object-cover">`;
            };
            reader.readAsDataURL(file);
        }
    });
}

// Animal Modal
function openAnimalModal(animalId = null) {
    const modal = document.getElementById('animalModal');
    const form = document.getElementById('animalForm');
    const title = document.getElementById('modalTitle');
    
    form.reset();
    currentAnimalImage = null;
    document.getElementById('imagePreview').innerHTML = '<i class="fas fa-paw text-6xl text-gray-300"></i>';
    
    if (animalId) {
        title.textContent = 'Editar Animal';
        const animal = allAnimals.find(a => a.id === animalId);
        if (animal) {
            document.getElementById('animalId').value = animal.id;
            document.getElementById('animalNome').value = animal.nome;
            document.getElementById('animalEspecie').value = animal.especie;
            document.getElementById('animalRaca').value = animal.raca || '';
            document.getElementById('animalIdade').value = animal.idade;
            document.getElementById('animalSexo').value = animal.sexo || '';
            document.getElementById('animalPorte').value = animal.porte || '';
            document.getElementById('animalCor').value = animal.cor || '';
            document.getElementById('animalDescricao').value = animal.descricao || '';
            document.getElementById('animalVacinado').checked = animal.vacinado;
            document.getElementById('animalCastrado').checked = animal.castrado;
            document.getElementById('animalDisponivel').checked = animal.disponivelAdocao;
            
            if (animal.fotoUrl) {
                document.getElementById('imagePreview').innerHTML = 
                    `<img src="${animal.fotoUrl}" class="w-full h-full object-cover">`;
            }
        }
    } else {
        title.textContent = 'Adicionar Animal';
        document.getElementById('animalId').value = '';
    }
    
    modal.classList.remove('hidden');
}

function closeAnimalModal() {
    document.getElementById('animalModal').classList.add('hidden');
}

// Handle animal form submit
document.getElementById('animalForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const animalId = document.getElementById('animalId').value;
    const animalData = {
        nome: document.getElementById('animalNome').value,
        especie: document.getElementById('animalEspecie').value,
        raca: document.getElementById('animalRaca').value || null,
        idade: Number.parseInt(document.getElementById('animalIdade').value),
        sexo: document.getElementById('animalSexo').value || null,
        porte: document.getElementById('animalPorte').value || null,
        cor: document.getElementById('animalCor').value || null,
        descricao: document.getElementById('animalDescricao').value || null,
        vacinado: document.getElementById('animalVacinado').checked,
        castrado: document.getElementById('animalCastrado').checked,
        disponivelAdocao: document.getElementById('animalDisponivel').checked
    };
    
    try {
        const url = animalId ? `/api/animals/${animalId}` : '/api/animals';
        const method = animalId ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(animalData)
        });
        
        if (response.ok) {
            const result = await response.json();
            const savedAnimal = result.animal || result;
            
            // Upload image if selected
            if (currentAnimalImage) {
                await uploadAnimalImage(savedAnimal.id, currentAnimalImage);
            }
            
            closeAnimalModal();
            await loadAnimals();
            showAlert('success', animalId ? 'Animal atualizado com sucesso!' : 'Animal cadastrado com sucesso!');
        } else {
            showAlert('error', 'Erro ao salvar animal');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('error', 'Erro ao salvar animal');
    }
});

// Upload animal image
async function uploadAnimalImage(animalId, file) {
    const formData = new FormData();
    formData.append('file', file);
    
    try {
        const response = await fetch(`/api/animals/${animalId}/upload-image`, {
            method: 'POST',
            body: formData
        });
        
        if (!response.ok) {
            console.error('Erro ao fazer upload da imagem');
        }
    } catch (error) {
        console.error('Erro no upload:', error);
    }
}

// Edit animal
function editAnimal(id) {
    openAnimalModal(id);
}

// Delete animal
async function deleteAnimal(id) {
    if (!confirm('Tem certeza que deseja excluir este animal?')) return;
    
    try {
        const response = await fetch(`/api/animals/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            await loadAnimals();
            showAlert('success', 'Animal excluído com sucesso!');
        } else {
            showAlert('error', 'Erro ao excluir animal');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('error', 'Erro ao excluir animal');
    }
}

// Load animals available for adoption
async function loadAnimaisDisponiveis() {
    const disponiveis = allAnimals.filter(a => a.disponivelAdocao);
    const grid = document.getElementById('adocaoGrid');
    
    if (disponiveis.length === 0) {
        grid.innerHTML = `
            <div class="col-span-full text-center py-12 text-white">
                <i class="fas fa-heart text-6xl mb-4 opacity-50"></i>
                <p class="text-xl">Nenhum animal disponível para adoção no momento</p>
            </div>
        `;
        return;
    }
    
    grid.innerHTML = disponiveis.map(animal => `
        <div class="glass-card rounded-xl overflow-hidden shadow-lg hover:shadow-2xl transition duration-300">
            <div class="h-48 bg-gradient-to-br from-pink-100 to-red-100 flex items-center justify-center overflow-hidden">
                ${animal.fotoUrl 
                    ? `<img src="${animal.fotoUrl}" alt="${animal.nome}" class="w-full h-full object-cover">` 
                    : `<i class="fas fa-${getAnimalIconClass(animal.especie)} text-6xl text-pink-300"></i>`
                }
            </div>
            <div class="p-4 bg-white bg-opacity-60">
                <h3 class="text-xl font-bold text-gray-900 mb-2">${animal.nome}</h3>
                <div class="text-sm text-gray-800 space-y-1 mb-3">
                    <p><i class="fas fa-paw mr-2 text-pink-600"></i>${animal.especie}${animal.raca ? ` - ${animal.raca}` : ''}</p>
                    <p><i class="fas fa-birthday-cake mr-2 text-red-600"></i>${animal.idade} ${animal.idade === 1 ? 'ano' : 'anos'}</p>
                </div>
                ${animal.descricao ? `<p class="text-sm text-gray-700 mb-3">${animal.descricao}</p>` : ''}
                <button onclick="openAdocaoModal(${animal.id})" 
                    class="w-full bg-pink-600 hover:bg-pink-700 text-white py-2 rounded-lg font-semibold transition">
                    <i class="fas fa-heart mr-2"></i>Adotar
                </button>
            </div>
        </div>
    `).join('');
}

// Adoption Modal
function openAdocaoModal(animalId) {
    const modal = document.getElementById('adocaoModal');
    const animal = allAnimals.find(a => a.id === animalId);
    
    if (animal) {
        document.getElementById('adocaoAnimalId').value = animal.id;
        document.getElementById('selectedAnimalInfo').textContent = `${animal.nome} - ${animal.especie}`;
        document.getElementById('adocaoForm').reset();
        modal.classList.remove('hidden');
    }
}

function closeAdocaoModal() {
    document.getElementById('adocaoModal').classList.add('hidden');
}

// Handle adoption form submit
document.getElementById('adocaoForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const animalId = Number.parseInt(document.getElementById('adocaoAnimalId').value);
    const formData = {
        animalId: animalId,
        nome: document.getElementById('adocaoNome')?.value || currentUser.nome || '',
        email: document.getElementById('adocaoEmail')?.value || currentUser.email || '',
        telefone: document.getElementById('adocaoTelefone')?.value || '',
        endereco: document.getElementById('adocaoEndereco').value,
        motivoAdocao: document.getElementById('adocaoMotivo').value,
        experienciaAnimais: document.getElementById('adocaoExperiencia').value || ''
    };
    
    try {
        const response = await fetch('/api/formularios', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            closeAdocaoModal();
            showAlert('success', 'Formulário de adoção enviado com sucesso!');
            await loadFormularios();
        } else {
            const error = await response.json();
            showAlert('error', error.message || 'Erro ao enviar formulário');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('error', 'Erro ao enviar formulário');
    }
});

// Load user formularios
async function loadFormularios() {
    try {
        console.log('Carregando formulários...');
        const response = await fetch('/api/formularios/meus');
        console.log('Response status:', response.status);
        if (response.ok) {
            allFormularios = await response.json();
            console.log('Formulários carregados:', allFormularios);
            displayFormularios();
        } else {
            console.error('Erro ao carregar formulários:', response.statusText);
        }
    } catch (error) {
        console.error('Erro ao carregar formulários:', error);
    }
}

// Display formularios
function displayFormularios() {
    const lista = document.getElementById('formulariosLista');
    
    if (allFormularios.length === 0) {
        lista.innerHTML = `
            <div class="text-center py-12 text-white">
                <i class="fas fa-clipboard-list text-6xl mb-4 opacity-50"></i>
                <p class="text-xl">Você ainda não enviou nenhum formulário de adoção</p>
            </div>
        `;
        return;
    }
    
    lista.innerHTML = allFormularios.map(form => {
        const statusColors = {
            'PENDENTE': 'bg-yellow-100 text-yellow-800',
            'APROVADO': 'bg-green-100 text-green-800',
            'RECUSADO': 'bg-red-100 text-red-800',
            'CANCELADO': 'glass text-gray-800'
        };
        
        const dataSolicitacao = form.dataSolicitacao ? new Date(form.dataSolicitacao).toLocaleDateString('pt-BR') : 'N/A';
        
        return `
            <div class="glass-card rounded-xl p-6 shadow-lg">
                <div class="flex justify-between items-start mb-4 bg-white bg-opacity-50 p-3 rounded-lg">
                    <div>
                        <h3 class="text-xl font-bold text-gray-900">${form.animal ? form.animal.nome : 'Animal não encontrado'}</h3>
                        <p class="text-gray-800">${form.animal ? form.animal.especie : ''} ${form.animal && form.animal.raca ? `- ${form.animal.raca}` : ''}</p>
                    </div>
                    <span class="px-3 py-1 rounded-full text-sm font-semibold ${statusColors[form.status] || 'glass text-gray-800'}">
                        ${form.status}
                    </span>
                </div>
                <div class="text-sm text-gray-800 space-y-2 bg-white bg-opacity-40 p-3 rounded-lg">
                    <p><i class="fas fa-calendar mr-2"></i><strong>Data:</strong> ${dataSolicitacao}</p>
                    <p><i class="fas fa-map-marker-alt mr-2 text-red-600"></i><strong>Endereço:</strong> ${form.endereco || 'N/A'}</p>
                    ${form.tipoResidencia ? `<p><i class="fas fa-home mr-2 text-purple-600"></i><strong>Residência:</strong> ${form.tipoResidencia}</p>` : ''}
                    ${form.motivoAdocao ? `<p><i class="fas fa-heart mr-2 text-pink-600"></i><strong>Motivo:</strong> ${form.motivoAdocao}</p>` : ''}
                    ${form.observacoes ? `<p><i class="fas fa-comment mr-2 text-blue-600"></i><strong>Observações:</strong> ${form.observacoes}</p>` : ''}
                </div>
            </div>
        `;
    }).join('');
}

// Show alert
function showAlert(type, message) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `fixed top-4 right-4 px-6 py-4 rounded-lg shadow-lg text-white z-50 ${
        type === 'success' ? 'bg-green-500' : 'bg-red-500'
    }`;
    alertDiv.innerHTML = `
        <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'} mr-2"></i>
        ${message}
    `;
    
    document.body.appendChild(alertDiv);
    
    setTimeout(() => {
        alertDiv.remove();
    }, 3000);
}
