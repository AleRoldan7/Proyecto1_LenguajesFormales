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
    
    private final IdentificadorHTML identificadorHTML;
    private final IdentificadorCSS identificadorCSS;
    private final IdentificadorJS identificadorJS; 

    public AnalizarGeneral() {
        this.identificadorHTML = new IdentificadorHTML();
        this.identificadorCSS = new IdentificadorCSS();
        this.identificadorJS = new IdentificadorJS(); 
    }

    /**
     * Método para analizar el texto ingresado y obtener los tokens correspondientes.
     * @param texto El texto a analizar.
     * @return Un objeto ResultadoAnalisis que contiene listas de tokens encontrados.
     */
    public ResultadoAnalisis analizarTexto(String texto) {
        List<Token> tokensHTML = new ArrayList<>(); 
        List<Token> tokensCSS = new ArrayList<>(); 
        List<Token> tokensJS = new ArrayList<>(); 
        
        String lenguajeActual = "";
        StringBuilder cadena = new StringBuilder(); 
        int fila = 1;  // Contador de líneas

        for (int i = 0; i < texto.length(); i++) {
            char actual = texto.charAt(i);

            // Detectar el inicio de un nuevo bloque de lenguaje
            if (actual == '>' && (i + 1) < texto.length() && texto.charAt(i + 1) == '>') {
                // Avanzar dos caracteres
                i += 2; 
                lenguajeActual = obtenerLenguaje(texto, i); 
                i += lenguajeActual.length(); 
                continue; 
            }

            // Procesar líneas de texto según el lenguaje actual
            if (!lenguajeActual.isEmpty()) {
                if (actual == '\n') { 
                    // Procesar la línea completa si hay texto
                    if (cadena.length() > 0) {
                        List<Token> tokens = procesarLineaSegunLenguaje(lenguajeActual, cadena.toString(), fila);
                        agregarTokens(tokensHTML, tokensCSS, tokensJS, tokens, lenguajeActual);
                    }
                    // Reiniciar cadena y avanzar la línea
                    cadena.setLength(0); 
                    fila++;
                } else {
                    // Acumular caracteres en la cadena
                    cadena.append(actual); 
                }
            }
        }

        // Procesar la última línea si hay texto
        if (cadena.length() > 0 && !lenguajeActual.isEmpty()) {
            List<Token> tokens = procesarLineaSegunLenguaje(lenguajeActual, cadena.toString(), fila);
            agregarTokens(tokensHTML, tokensCSS, tokensJS, tokens, lenguajeActual);
        }

        return new ResultadoAnalisis(tokensHTML, tokensCSS, tokensJS, "Resultado"); 
    }

    private String obtenerLenguaje(String texto, int index) {
        StringBuilder lenguajeBuilder = new StringBuilder();
        while (index < texto.length() && texto.charAt(index) != '\n') {
            lenguajeBuilder.append(texto.charAt(index));
            index++;
        }
        String lenguaje = lenguajeBuilder.toString().trim(); 
        System.out.println("Lenguaje detectado: " + lenguaje); // Debug
        return lenguaje;
    }

    private void agregarTokens(List<Token> tokensHTML, List<Token> tokensCSS, List<Token> tokensJS, List<Token> tokens, String lenguaje) {
        switch (lenguaje) {
            case "[html]":
                tokensHTML.addAll(tokens);
                break;
            case "[js]":
                tokensJS.addAll(tokens);
                break;
            case "[css]":
                tokensCSS.addAll(tokens);
                break;
            default:
                System.out.println("Lenguaje desconocido: " + lenguaje);
                break;
        }
    }

    private List<Token> procesarLineaSegunLenguaje(String lenguaje, String linea, int fila) {
        List<Token> tokens = new ArrayList<>(); 
        switch (lenguaje) {
            case "[html]":
                System.out.println("Procesando HTML: " + linea); // Debug
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

    /**
     * Muestra los resultados en una ventana.
     * @param texto El texto ingresado para analizar.
     */
    public void mostrarResultados(String texto) {
        // Obtener los tokens procesados
        ResultadoAnalisis resultado = analizarTexto(texto);
        List<Token> todosTokens = new ArrayList<>();
        todosTokens.addAll(resultado.getTokensHTML());
        todosTokens.addAll(resultado.getTokensCss());
        todosTokens.addAll(resultado.getTokensJs());
        
        // Verificar si hay tokens encontrados
        if (!todosTokens.isEmpty()) {
            VentanaTraduccion ventana = new VentanaTraduccion(todosTokens, "Resultados");
            ventana.setVisible(true);
        } else {
            // Mostrar un mensaje si no se encontraron tokens
            VentanaTraduccion ventanaVacia = new VentanaTraduccion(new ArrayList<>(), "Sin Resultados");
          
            ventanaVacia.setVisible(true);
        }
    }
}
