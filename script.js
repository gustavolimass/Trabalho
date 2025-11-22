// Endereço do seu Back End Spring Boot
const API_URL = "http://localhost:8080/api/produtos";

// Seleção de elementos do DOM
let form = document.getElementById("product-form");
let inproduct = document.getElementById("product");
let inquantity = document.getElementById("quantity");
let outResponse = document.getElementById("response"); // Para mensagens de erro/sucesso
const tableBody = document.getElementById("table-body");
const overlay = document.querySelector('.modal-overlay');
const modal = document.getElementById('modal');
const closeBtn = document.getElementById('modal-close');
const cancelBtn = document.querySelector('.btn-secondary'); // Botão cancelar do modal
let productModal = document.getElementById("update-product");
let quantityModal = document.getElementById("update-quantity");
let updateFormModal = document.getElementById("update-form");

// Variável para armazenar o ID do produto que está sendo editado
let currentIdBeingEdited = null;

// --- 1. FUNÇÃO PARA CARREGAR PRODUTOS (GET) ---
async function loadProducts() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Erro ao buscar produtos");
    
    const produtos = await response.json();
    
    // Limpa a tabela antes de desenhar
    tableBody.innerHTML = "";

    // Para cada produto vindo do Java, cria uma linha
    produtos.forEach(produto => {
      createRowHTML(produto);
    });
  } catch (error) {
    console.error(error);
    outResponse.textContent = "Erro de conexão com o servidor.";
  }
}

// Função auxiliar para criar a linha na tabela
function createRowHTML(produto) {
  const newRow = document.createElement('tr');
  // Guardamos o ID do banco de dados num atributo escondido na linha (dataset)
  newRow.dataset.id = produto.id; 

  // Trecho dentro do script.js
  newRow.innerHTML = `
    <td>${produto.nome}</td>
    <td style="text-align: center;">${produto.quantidade}</td>
    <td class="actions">
      <button class="btn-icon edit" title="Editar">
        <span class="material-symbols-outlined">edit</span>
      </button>
      
      <button class="btn-icon delete" title="Excluir">
        <span class="material-symbols-outlined">delete</span>
      </button>
    </td>
  `;
  tableBody.appendChild(newRow);
}

// --- 2. FUNÇÃO PARA ADICIONAR PRODUTO (POST) ---
async function addProduct() {
  let productValue = inproduct.value;
  let quantityValue = inquantity.value;

  if(productValue == ""){
    inproduct.focus();
    return;
  }
  if(quantityValue == 0 || isNaN(quantityValue)){
    inquantity.focus();
    return;
  }

  // Objeto JSON para enviar ao Java
  // IMPORTANTE: As chaves 'nome' e 'quantidade' devem ser iguais às da classe Java
  const novoProduto = {
    nome: productValue,
    quantidade: parseInt(quantityValue)
  };

  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(novoProduto)
    });

    if (response.ok) {
      // Se salvou com sucesso, recarrega a tabela e limpa o form
      inproduct.value = "";
      inquantity.value = "";
      loadProducts(); 
    } else {
      alert("Erro ao salvar produto");
    }
  } catch (error) {
    console.error("Erro na requisição:", error);
  }
}

// --- 3. FUNÇÃO PARA DELETAR (DELETE) ---
async function deleteProduct(id) {
  if(!confirm("Tem certeza que deseja excluir?")) return;

  try {
    await fetch(`${API_URL}/${id}`, {
      method: "DELETE"
    });
    // Remove da tela visualmente ou recarrega tudo
    loadProducts();
  } catch (error) {
    console.error("Erro ao deletar:", error);
  }
}

// --- 4. FUNÇÃO PARA EDITAR (PUT) ---
async function updateProduct() {
  const newName = productModal.value;
  const newQuantity = quantityModal.value;

  if (!currentIdBeingEdited) return;

  const produtoAtualizado = {
    nome: newName,
    quantidade: parseInt(newQuantity)
  };

  try {
    const response = await fetch(`${API_URL}/${currentIdBeingEdited}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(produtoAtualizado)
    });

    if(response.ok) {
      closeModal();
      loadProducts(); // Recarrega a tabela com os dados novos
    } else {
      alert("Erro ao atualizar.");
    }
  } catch (error) {
    console.error("Erro ao atualizar:", error);
  }
}

// --- EVENTOS E MODAIS ---

// Escuta cliques na tabela (para editar ou deletar)
tableBody.addEventListener('click', (event) => {
  const btnEdit = event.target.closest('.edit');
  const btnDelete = event.target.closest('.delete');
  const row = event.target.closest('tr');

  // Se clicou em EDITAR
  if (btnEdit && row) {
    // Pega o ID que guardamos no dataset
    const id = row.dataset.id;
    currentIdBeingEdited = id;

    // Preenche o modal com os dados atuais da tela
    const currentName = row.cells[0].textContent;
    const currentQty = row.cells[1].textContent;

    productModal.value = currentName;
    quantityModal.value = currentQty;

    modal.classList.add('active');
    overlay.style.display = "block";
  }

  // Se clicou em DELETAR
  if (btnDelete && row) {
    const id = row.dataset.id;
    deleteProduct(id);
  }
});

function closeModal() {
  modal.classList.remove('active');
  overlay.style.display = "none";
  currentIdBeingEdited = null;
}

// Eventos de fechar modal
closeBtn.addEventListener("click", closeModal);
cancelBtn.addEventListener("click", closeModal);
overlay.addEventListener("click", closeModal);

// Evento de Salvar Edição
updateFormModal.addEventListener("submit", (event) => {
  event.preventDefault(); // Não recarregar a página
  updateProduct();
});

// Evento de Adicionar Novo
form.addEventListener("submit", (event) => {
  event.preventDefault(); // Não recarregar a página
  addProduct();
});

// --- INICIALIZAÇÃO ---
// Assim que a página abre, carrega os dados do Back End
window.addEventListener("DOMContentLoaded", () => {
  loadProducts();
});