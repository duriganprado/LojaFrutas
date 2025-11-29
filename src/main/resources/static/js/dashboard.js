let currentUser = null;
let allFrutas = [];
let allPedidos = [];
let currentFrutaImage = null;

// Initialize
document.addEventListener('DOMContentLoaded', async () => {
    await checkAuth();
    initializeNavigation();
    initializeFilters();
    initializeImagePreview();
});

// Check authentication
async function checkAuth() {
    try {
        console.log('Checking authentication...');
        const response = await fetch('/api/clientes/me');
        console.log('Auth response status:', response.status);
        
        if (!response.ok) {
            console.error('Auth failed, redirecting to login');
            window.location.href = '/login';
            return;
        }

        currentUser = await response.json();
        console.log('Current user:', currentUser);
        document.getElementById('userNameDisplay').textContent = currentUser.nome;
        
        await loadFrutas();
        await loadPedidos();
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
function initializeNavigation() {
    const navItems = document.querySelectorAll('.nav-item');
    
    // Set first item as active and show first section
    const firstSection = document.querySelector('.section');
    if (firstSection) {
        firstSection.classList.remove('hidden');
    }
    
    navItems.forEach(item => {
        // Skip logout button
        if (item.textContent.includes('Sair')) return;
        
        item.addEventListener('click', () => {
            const section = item.getAttribute('data-section');
            if (!section) return;
            
            // Remove active from all nav items
            document.querySelectorAll('.nav-item').forEach(nav => {
                if (nav.getAttribute('data-section')) {
                    nav.classList.remove('active');
                }
            });
            item.classList.add('active');
            
            // Hide all sections with animation
            document.querySelectorAll('.section').forEach(sec => sec.classList.add('hidden'));
            
            // Show selected section with animation
            const selectedSection = document.getElementById(`${section}-section`);
            if (selectedSection) {
                selectedSection.classList.remove('hidden');
                
                // Load data when section is shown
                if (section === 'frutas') loadFrutas();
                else if (section === 'catalogo') displayCatalogo(allFrutas);
                else if (section === 'pedidos') loadPedidos();
            }
        });
    });
}

// Initialize filters
function initializeFilters() {
    const searchInput = document.getElementById('searchFruta');
    const filterCategoria = document.getElementById('filterCategoria');
    const searchCatalogo = document.getElementById('searchFrutaCatalogo');
    const filterCategoriaCatalogo = document.getElementById('filterCategoriaCatalogo');
    
    if (searchInput) searchInput.addEventListener('input', filterFrutas);
    if (filterCategoria) filterCategoria.addEventListener('change', filterFrutas);
    if (searchCatalogo) searchCatalogo.addEventListener('input', filterCatalogo);
    if (filterCategoriaCatalogo) filterCategoriaCatalogo.addEventListener('change', filterCatalogo);
}

// Filter frutas
function filterFrutas() {
    const search = document.getElementById('searchFruta')?.value?.toLowerCase() || '';
    const categoria = document.getElementById('filterCategoria')?.value || '';
    
    const filtered = allFrutas.filter(fruta => {
        const matchSearch = fruta.nome.toLowerCase().includes(search);
        const matchCategoria = !categoria || fruta.categoria === categoria;
        return matchSearch && matchCategoria;
    });
    
    displayFrutas(filtered);
}

// Filter catalogo
function filterCatalogo() {
    const search = document.getElementById('searchFrutaCatalogo')?.value?.toLowerCase() || '';
    const categoria = document.getElementById('filterCategoriaCatalogo')?.value || '';
    
    const filtered = allFrutas.filter(fruta => {
        const matchSearch = fruta.nome.toLowerCase().includes(search);
        const matchCategoria = !categoria || fruta.categoria === categoria;
        const matchDisponivel = fruta.disponivel;
        return matchSearch && matchCategoria && matchDisponivel;
    });
    
    displayCatalogo(filtered);
}

// Load all frutas
async function loadFrutas() {
    try {
        console.log('Carregando frutas...');
        const response = await fetch('/api/frutas');
        if (response.ok) {
            allFrutas = await response.json();
            console.log('Frutas carregadas:', allFrutas);
            displayFrutas(allFrutas);
        } else {
            console.error('Erro ao carregar frutas:', response.statusText);
        }
    } catch (error) {
        console.error('Erro ao carregar frutas:', error);
    }
}

// Display catalogo (user shopping view)
function displayCatalogo(frutas) {
    const grid = document.getElementById('catalogoGrid');
    
    if (!grid) return;
    
    // Filter only available frutas
    const availableFrutas = frutas.filter(f => f.disponivel);
    
    if (availableFrutas.length === 0) {
        grid.innerHTML = `
            <div class="col-span-full text-center py-12 text-gray-500">
                <i class="fas fa-apple-alt text-6xl mb-4 opacity-30"></i>
                <p class="text-xl">Nenhuma fruta disponível no momento</p>
            </div>
        `;
        return;
    }
    
    grid.innerHTML = availableFrutas.map(fruta => `
        <div class="bg-white border-2 border-gray-100 rounded-lg overflow-hidden hover:shadow-lg transition duration-300">
            <div class="h-32 bg-gradient-to-br from-orange-100 to-yellow-100 flex items-center justify-center overflow-hidden">
                ${fruta.fotoUrl 
                    ? `<img src="${fruta.fotoUrl}" alt="${fruta.nome}" class="w-full h-full object-cover">` 
                    : `<i class="fas fa-apple-alt text-4xl text-orange-300"></i>`
                }
            </div>
            <div class="p-3 bg-white">
                <h3 class="text-lg font-bold text-gray-900 truncate">${fruta.nome}</h3>
                <p class="text-sm text-gray-600 mb-2">${fruta.categoria}</p>
                
                <div class="flex justify-between items-center mb-2">
                    <span class="text-xl font-bold text-orange-600">R$ ${(fruta.preco || 0).toFixed(2)}</span>
                    <span class="text-xs bg-orange-100 text-orange-800 px-2 py-1 rounded">${fruta.estoque} kg</span>
                </div>
                
                ${fruta.descricao ? `<p class="text-xs text-gray-600 mb-2 line-clamp-2">${fruta.descricao}</p>` : ''}
                
                <div class="flex flex-wrap gap-1 mb-3">
                    ${fruta.organico ? '<span class="text-xs bg-green-100 text-green-700 px-2 py-1 rounded"><i class="fas fa-leaf"></i> Org</span>' : ''}
                    ${fruta.emPromocao ? '<span class="text-xs bg-red-100 text-red-700 px-2 py-1 rounded"><i class="fas fa-tag"></i> Promo</span>' : ''}
                </div>
                
                <button onclick="openPedidoModal(${fruta.id}, '${fruta.nome.replace(/'/g, "\\'")}', ${fruta.preco})" 
                        class="w-full bg-orange-600 hover:bg-orange-700 text-white py-2 rounded font-semibold transition">
                    <i class="fas fa-shopping-cart mr-2"></i>Comprar
                </button>
            </div>
        </div>
    `).join('');
}

// Display frutas
function displayFrutas(frutas) {
    const grid = document.getElementById('frutasGrid');
    
    if (!grid) return;
    
    if (frutas.length === 0) {
        grid.innerHTML = `
            <div class="col-span-full text-center py-12 text-gray-500">
                <i class="fas fa-apple-alt text-6xl mb-4 opacity-30"></i>
                <p class="text-xl">Nenhuma fruta encontrada</p>
            </div>
        `;
        return;
    }
    
    grid.innerHTML = frutas.map(fruta => `
        <div class="bg-white border-2 border-gray-100 rounded-lg overflow-hidden hover:shadow-lg transition duration-300">
            <div class="h-32 bg-gradient-to-br from-orange-100 to-yellow-100 flex items-center justify-center overflow-hidden">
                ${fruta.fotoUrl 
                    ? `<img src="${fruta.fotoUrl}" alt="${fruta.nome}" class="w-full h-full object-cover">` 
                    : `<i class="fas fa-apple-alt text-4xl text-orange-300"></i>`
                }
            </div>
            <div class="p-3 bg-white">
                <h3 class="text-lg font-bold text-gray-900 truncate">${fruta.nome}</h3>
                <p class="text-sm text-gray-600 mb-2">${fruta.categoria}</p>
                
                <div class="flex justify-between items-center mb-2">
                    <span class="text-xl font-bold text-orange-600">R$ ${(fruta.preco || 0).toFixed(2)}</span>
                    <span class="text-xs bg-orange-100 text-orange-800 px-2 py-1 rounded">${fruta.estoque} kg</span>
                </div>
                
                <div class="flex flex-wrap gap-1 mb-3">
                    ${fruta.organico ? '<span class="text-xs bg-green-100 text-green-700 px-2 py-1 rounded"><i class="fas fa-leaf"></i> Org</span>' : ''}
                    ${fruta.emPromocao ? '<span class="text-xs bg-red-100 text-red-700 px-2 py-1 rounded"><i class="fas fa-tag"></i> Promo</span>' : ''}
                </div>
                
                <div class="flex gap-2">
                    <button onclick="editFruta(${fruta.id})" class="flex-1 bg-blue-500 hover:bg-blue-600 text-white py-1 text-xs rounded font-semibold transition">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button onclick="deleteFruta(${fruta.id})" class="flex-1 bg-red-500 hover:bg-red-600 text-white py-1 text-xs rounded font-semibold transition">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

// Initialize image preview
function initializeImagePreview() {
    const input = document.getElementById('frutaFoto');
    if (!input) return;
    
    input.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            currentFrutaImage = file;
            const reader = new FileReader();
            reader.onload = (event) => {
                document.getElementById('imagePreview').innerHTML = 
                    `<img src="${event.target.result}" class="w-full h-full object-cover">`;
            };
            reader.readAsDataURL(file);
        }
    });
}

// Fruta Modal
function openFrutaModal(frutaId = null) {
    const modal = document.getElementById('frutaModal');
    if (!modal) return;
    
    const form = document.getElementById('frutaForm');
    const title = document.getElementById('modalTitle');
    
    form.reset();
    currentFrutaImage = null;
    const preview = document.getElementById('imagePreview');
    if (preview) preview.innerHTML = '<i class="fas fa-apple-alt text-6xl text-gray-300"></i>';
    
    if (frutaId) {
        title.textContent = 'Editar Fruta';
        const fruta = allFrutas.find(a => a.id === frutaId);
        if (fruta) {
            document.getElementById('frutaId').value = fruta.id;
            document.getElementById('frutaNome').value = fruta.nome;
            document.getElementById('frutaCategoria').value = fruta.categoria;
            document.getElementById('frutaOrigem').value = fruta.origem || '';
            document.getElementById('frutaPreco').value = fruta.preco || '';
            document.getElementById('frutaEstoque').value = fruta.estoque || '';
            document.getElementById('frutaCor').value = fruta.cor || '';
            document.getElementById('frutaSabor').value = fruta.sabor || '';
            document.getElementById('frutaUnidade').value = fruta.unidadeMedida || '';
            document.getElementById('frutaDescricao').value = fruta.descricao || '';
            document.getElementById('frutaOrganico').checked = fruta.organico;
            document.getElementById('frutaPromo').checked = fruta.emPromocao;
            document.getElementById('frutaDisponivel').checked = fruta.disponivel;
            
            if (fruta.fotoUrl && preview) {
                preview.innerHTML = 
                    `<img src="${fruta.fotoUrl}" class="w-full h-full object-cover">`;
            }
        }
    } else {
        title.textContent = 'Adicionar Fruta';
        document.getElementById('frutaId').value = '';
    }
    
    modal.classList.remove('hidden');
}

function closeFrutaModal() {
    const modal = document.getElementById('frutaModal');
    if (modal) modal.classList.add('hidden');
}

// Handle fruta form submit
document.getElementById('frutaForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const frutaId = document.getElementById('frutaId').value;
    const frutaData = {
        nome: document.getElementById('frutaNome').value,
        categoria: document.getElementById('frutaCategoria').value,
        origem: document.getElementById('frutaOrigem').value || null,
        preco: parseFloat(document.getElementById('frutaPreco').value),
        estoque: parseInt(document.getElementById('frutaEstoque').value),
        cor: document.getElementById('frutaCor').value || null,
        sabor: document.getElementById('frutaSabor').value || null,
        unidadeMedida: document.getElementById('frutaUnidade').value || null,
        descricao: document.getElementById('frutaDescricao').value || null,
        organico: document.getElementById('frutaOrganico').checked,
        emPromocao: document.getElementById('frutaPromo').checked,
        disponivel: document.getElementById('frutaDisponivel').checked
    };
    
    try {
        const url = frutaId ? `/api/frutas/${frutaId}` : '/api/frutas';
        const method = frutaId ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(frutaData)
        });
        
        if (response.ok) {
            const result = await response.json();
            const savedFruta = result.fruta || result;
            
            // Upload image if selected
            if (currentFrutaImage) {
                await uploadFrutaImage(savedFruta.id, currentFrutaImage);
            }
            
            closeFrutaModal();
            await loadFrutas();
            showAlert('success', frutaId ? 'Fruta atualizada com sucesso!' : 'Fruta cadastrada com sucesso!');
        } else {
            const error = await response.json();
            showAlert('error', error.message || 'Erro ao salvar fruta');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('error', 'Erro ao salvar fruta');
    }
});

// Upload fruta image
async function uploadFrutaImage(frutaId, file) {
    const formData = new FormData();
    formData.append('file', file);
    
    try {
        console.log('Iniciando upload de imagem para fruta:', frutaId);
        const response = await fetch(`/api/frutas/${frutaId}/upload-imagem`, {
            method: 'POST',
            body: formData
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            console.error('Erro ao fazer upload da imagem:', response.status, errorText);
            showAlert('error', 'Erro ao fazer upload da imagem: ' + response.status);
            return false;
        }
        
        console.log('Upload de imagem concluído com sucesso');
        return true;
    } catch (error) {
        console.error('Erro no upload:', error);
        showAlert('error', 'Erro ao fazer upload da imagem: ' + error.message);
        return false;
    }
}

// Edit fruta
function editFruta(id) {
    openFrutaModal(id);
}

// Delete fruta
async function deleteFruta(id) {
    if (!confirm('Tem certeza que deseja excluir esta fruta?')) return;
    
    try {
        const response = await fetch(`/api/frutas/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            await loadFrutas();
            showAlert('success', 'Fruta excluída com sucesso!');
        } else {
            showAlert('error', 'Erro ao excluir fruta');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('error', 'Erro ao excluir fruta');
    }
}

// Load user pedidos
async function loadPedidos() {
    try {
        console.log('Carregando pedidos...');
        const response = await fetch('/api/pedidos');
        console.log('Response status:', response.status);
        if (response.ok) {
            allPedidos = await response.json();
            console.log('Pedidos carregados:', allPedidos);
            displayPedidos();
        } else {
            console.error('Erro ao carregar pedidos:', response.statusText);
        }
    } catch (error) {
        console.error('Erro ao carregar pedidos:', error);
    }
}

// Display pedidos
function displayPedidos() {
    const lista = document.getElementById('pedidosLista');
    
    if (!lista) return;
    
    if (allPedidos.length === 0) {
        lista.innerHTML = `
            <div class="text-center py-12 text-white">
                <i class="fas fa-shopping-cart text-6xl mb-4 opacity-50"></i>
                <p class="text-xl">Você ainda não fez nenhum pedido</p>
            </div>
        `;
        return;
    }
    
    lista.innerHTML = allPedidos.map(pedido => {
        const statusColors = {
            'PENDENTE': 'bg-yellow-100 text-yellow-800',
            'CONFIRMADO': 'bg-blue-100 text-blue-800',
            'PREPARANDO': 'bg-purple-100 text-purple-800',
            'ENTREGUE': 'bg-green-100 text-green-800',
            'CANCELADO': 'bg-red-100 text-red-800'
        };
        
        const dataPedido = pedido.dataPedido ? new Date(pedido.dataPedido).toLocaleDateString('pt-BR') : 'N/A';
        const dataEntrega = pedido.dataEntrega ? new Date(pedido.dataEntrega).toLocaleDateString('pt-BR') : 'N/A';
        
        return `
            <div class="bg-white rounded-xl p-6 shadow-lg">
                <div class="flex justify-between items-start mb-4 bg-orange-50 p-3 rounded-lg">
                    <div>
                        <h3 class="text-xl font-bold text-gray-900">Pedido #${pedido.id}</h3>
                        <p class="text-gray-800">${pedido.fruta ? pedido.fruta.nome : 'Fruta não encontrada'}</p>
                    </div>
                    <span class="px-3 py-1 rounded-full text-sm font-semibold ${statusColors[pedido.status] || 'bg-gray-100 text-gray-800'}">
                        ${pedido.status}
                    </span>
                </div>
                <div class="text-sm text-gray-800 space-y-2 bg-orange-50 p-3 rounded-lg">
                    <p><i class="fas fa-calendar mr-2"></i><strong>Data do Pedido:</strong> ${dataPedido}</p>
                    <p><i class="fas fa-boxes mr-2 text-orange-600"></i><strong>Quantidade:</strong> ${pedido.quantidade}</p>
                    <p><i class="fas fa-dollar-sign mr-2 text-yellow-600"></i><strong>Preço Unitário:</strong> R$ ${(pedido.precoUnitario || 0).toFixed(2)}</p>
                    <p><i class="fas fa-map-marker-alt mr-2 text-red-600"></i><strong>Endereço:</strong> ${pedido.enderecoEntrega || 'N/A'}</p>
                    <p><i class="fas fa-phone mr-2 text-blue-600"></i><strong>Telefone:</strong> ${pedido.telefoneEntrega || 'N/A'}</p>
                    ${pedido.dataEntrega ? `<p><i class="fas fa-truck mr-2 text-purple-600"></i><strong>Entrega:</strong> ${dataEntrega}</p>` : ''}
                    ${pedido.observacoes ? `<p><i class="fas fa-comment mr-2 text-blue-600"></i><strong>Observações:</strong> ${pedido.observacoes}</p>` : ''}
                </div>
            </div>
        `;
    }).join('');
}

// Pedido Modal
function openPedidoModal(frutaId = null, frutaNome = null, preco = null) {
    const modal = document.getElementById('pedidoModal');
    if (!modal) return;
    
    const form = document.getElementById('pedidoForm');
    const title = document.getElementById('pedidoModalTitle');
    
    form.reset();
    
    if (frutaId) {
        title.textContent = 'Fazer Pedido';
        
        // Se os parâmetros foram passados diretamente (do catálogo), use-os
        if (frutaNome && preco !== null) {
            document.getElementById('pedidoFrutaId').value = frutaId;
            document.getElementById('selectedFrutaInfo').textContent = `${frutaNome} - R$ ${preco.toFixed(2)}`;
        } else {
            // Caso contrário, procure no array allFrutas
            const fruta = allFrutas.find(a => a.id === frutaId);
            if (fruta) {
                document.getElementById('pedidoFrutaId').value = fruta.id;
                document.getElementById('selectedFrutaInfo').textContent = `${fruta.nome} - R$ ${(fruta.preco || 0).toFixed(2)}`;
            }
        }
        
        document.getElementById('pedidoEndereco').value = '';
        document.getElementById('pedidoTelefone').value = '';
        document.getElementById('pedidoQuantidade').value = '1';
    }
    
    modal.classList.remove('hidden');
}

function closePedidoModal() {
    const modal = document.getElementById('pedidoModal');
    if (modal) modal.classList.add('hidden');
}

// Handle pedido form submit
document.getElementById('pedidoForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const frutaId = Number.parseInt(document.getElementById('pedidoFrutaId').value);
    const dataEntregaInput = document.getElementById('pedidoDataEntrega')?.value;
    let dataEntregaDesejada = null;
    
    // Converter data para ISO 8601 com hora (2025-12-01 -> 2025-12-01T00:00:00)
    if (dataEntregaInput) {
        dataEntregaDesejada = dataEntregaInput + 'T00:00:00';
    }
    
    const pedidoData = {
        frutaId: frutaId,
        quantidade: parseInt(document.getElementById('pedidoQuantidade').value),
        enderecoEntrega: document.getElementById('pedidoEndereco').value,
        telefoneEntrega: document.getElementById('pedidoTelefone').value,
        dataEntregaDesejada: dataEntregaDesejada,
        observacoes: document.getElementById('pedidoObservacoes')?.value || ''
    };
    
    try {
        const response = await fetch('/api/pedidos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(pedidoData)
        });
        
        if (response.ok) {
            closePedidoModal();
            showAlert('success', 'Pedido criado com sucesso!');
            await loadPedidos();
        } else {
            const error = await response.json();
            showAlert('error', error.message || 'Erro ao criar pedido');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('error', 'Erro ao criar pedido');
    }
});

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
