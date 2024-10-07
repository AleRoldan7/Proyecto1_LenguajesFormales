package AnalizadorGeneral;

import AnalizadorCSS.IdentificadorCSS;
import AnalizadorHTML.IdentificadorHTML;
import AnalizadorJS.IdentificadorJS;
import Interfaz.VentanaTraduccion;
import Reportes.ResultadoAnalisis;
import Reportes.Token;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alejandro
 */
public class AnalizarGeneral {
    
    private IdentificadorHTML identificadorHTML = new IdentificadorHTML();
    private IdentificadorCSS identificadorCSS = new IdentificadorCSS();
    private IdentificadorJS identificadorJS = new IdentificadorJS(); 

    public ResultadoAnalisis analizarTexto(String texto) {
        List<Token> tokensEncontrados = new ArrayList<>();
        String lenguajeActual = "";
        StringBuilder cadena = new StringBuilder(); 
        int fila = 1;  // Contador de líneas

        int i = 0;

        while (i < texto.length()) {
            char actual = texto.charAt(i);

            // Detectar el inicio de un nuevo lenguaje
            if (actual == '>' && (i + 1) < texto.length() && texto.charAt(i + 1) == '>') {
                i += 2;
                StringBuilder lenguajeBuilder = new StringBuilder(); 
                while (i < texto.length() && texto.charAt(i) != '\n') {
                    lenguajeBuilder.append(texto.charAt(i));
                    i++;
                }
                lenguajeActual = lenguajeBuilder.toString().trim(); // Establecer el lenguaje actual
                continue; 
            }

            // Procesar la línea si hay un lenguaje actual
            if (!lenguajeActual.isEmpty()) {
                if (actual == '\n') { 
                    List<Token> tokens = procesarLineaSegunLenguaje(lenguajeActual, cadena.toString(), fila);
                    tokensEncontrados.addAll(tokens);
                    cadena.setLength(0); 
                    fila++; // Incrementar la fila después de cada salto de línea
                } else {
                    cadena.append(actual); 
                }
            }
            i++;
        }

        // Procesar la última línea si queda texto
        if (cadena.length() > 0 && !lenguajeActual.isEmpty()) {
            List<Token> tokens = procesarLineaSegunLenguaje(lenguajeActual, cadena.toString(), fila);
            tokensEncontrados.addAll(tokens);
        }

        mostrarResultados(tokensEncontrados);
        return new ResultadoAnalisis(tokensEncontrados, null); // Aquí puedes agregar un mensaje de error si es necesario
    }

    private List<Token> procesarLineaSegunLenguaje(String lenguaje, String linea, int fila) {
        List<Token> tokens = new ArrayList<>(); 
        switch (lenguaje) {
            case "[html]":
                tokens = identificadorHTML.obtenerTokensValidos(linea); 
                break;
            case "[js]":
                tokens = identificadorJS.obtenerTokensValidos(linea, fila);
                break;
            case "[css]":
                tokens = identificadorCSS.obtenerTokensValidos(linea, fila); 
                break;
            default:
                System.out.println("Lenguaje desconocido: " + lenguaje);
                break;
        }
        return tokens;
    }

    // Agrupar tokens por lenguaje y mostrar los resultados en la ventana
    public void mostrarResultados(List<Token> tokensEncontrados) {
        // Crear una ventana para mostrar los tokens
        VentanaTraduccion ventana = new VentanaTraduccion(tokensEncontrados, "Resultados");
        ventana.setVisible(true); // Mostrar la ventana
    }
}
