package Biblioteca;
import java.io.*;
import java.util.*;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Biblioteca {
	
	//REQUISITAR LIVRO-----------------------------------------------------------
	static void requisitarLivro() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o título ou uma parte do título do livro:");
        String busca = scanner.nextLine();
        
        List<String[]> livrosEncontrados = new ArrayList<>();
        int count = 1;
        
        try (BufferedReader br = new BufferedReader(new FileReader("livros.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String titulo = parts[0];
                if (titulo.toLowerCase().contains(busca.toLowerCase()) && parts[3].equals("1")) {
                    livrosEncontrados.add(parts);
                    System.out.println(count + ". Título: " + titulo);
                    System.out.println("   Autor: " + parts[1]);
                    System.out.println("   Gênero: " + parts[2]);
                    System.out.println("------------------------");
                    count++;
                }
            }
            
            if (livrosEncontrados.isEmpty()) {
                System.out.println("Nenhum livro disponível encontrado.");
                return;
            }
            
            System.out.println("Selecione o número do livro desejado:");
            int escolha = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada
            
            if (escolha < 1 || escolha > livrosEncontrados.size()) {
                System.out.println("Opção inválida.");
                return;
            }
            
            String[] livroSelecionado = livrosEncontrados.get(escolha - 1);
            livroSelecionado[3] = "0"; // Alterando a disponibilidade para emprestado
            
            // Atualizar o arquivo com a lista de livros atualizada
            atualizarArquivo(livrosEncontrados);
            
            System.out.println("Livro requisitado com sucesso.");
            
        } catch (IOException e) {
            System.err.println("Erro ao requisitar livro: " + e.getMessage());
        }
    }
    
    private static void atualizarArquivo(List<String[]> livros) {
        File arquivoOriginal = new File("livros.txt");
        File arquivoTemporario = new File("livros.tmp");
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivoTemporario))) {
            for (String[] livro : livros) {
                pw.println(String.join(",", livro));
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo temporário: " + e.getMessage());
            return;
        }
        
        // Verificar se o arquivo original existe
        if (!arquivoOriginal.exists()) {
            System.err.println("O arquivo original não existe.");
            return;
        }
        
        // Fechar todos os recursos relacionados ao arquivo original
        System.gc(); // Solicitar a execução do coletor de lixo para liberar recursos relacionados ao arquivo original
        try {
            Thread.sleep(100); // Aguardar 100 milissegundos para garantir que todos os recursos tenham sido liberados
        } catch (InterruptedException e) {
            System.err.println("Erro ao aguardar antes de excluir o arquivo original: " + e.getMessage());
        }
        
        // Verificar se conseguimos excluir o arquivo original
        if (!arquivoOriginal.delete()) {
            System.err.println("Não foi possível excluir o arquivo original.");
            return;
        }
        
        // Renomear o arquivo temporário para substituir o arquivo original
        if (!arquivoTemporario.renameTo(arquivoOriginal)) {
            System.err.println("Erro ao renomear arquivo temporário.");
        }
    }
    
    private static boolean substituirLivro(String[] novoLivro) {
        File arquivoTemporario = new File("livros.tmp");
        File arquivoOriginal = new File("livros.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoOriginal));
             PrintWriter pw = new PrintWriter(new FileWriter(arquivoTemporario))) {
            String line;
            boolean livroSubstituido = false;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (!Arrays.equals(parts, novoLivro)) {
                    pw.println(String.join(",", parts));
                } else {
                    livroSubstituido = true;
                }
            }
            pw.println(String.join(",", novoLivro));
            return livroSubstituido;
        } catch (IOException e) {
            System.err.println("Erro ao substituir livro: " + e.getMessage());
            e.printStackTrace(); // Adicionando o rastreamento da pilha para ajudar a identificar a causa do erro
            return false;
        } finally {
            System.out.println("Tentando excluir arquivo original: " + arquivoOriginal.delete());
            System.out.println("Tentando renomear arquivo temporário: " + arquivoTemporario.renameTo(arquivoOriginal));
        }
    }
	//PESQUISAR LIVRO------------------------------------------------------------
	static void pesquisarLivro() {
		Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o título ou uma parte do título do livro:");
        String busca = scanner.nextLine();
        System.out.println("Resultados da pesquisa para: " + busca);
        try (BufferedReader br = new BufferedReader(new FileReader("livros.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String titulo = parts[0];
                if (titulo.toLowerCase().contains(busca.toLowerCase())) { // Verifica se o título contém a busca
                    System.out.println("Título: " + titulo);
                    System.out.println("Autor: " + parts[1]);
                    System.out.println("Gênero: " + parts[2]);
                    String disponibilidade = parts[3].equals("1") ? "Disponível" : "Emprestado";
                    System.out.println("Disponibilidade: " + disponibilidade);
                    System.out.println("------------------------");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao pesquisar livro: " + e.getMessage());
        }
    }
	//SALVAR LIVROS--------------------------------------------------------------
	static void salvarLivro(String titulo, String autor, String genero) {
        try (FileWriter fw = new FileWriter("livros.txt", true);
             PrintWriter pw = new PrintWriter(fw)) {
            // Formato de salvar: título, autor, gênero, disponibilidade (1 para disponível, 0 para emprestado)
            pw.println(titulo + "," + autor + "," + genero + ",1");
        } catch (IOException e) {
            System.err.println("Erro ao salvar o livro: " + e.getMessage());
        }
    
}
	//CADASTRAR LIVROS-----------------------------------------------------------
	static void cadastrarLivro() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o título do livro:");
        String titulo = scanner.nextLine();
        System.out.println("Digite o autor do livro:");
        String autor = scanner.nextLine();
        System.out.println("Digite o gênero do livro:");
        String genero = scanner.nextLine();
        
        salvarLivro(titulo, autor, genero);
        
        System.out.println("Livro cadastrado com sucesso!");
    }
	//LISTAR LIVROS DISPONIVEIS--------------------------------------------------
	static void listarDisponiveis() {
        System.out.println("Livros Disponíveis:");
        try (BufferedReader br = new BufferedReader(new FileReader("livros.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equals("1")) { // Verifica se o livro está disponível
                    System.out.println("Título: " + parts[0]);
                    System.out.println("Autor: " + parts[1]);
                    System.out.println("Gênero: " + parts[2]);
                    System.out.println("------------------------");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao listar livros disponíveis: " + e.getMessage());
        }
    }
	//LISTAR LIVROS EMPRESTADOS--------------------------------------------------
	static void listarEmprestados() {
        System.out.println("Livros Emprestados:");
        try (BufferedReader br = new BufferedReader(new FileReader("livros.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equals("0")) { // Verifica se o livro está emprestado
                    System.out.println("Título: " + parts[0]);
                    System.out.println("Autor: " + parts[1]);
                    System.out.println("Gênero: " + parts[2]);
                    System.out.println("------------------------");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao listar livros emprestados: " + e.getMessage());
        }
    }
}
