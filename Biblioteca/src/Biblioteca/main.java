package Biblioteca;

import java.io.IOException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();

        int opcao=0;

        do {
            System.out.println("---------BIBLIOTECA-----------");
            System.out.println("------------Menu-----------");
            System.out.println("\n    Selecione uma opção! \n");
            System.out.println("1. Listar livros disponíveis \n2. Listar livros emprestados \n3. Consultar Disponibilidade \n4. Cadastrar Livro  "
                    + "\n5. Devolver Livro \n6. Requisitar um livro \n7. Sair");
            if (scanner.hasNextInt()) {
            opcao = scanner.nextInt();
            
            scanner.nextLine();
            } else {
            	System.out.println("Opção inválida. Por favor, insira um número.");
            	scanner.next();
            	continue;
            	}

            switch (opcao) {
            
            	
                case 1:
                	Biblioteca.listarDisponiveis();
                    break;
                case 2:
                	Biblioteca.listarEmprestados();
                    break;
                case 3:
                   Biblioteca.pesquisarLivro();
                    break;
                case 4:
                	Biblioteca.cadastrarLivro();
                    break;
                case 5:
                   
                    break;
                case 6:
                	Biblioteca.requisitarLivro();
                    break;
                case 7:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção Inválida!");
                    break;
            }

        } while (opcao != 7);

        scanner.close();
    }
}
