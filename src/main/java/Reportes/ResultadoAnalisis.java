package Reportes;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author alejandro
 */
public class ResultadoAnalisis {
    
    private final String mensajeError; // Mensaje de error, si existe
    private final List<Token> tokensHTML; // Lista de tokens HTML
    private final List<Token> tokensCss; // Lista de tokens CSS
    private final List<Token> tokensJs; // Lista de tokens JS
    private final List<Token> todosTokens; // Lista de todos los tokens encontrados

    /**
     * Constructor que recibe las listas de tokens y un mensaje de error.
     * 
     * @param tokensHTML Lista de tokens HTML.
     * @param tokensCss Lista de tokens CSS.
     * @param tokensJs Lista de tokens JS.
     * @param mensajeError Mensaje de error, si existe.
     */
    public ResultadoAnalisis(List<Token> tokensHTML, List<Token> tokensCss, List<Token> tokensJs, String mensajeError) {
        this.tokensHTML = tokensHTML;
        this.tokensCss = tokensCss;
        this.tokensJs = tokensJs;
        this.mensajeError = mensajeError; // Asignar mensaje de error
        this.todosTokens = new ArrayList<>(); // Inicializar la lista de todos los tokens
        
        // Agregar todos los tokens a la lista combinada
        todosTokens.addAll(tokensHTML);
        todosTokens.addAll(tokensCss);
        todosTokens.addAll(tokensJs);
    }

    /**
     * Método para obtener el mensaje de error si existe.
     * 
     * @return Mensaje de error, o null si no existe.
     */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * Método para obtener los tokens HTML.
     * 
     * @return Lista de tokens HTML.
     */
    public List<Token> getTokensHTML() {
        return tokensHTML;
    }

    /**
     * Método para obtener los tokens CSS.
     * 
     * @return Lista de tokens CSS.
     */
    public List<Token> getTokensCss() {
        return tokensCss;
    }

    /**
     * Método para obtener los tokens JS.
     * 
     * @return Lista de tokens JS.
     */
    public List<Token> getTokensJs() {
        return tokensJs;
    }

    /**
     * Método para obtener todos los tokens encontrados.
     * 
     * @return Lista de todos los tokens.
     */
    public List<Token> getTodosTokens() {
        return todosTokens;
    }
}
