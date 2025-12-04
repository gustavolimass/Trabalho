# Projeto de Gerenciamento de Estoque

Este é um projeto full-stack de um sistema de gerenciamento de estoque, composto por um backend em Java e um frontend com HTML, CSS e JavaScript.

## ⚙️ Tecnologias Utilizadas

- **Backend**: Java 17, Maven
- **Frontend**: HTML, CSS, JavaScript
- **Testes**: Cypress
- **CI/CD**: GitHub Actions

---

## 📋 Pré-requisitos

Antes de começar, certifique-se de que você tem as seguintes ferramentas instaladas em sua máquina:

- **Java Development Kit (JDK) v17** ou superior.
- **Apache Maven** para gerenciar as dependências e o build do backend.
- **Node.js v18** ou superior, que inclui o `npm` (Node Package Manager).

---

## 🚀 Como Executar o Projeto

Siga os passos abaixo para executar o projeto completo localmente.

### 1. Backend (Servidor Java)

O backend é responsável por toda a lógica de negócio e persistência dos dados.

```bash
# 1. Navegue até a pasta do backend
cd back

# 2. Compile o projeto e gere o arquivo .jar com o Maven.
# Este comando irá baixar as dependências e empacotar a aplicação.
mvn -B package

# 3. Após a compilação, o arquivo .jar estará na pasta 'target'.
# Execute a aplicação (substitua 'nome-do-arquivo.jar' pelo nome gerado).
java -jar target/nome-do-arquivo.jar
```

Após esses passos, o servidor backend estará rodando, pronto para receber requisições.

### 2. Frontend (Interface do Usuário)

O frontend é a interface com a qual o usuário interage no navegador.

```bash
# 1. Em um novo terminal, na raiz do projeto, instale as dependências do frontend (Cypress, etc).
npm install

# 2. Abra o arquivo `index.html` (ou o arquivo HTML principal) em seu navegador preferido.
```

A interface do usuário se comunicará com o backend que você iniciou no passo anterior.

---

## 🧪 Como Rodar os Testes

O projeto utiliza Cypress para testes de ponta a ponta (E2E) no frontend.

```bash
# Para abrir a interface do Cypress e rodar os testes manualmente:
npm run cy:open

# Para rodar todos os testes em modo headless (via linha de comando, como no CI):
npm run cy:run
```