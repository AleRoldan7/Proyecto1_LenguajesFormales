/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reportes;

import java.util.List;

/**
 *
 * @author alejandro
 */
public class ResultadoAnalisis {
    
    private List<Token> tokensEncontrados;
    private String mensajeError;

    public ResultadoAnalisis(List<Token> tokensEncontrados, String mensajeError) {
        this.tokensEncontrados = tokensEncontrados;
        this.mensajeError = mensajeError;
    }

    // Método para obtener los tokens encontrados
    public List<Token> getTokens() {
        return tokensEncontrados;
    }

    // Método para obtener el mensaje de error si existe
    public String getMensajeError() {
        return mensajeError;
    }
}
