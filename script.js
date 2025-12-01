// Endereço do seu Back End Spring Boot
const API_URL = "http://localhost:8080";

// Seleção de elementos do DOM
let form = document.getElementById("product-form");
let formCategory = document.getElementById("category-form");
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
const categorySelect = document.getElementById("categoies");
const updateCategorySelect = document.getElementById("update-category");
const filterByCategorySelect = document.getElementById("filter-by-category");

// Variável para armazenar o ID do produto que está sendo editado
let currentIdBeingEdited = null;

// --- 1. FUNÇÃO PARA CARREGAR PRODUTOS (GET) ---
async function loadProducts() {
    try {
        const response = await fetch(`${API_URL}/api/product`);
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
        outResponse.textContent = "Erro de conexão com o servidor na rota /api/product.";
    }
}

async function filterProducts() {
    try {
        if (!filterByCategorySelect.value) {
            outResponse.textContent = ""; // limpa mensagem anterior
            await loadProducts();
            return;
        }
        const categoryId = parseInt(filterByCategorySelect.value, 10);
        if (isNaN(categoryId)) {
            outResponse.textContent = "Categoria inválida.";
            await loadProducts();
            return;
        }
        const response = await fetch(`${API_URL}/api/product/by-category?categoryId=${categoryId}`)
        if (!response.ok) {
            if (response.status === 404) {
                // sem produtos para a categoria selecionada
                tableBody.innerHTML = "";
                outResponse.textContent = "Nenhum produto encontrado para esta categoria.";
                return;
            }
            throw new Error("Erro ao buscar produtos por categoria");
        }

        const produtos = await response.json();

        // Limpa a tabela antes de desenhar
        tableBody.innerHTML = "";

        // Para cada produto vindo do Java, cria uma linha
        produtos.forEach(produto => {
            createRowHTML(produto);
        });
    } catch (error) {
        console.error(error);
        outResponse.textContent = "Erro de conexão com o servidor na rota /api/product/by-category.";
    }

}

async function loadCategories() {
    try {
        const response = await fetch(`${API_URL}/api/category`);
        if (!response.ok) throw new Error("Erro ao buscar categorias");

        const categorias = await response.json();

        categorySelect.innerHTML = `<option value="">Selecione a categoria</option>`;
        updateCategorySelect.innerHTML = `<option value="">Selecione a categoria</option>`;
        filterByCategorySelect.innerHTML = `<option value="">Selecione a categoria</option>`;



        categorias.forEach(cat => {
            const option1 = document.createElement("option");
            option1.value = cat.id;
            option1.textContent = cat.name;
            categorySelect.appendChild(option1);

            const option2 = document.createElement("option");
            option2.value = cat.id;
            option2.textContent = cat.name;
            filterByCategorySelect.appendChild(option2);

            const option3 = document.createElement("option");
            option3.value = cat.id;
            option3.textContent = cat.name;
            updateCategorySelect.appendChild(option3);

        });

        
    } catch (error) {
        console.error(error);
        outResponse.textContent = "Erro de conexão com o servidor na rota /api/category.";
    }
}

// Função auxiliar para criar a linha na tabela
function createRowHTML(produto) {
    const newRow = document.createElement('tr');
    // Guardamos o ID do banco de dados num atributo escondido na linha (dataset)
    newRow.dataset.id = produto.id;
    newRow.dataset.categoryId = produto.categoryId.id;
    newRow.dataset.categoryName = produto.categoryId.name;


    // Trecho dentro do script.js
    newRow.innerHTML = `
    <td>${produto.name}</td>
    <td style="text-align: center;">${produto.quantity}</td>
    <td style="text-align: center;">${produto.categoryId.name}</td>
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

    const categoryId = parseInt(categorySelect.value);
    const categoryName = categorySelect.options[categorySelect.selectedIndex].text;


    if(productValue == ""){
        inproduct.focus();
        return;
    }
    if(quantityValue == 0 || isNaN(quantityValue)){
        inquantity.focus();
        return;
    }

    if (isNaN(categoryId)) {
        alert("Selecione uma categoria!");
        categorySelect.focus();
        return;
    }

    // Objeto JSON no formato que o backend exige
    const novoProduto = {
        name: productValue,
        quantity: parseInt(quantityValue),
        categoryId: {
            id: categoryId,
            name: categoryName
        }
    };

    try {
        const response = await fetch(`${API_URL}/api/product`, {
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
            categorySelect.value = ""
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
        await fetch(`${API_URL}/api/product/${id}`, {
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
    const newQuantity = parseInt(quantityModal.value, 10);

    let newCategoryId = parseInt(updateCategorySelect.value);
    let newCategoryName = updateCategorySelect.options[updateCategorySelect.selectedIndex]?.text || "";

    console.log(newCategoryId, newCategoryName)

    // fallback: se o select não estiver com valor válido, usa a categoria da linha editada
    if (isNaN(newCategoryId) && currentIdBeingEdited) {
        const row = document.querySelector(`tr[data-id="${currentIdBeingEdited}"]`);
        if (row) {
            newCategoryId = parseInt(row.dataset.categoryId, 10);
            newCategoryName = row.dataset.categoryName;
        }
    }

    console.log(currentIdBeingEdited)

    console.log(newCategoryId, newCategoryName)

    const produtoAtualizado = {
        name: newName,
        quantity: newQuantity,
        categoryId: {
            id: newCategoryId,
            name: newCategoryName
        }
}

console.log(produtoAtualizado)

    try {
        const response = await fetch(`${API_URL}/api/product/${currentIdBeingEdited}`, {
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

        updateCategorySelect.value = row.dataset.categoryId;

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

formCategory.addEventListener("submit", (event) => {
    event.preventDefault(); // Não recarregar a página
    filterProducts();
});

// --- INICIALIZAÇÃO ---
// Assim que a página abre, carrega os dados do Back End
window.addEventListener("DOMContentLoaded", async () => {
    await loadCategories()
    await loadProducts();
});