/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

import Reportes.Token;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author alejandro
 */
public class VentanaTraduccion extends JFrame{
    
    private JTextArea textArea;

    public VentanaTraduccion(List<Token> tokensEncontrados, String lenguaje) {
        setTitle("Resultados de Análisis - " + lenguaje);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        textArea = new JTextArea();
        textArea.setEditable(false); // No permitir la edición del texto
        JScrollPane scrollPane = new JScrollPane(textArea); // Permitir scroll
        
        // Mostrar los tokens encontrados
        mostrarTokens(tokensEncontrados, lenguaje);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void mostrarTokens(List<Token> tokensEncontrados, String lenguaje) {
        StringBuilder builder = new StringBuilder();
        builder.append("Lenguaje: ").append(lenguaje).append("\n\n");
        
        for (Token token : tokensEncontrados) {
            builder.append("Token: ").append(token.getLexema())
                   .append(", Tipo: ").append(token.getTipo())
                   .append(", Fila: ").append(token.getFila())
                   .append(", Columna: ").append(token.getColumna())
                   .append("\n");
        }
        
        textArea.setText(builder.toString());
    }


}
