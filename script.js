let form = document.getElementById("product-form");
let inproduct = document.getElementById("product");
let inquantity = document.getElementById("quantity");
let outResponse = document.getElementById("response")
let btnForm = document.getElementById("submit-product")
const tableBody = document.getElementById("table-body")
const overlay = document.querySelector('.modal-overlay');
const modal = document.getElementById('modal');
const closeBtn = document.getElementById('modal-close');
const cancelBtn = document.querySelector('.btn-secondary');
let currentRowBeingEdited = null;
let productModal = document.getElementById("update-product");
let quantityModal = document.getElementById("update-quantity");
let uptadeFormModal = document.getElementById("update-form");

function addProduct() {
  let productValue = inproduct.value;
  let quantityValue = inquantity.value;

  if(productValue == ""){
    inproduct.focus();
    return
  }

  if(quantityValue == 0 || isNaN(quantityValue)){
    inquantity.focus();
    return
  }
  const newRow = document.createElement('tr');

  newRow.innerHTML = `
    <td>${productValue}</td>
    <td>${quantityValue}</td>
    <td class="actions">
      <button class="btn-icon edit"><span class="material-symbols-outlined">edit</span></button>
      <button class="btn-icon delete"><span class="material-symbols-outlined">delete</span></button>
    </td>
  `;

  tableBody.appendChild(newRow)

  inproduct.value = "";
  inquantity.value = "";
  inproduct.focus();
}



tableBody.addEventListener('click', (event) => {
  if (event.target.closest('.edit')) {
    modal.classList.add('active');
    const tableRow = event.target.closest('tr')
    openModal(tableRow)
  }

  if (event.target.closest('.delete')) {
    const row = event.target.closest('tr');
    row.remove();
  }
});

function openModal(tableRow) {
  currentRowBeingEdited = tableRow;

  const currentProduct = tableRow.cells[0].textContent
  const quantityProduct = tableRow.cells[1].textContent

  productModal.value = currentProduct;
  quantityModal.value = quantityProduct;

  modal.classList.add('active');
  overlay.style.display = "block";
}


function productUpadateModal() {
  let newProduct = productModal.value;
  let neQuantity = quantityModal.value;

  currentRowBeingEdited.cells[0].textContent = newProduct ;
  currentRowBeingEdited.cells[1].textContent = neQuantity ;

  closeModal()

}

function closeModal() {
  modal.classList.remove('active');
  overlay.style.display = "none";
}

closeBtn.addEventListener("click", closeModal)
cancelBtn.addEventListener("click", closeModal)
overlay.addEventListener("click", closeModal)

uptadeFormModal.addEventListener("submit", (event) => {
  event.preventDefault();
  productUpadateModal();
})


form.addEventListener("submit", (event) => {
  event.preventDefault();
  addProduct();
});