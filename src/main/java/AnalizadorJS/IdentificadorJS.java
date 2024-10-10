package AnalizadorJS;

import Reportes.Token;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IdentificadorJS {

    private String tokenEstadoJS = ">>[js]";
    
    private String[] palabrasReservadasJS = {
        "function", "const", "let", "document", "event", "alert", 
        "for", "while", "if", "else", "return", "console.log", "null"
    };

    // Operadores Aritméticos
    private final String[] operadoresAritmeticos = {"+", "-", "*", "/"};

    // Operadores Relacionales
    private final String[] operadoresRelacionales = {"==", "<", ">", "<=", ">=", "!="};

    // Operadores Lógicos
    private final String[] operadoresLogicos = {"||", "&&", "!"};

    // Operadores Incrementales
    private final String[] operadoresIncrementales = {"++", "--"};

    // Otros símbolos
    private final String[] otrosSimbolos = {"(", ")", "{", "}", "[", "]", "=", ";", ",", ".", ":", "\"", "'", "`"};

    public String analizarJS(String cadena) {
        StringBuilder resuJs = new StringBuilder();

        // Inicializar el índice de búsqueda
        int startIndex = 0;

        // Buscar bloques de código JavaScript
        while ((startIndex = cadena.indexOf(tokenEstadoJS, startIndex)) != -1) {
            // Extraer solo el contenido después de la etiqueta de estado
            startIndex += tokenEstadoJS.length();
            int endIndex = cadena.indexOf(">>", startIndex); // Suponiendo que el bloque JS finaliza con ">>"

            String contenidoJS;
            if (endIndex != -1) {
                contenidoJS = cadena.substring(startIndex, endIndex).trim();
                startIndex = endIndex + 2; // Avanza después del bloque encontrado
            } else {
                contenidoJS = cadena.substring(startIndex).trim(); // Captura hasta el final si no se encuentra el final
                startIndex = cadena.length(); // Marca el final de la cadena
            }

            if (!contenidoJS.isEmpty()) {
                resuJs.append("Lenguaje JavaScript detectado \n");
                resuJs.append(analizarTokensJS(contenidoJS)); // Analizar el contenido
            } else {
                resuJs.append("Error: No se encontró contenido JavaScript después de la etiqueta de estado.\n");
            }
        }

        if (resuJs.length() == 0) {
            resuJs.append("Error: Lenguaje no detectado, se requiere la etiqueta >>[js].\n");
        }

        return resuJs.toString();
    }


    public List<Token> obtenerTokensValidosJS(String linea) {
        List<Token> tokens = new ArrayList<>();
        Set<String> tokensDetectados = new HashSet<>(); // Para evitar duplicaciones
        int i = 0;

        while (i < linea.length()) {
            // Saltar espacios en blanco
            //while (i < linea.length() && Character.isWhitespace(linea.charAt(i))) {
            ///    i++;
           // }

            // Si encontramos el final de la línea
            if (i >= linea.length()) {
                break;
            }

            // Verificar si es un comentario de una línea
            if (i + 1 < linea.length() && linea.charAt(i) == '/' && linea.charAt(i + 1) == '/') {
                int inicioComentario = i;
                // Avanza más allá de "//"
                i += 2;
                // Busca el final del comentario
                while (i < linea.length() && linea.charAt(i) != '\n') {
                    i++;
                }
                // Agrega el comentario a la lista de tokens
                String comentario = linea.substring(inicioComentario, i);
                agregarToken(tokens, tokensDetectados, comentario, "Comentario de una línea");
            }
            // Verificar si es un comentario de múltiples líneas
            else if (i + 1 < linea.length() && linea.charAt(i) == '/' && linea.charAt(i + 1) == '*') {
                int inicioComentario = i;
                // Avanza más allá de "/*"
                i += 2;
                // Busca el final del comentario
                while (i + 1 < linea.length() && !(linea.charAt(i) == '*' && linea.charAt(i + 1) == '/')) {
                    i++;
                }
                if (i + 1 < linea.length()) { // Si se encontró el cierre
                    String comentario = linea.substring(inicioComentario, i + 2);
                    agregarToken(tokens, tokensDetectados, comentario, "Comentario de múltiples líneas");
                    i += 2; // Avanza más allá de "*/"
                }
            }
            // Verificar si el carácter actual es el inicio de un token
            else if (Character.isLetter(linea.charAt(i)) || linea.charAt(i) == '_') {
                // Reconocer identificadores o palabras reservadas
                StringBuilder identificador = new StringBuilder();
                while (i < linea.length() && (Character.isLetterOrDigit(linea.charAt(i)) || linea.charAt(i) == '_')) {
                    identificador.append(linea.charAt(i));
                    i++;
                }
                String lexema = identificador.toString();
                if (esPalabraReservada(lexema)) {
                    agregarToken(tokens, tokensDetectados, lexema, "Palabra Reservada");
                } else {
                    agregarToken(tokens, tokensDetectados, lexema, "Identificador");
                }
            } else if (Character.isDigit(linea.charAt(i))) {
                // Reconocer números (Enteros y Decimales)
                StringBuilder numero = new StringBuilder();
                while (i < linea.length() && (Character.isDigit(linea.charAt(i)) || linea.charAt(i) == '.')) {
                    numero.append(linea.charAt(i));
                    i++;
                }
                agregarToken(tokens, tokensDetectados, numero.toString(), "Número");
            } else {
                // Reconocer operadores, otros símbolos o cadenas
                StringBuilder simbolo = new StringBuilder();
                while (i < linea.length() && !Character.isWhitespace(linea.charAt(i))) {
                    simbolo.append(linea.charAt(i));
                    i++;
                }
                String lexema = simbolo.toString();

                // Identificar si es un operador, símbolo u otro tipo
                if (esOperador(lexema)) {
                    agregarToken(tokens, tokensDetectados, lexema, "Operador");
                } else if (esSimbolo(lexema)) {
                    agregarToken(tokens, tokensDetectados, lexema, "Símbolo");
                } else if (esCadena(lexema)) {
                    agregarToken(tokens, tokensDetectados, lexema, "Cadena");
                }
            }
        }

        return tokens;
    }


    // Método para agregar tokens a la lista, asegurando que no se dupliquen
    private void agregarToken(List<Token> tokens, Set<String> tokensDetectados, String lexema, String tipo) {
        if (!tokensDetectados.contains(lexema)) {
            tokens.add(new Token(lexema, "", "JavaScript", tipo, 0, 0));
            tokensDetectados.add(lexema); // Añadir a los detectados
        }
    }

    // Verifica si una palabra es una palabra reservada de JavaScript
    private boolean esPalabraReservada(String palabra) {
        for (String reservada : palabrasReservadasJS) {
            if (reservada.equals(palabra)) {
                return true;
            }
        }
        return false;
    }

    // Verifica si el lexema es un operador
    private boolean esOperador(String lexema) {
        for (String operador : operadoresAritmeticos) {
            if (operador.equals(lexema)) return true;
        }
        for (String operador : operadoresRelacionales) {
            if (operador.equals(lexema)) return true;
        }
        for (String operador : operadoresLogicos) {
            if (operador.equals(lexema)) return true;
        }
        for (String operador : operadoresIncrementales) {
            if (operador.equals(lexema)) return true;
        }
        return false;
    }

    // Verifica si el lexema es un símbolo
    private boolean esSimbolo(String lexema) {
        for (String simbolo : otrosSimbolos) {
            if (simbolo.equals(lexema)) return true;
        }
        return false;
    }

    // Verifica si el lexema es una cadena
    private boolean esCadena(String lexema) {
        return (lexema.startsWith("\"") && lexema.endsWith("\"")) || 
               (lexema.startsWith("'") && lexema.endsWith("'")) || 
               (lexema.startsWith("`") && lexema.endsWith("`"));
    }

    // Analiza los tokens en la cadena JS y devuelve una descripción detallada
    private String analizarTokensJS(String entrada) {
        StringBuilder resultado = new StringBuilder();
        List<Token> tokensValidos = obtenerTokensValidosJS(entrada);

        if (tokensValidos.isEmpty()) {
            resultado.append("No se detectaron tokens válidos.\n");
        } else {
            for (Token token : tokensValidos) {
                resultado.append("Token detectado: ").append(token.getLexema()).append(", Tipo: ").append(token.getTipo()).append("\n");
            }
        }

        return resultado.toString();
    }
}
