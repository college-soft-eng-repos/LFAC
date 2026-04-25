package main.java.com.projeto.core.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Gerenciador centralizado de operações com arquivos
 * Responsável por: leitura, escrita, validação e construção de caminhos
 */
public class GerenciadorArquivos {
    private String resourcesPath;

    public GerenciadorArquivos() {
        this.resourcesPath = obterResourcesPath();
    }

    public GerenciadorArquivos(String resourcesPath) {
        this.resourcesPath = resourcesPath;
    }

    public String getCaminhoEnt() {
        return resourcesPath + "/arquivo.ent";
    }

    public String getCaminhoPpr() {
        return getCaminhoEnt().replace(".ent", ".ppr");
    }

    public String getCaminhoTks() {
        return getCaminhoEnt().replace(".ent", ".tks");
    }

    public String getCaminhoErr() {
        return getCaminhoEnt().replace(".ent", ".err");
    }

    public String getResourcesPath() {
        return resourcesPath;
    }

    public void setResourcesPath(String resourcesPath) {
        this.resourcesPath = resourcesPath;
    }

    /**
     * Obtém o caminho de recursos - variável de ambiente ou falback padrão
     */
    private static String obterResourcesPath() {
        String path = System.getenv("LFAC_RESOURCES_PATH");
        if (path == null || path.trim().isEmpty()) {
            // Usar caminho absoluto baseado no diretório do projeto
            String projectDir = System.getProperty("user.dir");
            path = projectDir + "/analisador-lexico/resources";
        }
        return path;
    }

    /**
     * Lê o conteúdo de um arquivo
     * 
     * @param caminho caminho do arquivo
     * @return conteúdo do arquivo
     * @throws IOException se houver erro na leitura
     */
    public String lerArquivo(String caminho) throws IOException {
        validarArquivoExiste(caminho);
        return Files.readString(Paths.get(caminho));
    }

    /**
     * Escreve conteúdo em um arquivo
     * 
     * @param caminho  caminho do arquivo de destino
     * @param conteudo conteúdo a escrever
     * @throws IOException se houver erro na escrita
     */
    public void escreverArquivo(String caminho, String conteudo) throws IOException {
        Files.writeString(Paths.get(caminho), conteudo);
    }

    /**
     * Valida se um arquivo existe
     * 
     * @param caminho caminho do arquivo
     * @throws IOException se o arquivo não existir
     */
    private void validarArquivoExiste(String caminho) throws IOException {
        Path path = Paths.get(caminho);
        if (!Files.exists(path)) {
            throw new IOException("Arquivo não encontrado: " + caminho);
        }
    }
}