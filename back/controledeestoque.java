import java.util.Scanner;

public class ControleEstoque {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Produto[] produtos = new Produto[10];

        for (int i = 0; i < 10; i++) {
            produtos[i] = new Produto();
            produtos[i].setId(i + 1);
            produtos[i].setNome("Produto " + (i + 1));
            produtos[i].setQuantidade(0);
        }

        while (true) {
            System.out.println("\nControle de Estoque");
            System.out.println("1. Listar produtos");
            System.out.println("2. Adicionar produto");
            System.out.println("3. Remover produto");
            System.out.println("4. Atualizar produto");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1:
                    listarProdutos(produtos);
                    break;
                case 2:
                    adicionarProduto(produtos, scanner);
                    break;
                case 3:
                    removerProduto(produtos, scanner);
                    break;
                case 4:
                    atualizarProduto(produtos, scanner);
                    break;
                case 5:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public static void listarProdutos(Produto[] produtos) {
        System.out.println("Produtos:");
        for (Produto p : produtos) {
            if (p.getQuantidade() > 0) {
                System.out.println(p.getId() + ". " + p.getNome() + " - Quantidade: " + p.getQuantidade());
            }
        }
    }

    public static void adicionarProduto(Produto[] produtos, Scanner scanner) {
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Digite a quantidade do produto: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine(); 

        for (Produto p : produtos) {
            if (p.getQuantidade() == 0) {
                p.setNome(nome);
                p.setQuantidade(quantidade);
                System.out.println("Produto adicionado com sucesso!");
                return;
            }
        }

        System.out.println("Não há espaço para adicionar mais produtos.");
    }

    public static void removerProduto(Produto[] produtos, Scanner scanner) {
        System.out.print("Digite o ID do produto que deseja remover: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        for (Produto p : produtos) {
            if (p.getId() == id) {
                p.setQuantidade(0);
                System.out.println("Produto removido com sucesso!");
                return;
            }
        }

        System.out.println("Produto não encontrado.");
    }

    public static void atualizarProduto(Produto[] produtos, Scanner scanner) {
        System.out.print("Digite o ID do produto que deseja atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        for (Produto p : produtos) {
            if (p.getId() == id) {
                System.out.print("Digite o novo nome do produto: ");
                String nome = scanner.nextLine();
                System.out.print("Digite a nova quantidade do produto: ");
                int quantidade = scanner.nextInt();
                scanner.nextLine(); 
                p.setNome(nome);
                p.setQuantidade(quantidade);
                System.out.println("Produto atualizado com sucesso!");
                return;
            }
        }

        System.out.println("Produto não encontrado.");
    }
}

class Produto {
    private int id;
    private String nome;
    private int quantidade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}