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

            // Comprobar si el lenguaje actual es válido
            if (!lenguajeActual.isEmpty() && !esLenguajeValido(lenguajeActual)) {
                System.out.println("Error: Lenguaje desconocido o inválido: " + lenguajeActual);
                return new ResultadoAnalisis(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "Error: Lenguaje desconocido o inválido.");
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

        // Agregar etiquetas de estado al reporte
        agregarEtiquetasEstado(tokensHTML, tokensCSS, tokensJS);

        return new ResultadoAnalisis(tokensHTML, tokensCSS, tokensJS, "Resultado"); 
    }

    // Método para agregar etiquetas de estado
    private void agregarEtiquetasEstado(List<Token> tokensHTML, List<Token> tokensCSS, List<Token> tokensJS) {
        if (!tokensHTML.isEmpty()) {
            tokensHTML.add(new Token("[html]", "html", "HTML","Etiqueta de estado", -1, -1));
        }
        if (!tokensCSS.isEmpty()) {
            tokensCSS.add(new Token("[css]", "css", "CSS", "Etiqueta de estado", -1, -1));
        }
        if (!tokensJS.isEmpty()) {
            tokensJS.add(new Token("[js]", "js", "JS", "Etiqueta de estado", -1, -1));
        }
    }


    // Método para verificar si el lenguaje es válido
    private boolean esLenguajeValido(String lenguaje) {
        return "[html]".equals(lenguaje) || "[css]".equals(lenguaje) || "[js]".equals(lenguaje);
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
              
                tokens = identificadorHTML.obtenerTokensValidos(linea);
                break;
            case "[js]":
                System.out.println("Procesando JS: " + linea); // Debug
                tokens = identificadorJS.obtenerTokensValidosJS(linea);
                break;
            case "[css]":
                System.out.println("Procesando CSS: " + linea); // Debug
                tokens = identificadorCSS.analizarLinea(linea);
                break;
            default:
                System.out.println("Lenguaje desconocido: " + lenguaje);
                break;
        }
        return tokens;
    }
    
    /**
    * Optimiza el código eliminando comentarios y líneas que contienen tokens.
    * @param texto El texto a optimizar.
    * @return El texto optimizado.
    */
    public String optimizarCodigo(String texto) {
       StringBuilder codigoOptimizado = new StringBuilder();
       boolean dentroDeComentarioSimple = false;
       boolean dentroDeComentarioMultiple = false;
       boolean dentroDeLineaConContenido = false;

       for (int i = 0; i < texto.length(); i++) {
           char actual = texto.charAt(i);

           // Detección de comentarios de una línea "//"
           if (!dentroDeComentarioMultiple && actual == '/' && i + 1 < texto.length() && texto.charAt(i + 1) == '/') {
               dentroDeComentarioSimple = true;
           }

           // Detección de comentarios de múltiples líneas "/*"
           if (!dentroDeComentarioSimple && actual == '/' && i + 1 < texto.length() && texto.charAt(i + 1) == '*') {
               dentroDeComentarioMultiple = true;
           }

           // Finalización de comentarios múltiples "*/"
           if (dentroDeComentarioMultiple && actual == '*' && i + 1 < texto.length() && texto.charAt(i + 1) == '/') {
               dentroDeComentarioMultiple = false;
               i++;
               continue;
           }

           // Ignorar el contenido de un comentario simple hasta el final de la línea
           if (dentroDeComentarioSimple && actual == '\n') {
               dentroDeComentarioSimple = false;
               continue;
           }

           // Si estamos dentro de un comentario, ignorar el contenido
           if (dentroDeComentarioSimple || dentroDeComentarioMultiple) {
               continue;
           }

           // Detección de líneas vacías
           if (actual == '\n') {
               // Si hay contenido en la línea, no agregarla al resultado
               if (dentroDeLineaConContenido) {
                   dentroDeLineaConContenido = false; // Reiniciar para la próxima línea
               }
               continue; // Ignorar salto de línea
           }

           // Si encuentra un carácter que no sea espacio en blanco, la línea tiene contenido
           if (!Character.isWhitespace(actual)) {
               dentroDeLineaConContenido = true;
           }

           // Agregar carácter al código optimizado si no se está en una línea que debe ser eliminada
           if (dentroDeLineaConContenido) {
               codigoOptimizado.append(actual);
           }
       }

       return codigoOptimizado.toString().trim();
    }


    public void mostrarResultados(String texto) {
        // Obtener los tokens procesados
        ResultadoAnalisis resultado = analizarTexto(texto);
        List<Token> tokensHTML = resultado.getTokensHTML();
        List<Token> tokensCSS = resultado.getTokensCss();
        List<Token> tokensJS = resultado.getTokensJs();

        // Optimizar el código
        String textoOptimizado = optimizarCodigo(texto);

        // Generar archivo HTML
        GeneradorHTML generadorHTML = new GeneradorHTML();
        String rutaArchivo = "resultadoAnalisis.html"; // Puedes cambiar la ruta si lo deseas
        generadorHTML.generarArchivoHTML(tokensHTML, tokensCSS, tokensJS, rutaArchivo);

        // Mostrar la ventana de resultados si hay tokens
        if (!tokensHTML.isEmpty() || !tokensCSS.isEmpty() || !tokensJS.isEmpty()) {
            VentanaTraduccion ventana = new VentanaTraduccion(tokensHTML, "Resultados", textoOptimizado);
            ventana.setVisible(true);
        }
    }


    
    

}
