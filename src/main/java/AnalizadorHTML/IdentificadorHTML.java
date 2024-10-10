package AnalizadorHTML;

import Reportes.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdentificadorHTML {

    private String[] palabraReservada = {
        "class", "=", "href", "onClick", "id", "style", "type", "placeholder", "required", "name"
    };
    private TraductorEtiquetas traductor = new TraductorEtiquetas();
    private List<TraductorEtiquetas> etiNormales = new ArrayList<>();
    private List<Token> tokenEncontrado = new ArrayList<>();

    public String analizarHTML(String cadena) {
        StringBuilder resultado = new StringBuilder();

        // Detectar y procesar el token de estado >>[html]
        if (cadena.contains(">>[html]")) {
            agregarToken(">>[html]", ">>\\[html\\]", "html", "Token de Estado", 0, 0);
            resultado.append("Token de estado detectado: >>[html]\n");
        }

        // Verificar si es un HTML válido
        if (esHTMLValido(cadena)) {
            resultado.append("Lenguaje HTML detectado\n");
            // Aquí llamamos al método que analiza las etiquetas
            List<Token> tokens = analizarEtiquetas(cadena);
            for (Token token : tokens) {
                resultado.append("Token encontrado: ").append(token.getLexema()).append("\n");
            }

            resultado.append(validarAtributos(cadena));
            resultado.append(validarCadenas(cadena));

            String erroresTexto = validarTextoFueraDeEtiquetas(cadena);
            if (!erroresTexto.isEmpty()) {
                resultado.append(erroresTexto);
            }
        } else {
            resultado.append("El código no corresponde a HTML válido\n");
        }

        // Generar el reporte de tokens encontrados en la consola
        generarReporteTokens();

        return "Análisis completado."; // O cualquier mensaje que desees
    }

    private List<Token> analizarEtiquetas(String entrada) {
        List<Token> tokens = new ArrayList<>();
        List<String> etiquetas = traductor.etiquetasNormales(); // Obtiene las etiquetas válidas desde el traductor

        int i = 0; // Índice para recorrer la cadena de entrada
        while (i < entrada.length()) {
            // Verificar el token de estado antes de procesar etiquetas
            if (entrada.startsWith(">>[html]", i)) {
                tokens.add(new Token(">>[html]", ">>\\[html\\]", "html", "Token de Estado", 0, 0));
                i += 8; // Avanzamos más allá del token
                continue;
            }

            if (entrada.charAt(i) == '<') {
                int finEtiqueta = entrada.indexOf('>', i);
                if (finEtiqueta == -1) break; // No hay cierre de etiqueta

                String etiquetaCompleta = entrada.substring(i + 1, finEtiqueta).trim();
                String nombreEtiqueta;

                // Separar nombre de la etiqueta de sus atributos
                if (etiquetaCompleta.contains(" ")) {
                    nombreEtiqueta = etiquetaCompleta.substring(0, etiquetaCompleta.indexOf(" ")).trim();
                } else {
                    nombreEtiqueta = etiquetaCompleta; // No tiene atributos
                }

                // Verificar si la etiqueta es válida usando el arreglo existente
                if (etiquetas.contains(nombreEtiqueta)) {
                    // Agregar el token para la etiqueta
                    tokens.add(new Token(nombreEtiqueta, "expresión_regular_placeholder", "html", "Etiqueta Normal", 0, 0));

                    // Manejo del texto interno si existe
                    int inicioTexto = finEtiqueta + 1; // Mover después de '>'
                    int finTexto = entrada.indexOf('<', inicioTexto);
                    if (finTexto > inicioTexto) {
                        String textoInterno = entrada.substring(inicioTexto, finTexto).trim();
                        if (!textoInterno.isEmpty()) {
                            tokens.add(new Token(textoInterno, "expresión_regular_placeholder", "html", "Texto Interno", 0, 0));
                        }
                    }
                }
                i = finEtiqueta; // Mover el índice al final de la etiqueta
            } else {
                i++; // Avanzar al siguiente carácter
            }
        }

        return tokens;
    }



    private void generarReporteTokens() {
        if (!tokenEncontrado.isEmpty()) {
            System.out.println("\nReporte de Tokens Encontrados:");
            for (Token token : tokenEncontrado) {
                System.out.println("Token: " + token.getLexema()
                        + ", Tipo: " + token.getTipo()
                        + ", Lenguaje: " + token.getLenguaje()
                        + ", Fila: " + token.getFila()
                        + ", Columna: " + token.getColumna());
            }
        } else {
            System.out.println("No se encontraron tokens válidos.");
        }
    }

    public boolean esHTMLValido(String cadena) {
        Stack<String> stack = new Stack<>();
        int i = 0;

        while (i < cadena.length()) {
            if (cadena.charAt(i) == '<') {
                int finEtiqueta = cadena.indexOf('>', i);
                if (finEtiqueta == -1) {
                    return false; // Etiqueta no cerrada
                }
                String etiquetaCompleta = cadena.substring(i + 1, finEtiqueta).trim();
                String nombreEtiqueta;

                if (etiquetaCompleta.contains(" ")) {
                    nombreEtiqueta = etiquetaCompleta.substring(0, etiquetaCompleta.indexOf(" ")).trim();
                } else {
                    nombreEtiqueta = etiquetaCompleta; // No tiene atributos
                }

                if (!etiquetaCompleta.startsWith("/")) { // Es una etiqueta de apertura
                    if (traductor.etiquetasNormales().contains("<" + nombreEtiqueta + ">")) {
                        
                        
                        traductor.posiEtiquetas("<" + nombreEtiqueta + ">");
                        
                        int tmp = traductor.posiEtiquetas("<" + nombreEtiqueta + ">");
                        
                        
                        stack.push(traductor.etiquetaTraducida().get(tmp)); // Agregar etiqueta al stack
                        System.out.println("Jalando");
                        
                    } else {
                        return false; // Etiqueta no válida
                    }
                } else { // Es una etiqueta de cierre
                    System.out.println("NO ES ETIQUETA VALIDAAAAAA");
                    String etiquetaDeApertura = etiquetaCompleta.substring(1).trim(); // Extrae el nombre de la etiqueta
                    if (stack.isEmpty() || !stack.peek().equals(etiquetaDeApertura)) {
                        return false; // No hay una etiqueta de apertura correspondiente
                    }
                    stack.pop(); // Cerrar la etiqueta
                }
                i = finEtiqueta; // Mover el índice al final de la etiqueta
            }
            i++;
        }

        return stack.isEmpty(); // Asegúrate de que todas las etiquetas están cerradas
    }

    private void validarPalabrasReservadas(String etiqueta, StringBuilder resultado) {
        for (String palabra : palabraReservada) {
            if (etiqueta.contains(palabra)) {
                resultado.append("Palabra reservada encontrada: '").append(palabra).append("'\n");
            }
        }
    }

    private String validarTextoFueraDeEtiquetas(String cadena) {
        StringBuilder errores = new StringBuilder();
        boolean dentroDeEtiqueta = false;
        StringBuilder textoActual = new StringBuilder();

        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);

            if (c == '<') {
                dentroDeEtiqueta = true;
                if (textoActual.length() > 0) {
                    errores.append("Texto fuera de etiqueta detectado: ").append(textoActual).append("\n");
                    textoActual.setLength(0);
                }
            }

            if (dentroDeEtiqueta && c == '>') {
                dentroDeEtiqueta = false;
            }

            if (!dentroDeEtiqueta) {
                textoActual.append(c);
            }
        }

        if (textoActual.length() > 0) {
            errores.append("Texto fuera de etiqueta detectado al final: ").append(textoActual).append("\n");
        }

        return errores.toString();
    }

    private String validarAtributos(String entrada) {
        StringBuilder resultado = new StringBuilder();
        int i = 0;

        while (i < entrada.length()) {
            if (entrada.charAt(i) == '<' && entrada.charAt(i + 1) != '/') {
                int inicioEtiqueta = i;
                int finEtiqueta = entrada.indexOf('>', inicioEtiqueta);
                if (finEtiqueta == -1) {
                    resultado.append("Etiqueta no válida o mal cerrada\n");
                    break;
                }

                String etiqueta = entrada.substring(inicioEtiqueta, finEtiqueta + 1);
                resultado.append("Etiqueta de apertura encontrada: ").append(etiqueta).append("\n");

                // Validar palabras reservadas en la etiqueta
                validarPalabrasReservadas(etiqueta, resultado);

                // Validar cadenas entre comillas
                String cadenaResultado = validarCadenas(etiqueta);
                if (!cadenaResultado.isEmpty()) {
                    resultado.append(cadenaResultado);
                }

                i = finEtiqueta;
            }
            i++;
        }
        return resultado.toString();
    }

    private String validarCadenas(String entrada) {
        StringBuilder resultado = new StringBuilder();
        int i = 0;

        while (i < entrada.length()) {
            char actual = entrada.charAt(i);

            if (actual == '"' || actual == '\'') {
                char delimitador = actual;
                i++;
                StringBuilder cadenaActual = new StringBuilder();

                while (i < entrada.length() && entrada.charAt(i) != delimitador) {
                    cadenaActual.append(entrada.charAt(i));
                    i++;
                }

                if (i < entrada.length()) {
                    resultado.append("Cadena encontrada: '").append(cadenaActual).append("'\n");
                }
            }
            i++;
        }
        return resultado.toString();
    }

    private void agregarToken(String lexema, String regex, String lenguaje, String tipo, int fila, int columna) {
        Token nuevoToken = new Token(lexema, regex, lenguaje, tipo, fila, columna);
        tokenEncontrado.add(nuevoToken);
    }

  
    /*
    public List<Token> obtenerTokensValidos(String linea) {
        List<Token> tokens = new ArrayList<>();

        // Lógica para reconocer las etiquetas
        String regex = "<([a-zA-Z0-9]+)([^>]*)>(.*?)</\\1>"; // Regex para etiquetas
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(linea);

        while (matcher.find()) {
            String nombreEtiqueta = matcher.group(1);
            String atributos = matcher.group(2);
            String contenido = matcher.group(3);

            // Agregar el token reconocido a la lista
            tokens.add(new Token(linea.toString(), linea.toString(), "HTML", "",0, 0));
        }

        return tokens;
    }
    */
    
    
    public List<Token> obtenerTokensValidos(String linea) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;

        while (i < linea.length()) {
            if (linea.charAt(i) == '<') {
                // Verificar si es un comentario
                if (i + 3 < linea.length() && linea.charAt(i + 1) == '!' && linea.charAt(i + 2) == '-' && linea.charAt(i + 3) == '-') {
                    int inicioComentario = i;
                    i += 4; // Saltamos '<!--'

                    // Buscar el cierre del comentario
                    while (i + 2 < linea.length() && !(linea.charAt(i) == '-' && linea.charAt(i + 1) == '-' && linea.charAt(i + 2) == '>')) {
                        i++;
                    }

                    if (i + 2 < linea.length()) { // Si se encontró el cierre
                        String comentario = linea.substring(inicioComentario, i + 3);
                        tokens.add(new Token(comentario, "", "HTML", "Comentario", 0, 0));
                        i += 3; // Saltamos '-->'
                    }
                } else {
                    // Es el inicio de una etiqueta
                    int inicioEtiqueta = i;
                    i++; // Saltamos el '<'

                    // Extraer el nombre de la etiqueta (hasta un espacio o '>')
                    StringBuilder nombreEtiqueta = new StringBuilder();
                    while (i < linea.length() && linea.charAt(i) != ' ' && linea.charAt(i) != '>') {
                        nombreEtiqueta.append(linea.charAt(i));
                        i++;
                    }

                    // Saltar los atributos de la etiqueta si existen (hasta el cierre '>')
                    while (i < linea.length() && linea.charAt(i) != '>') {
                        i++;
                    }

                    // Si es el cierre de la etiqueta, movemos el índice
                    if (i < linea.length() && linea.charAt(i) == '>') {
                        i++; // Saltamos el '>'
                    }

                   
                    
                    
                     if (traductor.etiquetasNormales().contains("<" + nombreEtiqueta + ">")) {
                        
                        
                        traductor.posiEtiquetas("<" + nombreEtiqueta + ">");
                        
                        int tmp = traductor.posiEtiquetas("<" + nombreEtiqueta + ">");
                        
                         // Agregar el token para la etiqueta de apertura
                        tokens.add(new Token(traductor.etiquetaTraducida().get(tmp), "", "HTML", "Etiqueta", 0, 0));
                       
                        System.out.println("Jalando");
                        
                    }
                    
                    // Verificar si hay contenido dentro de la etiqueta
                    StringBuilder contenidoEtiqueta = new StringBuilder();
                    while (i < linea.length() && linea.charAt(i) != '<') {
                        contenidoEtiqueta.append(linea.charAt(i));
                        i++;
                    }

                    // Si se encontró contenido, agregarlo como token
                    if (contenidoEtiqueta.length() > 0) {
                        tokens.add(new Token(contenidoEtiqueta.toString().trim(), "", "HTML", "Contenido", 0, 0));
                    }
                }
            } else {
                i++; // Avanzar al siguiente carácter
            }
            
            
        }

        for (Token token : tokens) {
            System.out.println("Agregar "+token.getLexema());
        }
        return tokens;
    }

    
}
