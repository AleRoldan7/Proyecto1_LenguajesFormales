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
    private List<Token> tokenEncontrado = new ArrayList<>();

    public String analizarHTML(String cadena) {
        StringBuilder resultado = new StringBuilder();

        // Detectar y procesar el token de estado >>[html]
        if (cadena.contains(">>[html]")) {
            agregarToken(">>[html]", ">>\\[html\\]", "html", "Token de Estado", 0, 0);
            resultado.append("Token de estado detectado: >>[html]\n");
        }

        if (esHTMLValido(cadena)) {
            resultado.append("Lenguaje HTML detectado\n");
            resultado.append(analizarEtiquetas(cadena));
            resultado.append(validarAtributos(cadena));
            resultado.append(validarCadenas(cadena));

            // Obtener los tokens válidos
            List<Token> tokens = obtenerTokensValidos(cadena);
            for (Token token : tokens) {
                resultado.append("Token encontrado: ").append(token.getLexema()).append("\n");
            }

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

    private boolean esHTMLValido(String cadena) {
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
                        stack.push(nombreEtiqueta); // Agregar etiqueta al stack
                    } else {
                        return false; // Etiqueta no válida
                    }
                } else { // Es una etiqueta de cierre
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

    // Método para analizar las etiquetas HTML y generar tokens
    private List<Token> analizarEtiquetas(String entrada) {
        List<Token> tokens = new ArrayList<>();
        List<String> etiquetas = traductor.etiquetasNormales(); // Obtiene las etiquetas válidas desde el traductor

        int i = 0; // Índice para recorrer la cadena de entrada
        while (i < entrada.length()) {
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
            }
            i++;
        }

        return tokens;
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
            if (linea.charAt(i) == '<') { // Comienza una etiqueta
                int finEtiqueta = linea.indexOf('>', i);
                if (finEtiqueta == -1) break; // No hay cierre de etiqueta, termina el proceso

                String etiquetaCompleta = linea.substring(i + 1, finEtiqueta).trim();
                String nombreEtiqueta;
                String atributos = "";
                String contenido = "";

                // Separar nombre de la etiqueta de sus atributos
                if (etiquetaCompleta.contains(" ")) {
                    nombreEtiqueta = etiquetaCompleta.substring(0, etiquetaCompleta.indexOf(" ")).trim();
                    atributos = etiquetaCompleta.substring(etiquetaCompleta.indexOf(" ") + 1).trim();
                } else {
                    nombreEtiqueta = etiquetaCompleta; // No tiene atributos
                }

                // Identificar contenido entre la etiqueta de apertura y cierre
                int cierreEtiqueta = linea.indexOf("</" + nombreEtiqueta + ">", finEtiqueta);
                if (cierreEtiqueta != -1) {
                    contenido = linea.substring(finEtiqueta + 1, cierreEtiqueta).trim();
                }

                // Agregar tokens de la etiqueta de apertura
                tokens.add(new Token("<" + nombreEtiqueta + ">", "<" + nombreEtiqueta + ">", "HTML", "Etiqueta de Apertura", 0, 0));

                // Agregar atributos como token si existen
                if (!atributos.isEmpty()) {
                    tokens.add(new Token(atributos, atributos, "HTML", "Atributos", 0, 0));
                }

                // Agregar contenido entre las etiquetas si existe
                if (!contenido.isEmpty()) {
                    tokens.add(new Token(contenido, contenido, "HTML", "Contenido", 0, 0));
                }

                // Agregar tokens de la etiqueta de cierre
                tokens.add(new Token("</" + nombreEtiqueta + ">", "</" + nombreEtiqueta + ">", "HTML", "Etiqueta de Cierre", 0, 0));

                // Avanzar el índice después de la etiqueta de cierre
                i = cierreEtiqueta + nombreEtiqueta.length() + 3; // Para avanzar </nombreEtiqueta>
            } else {
                i++; // Continuar con el siguiente carácter si no es una etiqueta
            }
        }

        return tokens;
    }



}
