describe('Testes de Sistema - Gerenciador de Estoque', () => {
  
  // Antes de cada teste, o robô visita o site
  beforeEach(() => {
    // IMPORTANTE: Troque 5500 pela porta que seu Live Server estiver usando!
    cy.visit('http://127.0.0.1:5500/index.html'); 
  });

  it('Deve adicionar um novo produto com sucesso', () => {
    // 1. Digitar o nome do produto
    cy.get('#product').type('Monitor Ultrawide');

    // 2. Digitar a quantidade
    cy.get('#quantity').type('5');

    // 3. Clicar no botão adicionar
    cy.get('#submit-product').click();

    // 4. Validar (Assert): O Cypress verifica se o texto apareceu na tabela
    cy.get('#table-body').should('contain', 'Monitor Ultrawide');
    cy.get('#table-body').should('contain', '5');
  });

  it('Deve validar campos vazios (não deve adicionar)', () => {
    // Clica sem digitar nada
    cy.get('#submit-product').click();

    // Verifica se o input ganhou o foco (significa que o JS bloqueou)
    cy.get('#product').should('be.focused');
  });
});