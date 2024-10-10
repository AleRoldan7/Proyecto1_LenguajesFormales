package Interfaz;

import Reportes.Token;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * @author alejandro
 */
public class VentanaTraduccion extends JFrame {
    
    private JTextArea textAreaTokens;
    private JTextArea textAreaCodigoOptimizado;
    private JTextArea textAreaErrores; // Área para mostrar los errores

    public VentanaTraduccion(List<Token> tokensEncontrados, String lenguaje, String codigoFuente) {
        setTitle("Resultados de Análisis - " + lenguaje);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new GridLayout(3, 1)); // Ajustamos para 3 secciones

        // Área de texto para los tokens
        textAreaTokens = new JTextArea();
        textAreaTokens.setEditable(false);
        JScrollPane scrollPaneTokens = new JScrollPane(textAreaTokens);

        // Área para mostrar el código optimizado
        textAreaCodigoOptimizado = new JTextArea();
        textAreaCodigoOptimizado.setEditable(false);
        textAreaCodigoOptimizado.setFont(new Font("Courier", Font.PLAIN, 12)); // Fuente monoespaciada
        JScrollPane scrollPaneCodigoOptimizado = new JScrollPane(textAreaCodigoOptimizado);

        // Área para mostrar los errores
        textAreaErrores = new JTextArea();
        textAreaErrores.setEditable(false);
        textAreaErrores.setFont(new Font("Courier", Font.PLAIN, 12)); // Fuente monoespaciada
        JScrollPane scrollPaneErrores = new JScrollPane(textAreaErrores);

        // Mostrar los tokens encontrados
        mostrarTokens(tokensEncontrados, lenguaje);

        // Mostrar el código optimizado
        String codigoOptimizado = optimizarCodigo(codigoFuente);
        mostrarCodigoOptimizado(codigoOptimizado);

        // Añadir los componentes al panel principal
        panelPrincipal.add(scrollPaneTokens);
        panelPrincipal.add(scrollPaneCodigoOptimizado); // Sección de código optimizado
        panelPrincipal.add(scrollPaneErrores); // Sección para errores

        add(panelPrincipal, BorderLayout.CENTER);
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
        
        textAreaTokens.setText(builder.toString());
    }

    private void mostrarCodigoOptimizado(String codigoOptimizado) {
        // Asegúrate de que los saltos de línea se mantengan
        textAreaCodigoOptimizado.setText(codigoOptimizado);
    }

    // Método para optimizar el código
    private String optimizarCodigo(String codigoFuente) {
        StringBuilder codigoOptimizado = new StringBuilder();
        String[] lineas = codigoFuente.split("\n");

        for (String linea : lineas) {
            // Eliminar líneas vacías
            if (linea.trim().isEmpty()) {
                continue;
            }

            // Eliminar comentarios
            if (linea.trim().startsWith("//")) {
                continue;
            }

            // Eliminar líneas que contienen solo saltos de línea
            if (linea.trim().length() == 0) {
                continue;
            }

            // Añadir la línea optimizada
            codigoOptimizado.append(linea).append("\n");
        }

        return codigoOptimizado.toString();
    }

    // Método para mostrar errores
    public void mostrarErrores(List<String> errores) {
        StringBuilder builder = new StringBuilder();
        if (errores.isEmpty()) {
            builder.append("No se encontraron errores.\n");
        } else {
            builder.append("Errores encontrados:\n\n");
            for (String error : errores) {
                builder.append(error).append("\n");
            }
        }
        textAreaErrores.setText(builder.toString());
    }
}
