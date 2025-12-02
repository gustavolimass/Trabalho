// Painel de administração: categorias e produtos
const API_URL = "http://localhost:8080";

// --- DOM Elements ---
// Categorias
const categoryForm = document.getElementById("category-admin-form");
const categoryNameInput = document.getElementById("category-name-input");
const categoriesTableBody = document.getElementById("categories-table-body");

// Produtos
const productsTableBody = document.getElementById("products-table-body");

// Modal de produto
const adminOverlay = document.getElementById("admin-modal-overlay");
const productModal = document.getElementById("product-admin-modal");
const productModalCloseBtn = document.getElementById("product-modal-close");
const productModalCancelBtn = document.getElementById("product-modal-cancel");
const productAdminForm = document.getElementById("product-admin-form");
const adminProductNameInput = document.getElementById("admin-product-name");
const adminProductQuantityInput = document.getElementById("admin-product-quantity");
const adminProductCategorySelect = document.getElementById("admin-product-category");

let currentProductIdBeingEdited = null;
let cachedCategories = [];

// --- Utilidades ---
function openProductModal() {
  productModal.classList.add("active");
  adminOverlay.classList.add("active");
}

function closeProductModal() {
  productModal.classList.remove("active");
  adminOverlay.classList.remove("active");
  currentProductIdBeingEdited = null;
}

// --- Categorias ---
async function loadCategoriesAdmin() {
  try {
    const response = await fetch(`${API_URL}/api/category`);
    if (!response.ok) throw new Error("Erro ao buscar categorias");

    const categorias = await response.json();
    cachedCategories = categorias;

    // Popular tabela de categorias
    categoriesTableBody.innerHTML = "";
    categorias.forEach((cat) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td style="text-align:center;">${cat.id}</td>
        <td>${cat.name}</td>
        <td class="actions">
          <button class="btn-icon delete" data-id="${cat.id}" title="Excluir categoria">
            <span class="material-symbols-outlined">delete</span>
          </button>
        </td>
      `;
      categoriesTableBody.appendChild(tr);
    });

    // Popular select de categorias do modal de produto
    adminProductCategorySelect.innerHTML = `<option value="">Selecione a categoria</option>`;
    categorias.forEach((cat) => {
      const opt = document.createElement("option");
      opt.value = cat.id;
      opt.textContent = cat.name;
      adminProductCategorySelect.appendChild(opt);
    });
  } catch (error) {
    console.error("Erro ao carregar categorias (admin):", error);
  }
}

async function createCategoryAdmin() {
  const name = categoryNameInput.value.trim();
  if (!name) {
    categoryNameInput.focus();
    return;
  }

  try {
    const response = await fetch(`${API_URL}/api/category`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name }),
    });

    if (!response.ok) {
      console.error("Erro ao criar categoria", response.status);
      return;
    }

    categoryNameInput.value = "";
    await loadCategoriesAdmin();
  } catch (error) {
    console.error("Erro de conexão ao criar categoria:", error);
  }
}

async function deleteCategoryAdmin(id) {
  if (!confirm("Tem certeza que deseja excluir esta categoria?")) return;

  try {
    const response = await fetch(`${API_URL}/api/category/${id}`, {
      method: "DELETE",
    });

    if (!response.ok) {
      console.error("Erro ao excluir categoria", response.status);
      return;
    }

    await loadCategoriesAdmin();
    await loadProductsAdmin();
  } catch (error) {
    console.error("Erro de conexão ao excluir categoria:", error);
  }
}

// --- Produtos ---
async function loadProductsAdmin() {
  try {
    const response = await fetch(`${API_URL}/api/product`);
    if (!response.ok) throw new Error("Erro ao buscar produtos");

    const produtos = await response.json();
    productsTableBody.innerHTML = "";

    produtos.forEach((produto) => {
      const tr = document.createElement("tr");
      tr.dataset.id = produto.id;

      const categoryName = produto.categoryId?.name ?? "-";

      tr.innerHTML = `
        <td>${produto.name}</td>
        <td style="text-align:center;">${produto.quantity}</td>
        <td style="text-align:center;">${categoryName}</td>
        <td class="actions">
          <button class="btn-icon edit" data-id="${produto.id}" title="Editar produto">
            <span class="material-symbols-outlined">edit</span>
          </button>
          <button class="btn-icon delete" data-id="${produto.id}" title="Excluir produto">
            <span class="material-symbols-outlined">delete</span>
          </button>
        </td>
      `;

      productsTableBody.appendChild(tr);
    });
  } catch (error) {
    console.error("Erro ao carregar produtos (admin):", error);
  }
}

function handleOpenEditProduct(id) {
  const row = productsTableBody.querySelector(`tr[data-id="${id}"]`);
  if (!row) return;

  const name = row.cells[0].textContent;
  const quantity = row.cells[1].textContent;
  const categoryName = row.cells[2].textContent;

  currentProductIdBeingEdited = id;

  adminProductNameInput.value = name;
  adminProductQuantityInput.value = quantity;

  // Selecionar categoria correspondente se existir
  const cat = cachedCategories.find((c) => c.name === categoryName);
  if (cat) {
    adminProductCategorySelect.value = String(cat.id);
  } else {
    adminProductCategorySelect.value = "";
  }

  openProductModal();
}

async function updateProductAdmin(event) {
  event.preventDefault();
  if (!currentProductIdBeingEdited) return;

  const newName = adminProductNameInput.value.trim();
  const newQuantity = parseInt(adminProductQuantityInput.value, 10);
  const categoryId = parseInt(adminProductCategorySelect.value, 10);

  if (!newName) {
    adminProductNameInput.focus();
    return;
  }
  if (isNaN(newQuantity)) {
    adminProductQuantityInput.focus();
    return;
  }
  if (isNaN(categoryId)) {
    adminProductCategorySelect.focus();
    return;
  }

  const categoryObj = cachedCategories.find((c) => c.id === categoryId);
  if (!categoryObj) {
    alert("Categoria selecionada não foi encontrada na lista carregada.");
    return;
  }

  const payload = {
    name: newName,
    quantity: newQuantity,
    categoryId: {
      id: categoryObj.id,
      name: categoryObj.name,
    },
  };

  try {
    const response = await fetch(`${API_URL}/api/product/${currentProductIdBeingEdited}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      console.error("Erro ao atualizar produto", response.status);
      return;
    }

    closeProductModal();
    await loadProductsAdmin();
  } catch (error) {
    console.error("Erro de conexão ao atualizar produto:", error);
  }
}

async function deleteProductAdmin(id) {
  if (!confirm("Tem certeza que deseja excluir este produto?")) return;

  try {
    const response = await fetch(`${API_URL}/api/product/${id}`, {
      method: "DELETE",
    });

    if (!response.ok) {
      console.error("Erro ao excluir produto", response.status);
      return;
    }

    await loadProductsAdmin();
  } catch (error) {
    console.error("Erro de conexão ao excluir produto:", error);
  }
}

// --- Event Listeners ---
if (categoryForm) {
  categoryForm.addEventListener("submit", (event) => {
    event.preventDefault();
    createCategoryAdmin();
  });
}

if (categoriesTableBody) {
  categoriesTableBody.addEventListener("click", (event) => {
    const deleteBtn = event.target.closest(".delete");
    if (deleteBtn) {
      const id = deleteBtn.getAttribute("data-id");
      if (id) deleteCategoryAdmin(id);
    }
  });
}

if (productsTableBody) {
  productsTableBody.addEventListener("click", (event) => {
    const editBtn = event.target.closest(".edit");
    const deleteBtn = event.target.closest(".delete");

    if (editBtn) {
      const id = editBtn.getAttribute("data-id");
      if (id) handleOpenEditProduct(id);
    }

    if (deleteBtn) {
      const id = deleteBtn.getAttribute("data-id");
      if (id) deleteProductAdmin(id);
    }
  });
}

if (productModalCloseBtn) {
  productModalCloseBtn.addEventListener("click", closeProductModal);
}

if (productModalCancelBtn) {
  productModalCancelBtn.addEventListener("click", closeProductModal);
}

if (adminOverlay) {
  adminOverlay.addEventListener("click", closeProductModal);
}

if (productAdminForm) {
  productAdminForm.addEventListener("submit", updateProductAdmin);
}

// --- Inicialização ---
window.addEventListener("DOMContentLoaded", async () => {
  await loadCategoriesAdmin();
  await loadProductsAdmin();
});
