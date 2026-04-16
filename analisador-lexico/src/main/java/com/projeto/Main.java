package main.java.com.projeto;

import main.java.com.projeto.core.PreProcessador;
import java.io.IOException;

public class Main {
 
  public static void main(String[] args){
    try {
      // caminho do arquivo de entrada
      String caminhoArquivoEntrada = "/home/gabriella/Documentos/College/Subjects/01.2026/LFAC/analisador-lexico/resources/arquivo.fat";

      // Processar o arquivo e gerar o de saída
      PreProcessador.processarArquivo(caminhoArquivoEntrada);

    } catch (IOException e) {
      System.err.println("Erro ao processar o arquivo: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
