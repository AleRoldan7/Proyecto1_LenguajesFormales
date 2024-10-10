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
        "for", "while", "if", "else", "return", "console.log", "null", "target", "value"
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
        int i = 0;

        while (i < linea.length()) {
            // Saltar espacios en blanco
            while (i < linea.length() && Character.isWhitespace(linea.charAt(i))) {
                i++;
            }

            // Si encontramos el final de la línea
            if (i >= linea.length()) {
                break;
            }

            // Verificar si es un comentario de una línea
            if (i + 1 < linea.length() && linea.charAt(i) == '/' && linea.charAt(i + 1) == '/') {
                int inicioComentario = i;
                i += 2; // Saltar "//"
                while (i < linea.length() && linea.charAt(i) != '\n') {
                    i++;
                }
                String comentario = linea.substring(inicioComentario, i);
                tokens.add(new Token(comentario, "", "JavaScript", "Comentario de una línea", 0, 0));
            }
            // Verificar si es un comentario de múltiples líneas
            else if (i + 1 < linea.length() && linea.charAt(i) == '/' && linea.charAt(i + 1) == '*') {
                int inicioComentario = i;
                i += 2; // Saltar "/*"
                while (i + 1 < linea.length() && !(linea.charAt(i) == '*' && linea.charAt(i + 1) == '/')) {
                    i++;
                }
                if (i + 1 < linea.length()) {
                    String comentario = linea.substring(inicioComentario, i + 2);
                    tokens.add(new Token(comentario, "", "JavaScript", "Comentario de múltiples líneas", 0, 0));
                    i += 2; // Saltar "*/"
                }
            }
            // Verificar si el carácter actual es el inicio de un identificador o palabra reservada
            else if (Character.isLetter(linea.charAt(i)) || linea.charAt(i) == '_') {
                StringBuilder identificador = new StringBuilder();
                while (i < linea.length() && (Character.isLetterOrDigit(linea.charAt(i)) || linea.charAt(i) == '_')) {
                    identificador.append(linea.charAt(i));
                    i++;
                }
                String lexema = identificador.toString();
                if (esPalabraReservada(lexema)) {
                    tokens.add(new Token(lexema, "", "JavaScript", "Palabra Reservada", 0, 0));
                } else {
                    tokens.add(new Token(lexema, "", "JavaScript", "Identificador", 0, 0));
                }
            }
            // Verificar si es un número
            else if (Character.isDigit(linea.charAt(i))) {
                StringBuilder numero = new StringBuilder();
                while (i < linea.length() && (Character.isDigit(linea.charAt(i)) || linea.charAt(i) == '.')) {
                    numero.append(linea.charAt(i));
                    i++;
                }
                tokens.add(new Token(numero.toString(), "", "JavaScript", "Número", 0, 0));
            }
            // Verificar cadenas
            else if (linea.charAt(i) == '"' || linea.charAt(i) == '\'' || linea.charAt(i) == '`') {
                char tipoCadena = linea.charAt(i);
                StringBuilder cadena = new StringBuilder();
                cadena.append(tipoCadena);
                i++;
                while (i < linea.length() && linea.charAt(i) != tipoCadena) {
                    cadena.append(linea.charAt(i));
                    i++;
                }
                if (i < linea.length()) {
                    cadena.append(tipoCadena); // Agregar el cierre de la cadena
                    tokens.add(new Token(cadena.toString(), "", "JavaScript", "Cadena", 0, 0));
                    i++; // Saltar el cierre de la cadena
                }
            }
            // Verificar si es un símbolo (incluyendo paréntesis, corchetes, y llaves)
            else if (esSimbolo(Character.toString(linea.charAt(i)))) {
                StringBuilder simbolo = new StringBuilder();
                simbolo.append(linea.charAt(i));
                tokens.add(new Token(simbolo.toString(), "", "JavaScript", "Símbolo", 0, 0));
                i++;
            }
            // Verificar operadores
            else {
                StringBuilder simbolo = new StringBuilder();
                while (i < linea.length() && !Character.isWhitespace(linea.charAt(i))) {
                    simbolo.append(linea.charAt(i));
                    i++;
                }
                String lexema = simbolo.toString();
                if (esOperador(lexema)) {
                    tokens.add(new Token(lexema, "", "JavaScript", "Operador", 0, 0));
                }
            }
        }
        for (Token token : tokens) {
            System.out.println("Agrego " + token.getLexema());
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

    // Método para analizar y retornar los tokens encontrados
    private String analizarTokensJS(String contenido) {
        List<Token> tokens = obtenerTokensValidosJS(contenido);
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append(token.getLexema()).append("\n");
        }
        return sb.toString();
    }
}
