package AnalizadorCSS;

import Reportes.Token;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para analizar y validar entradas CSS.
 * Esta clase identifica selectores, reglas y propiedades en el CSS.
 */
public class IdentificadorCSS {
    
   private List<String> tokensValidos = new ArrayList<>();

    public void analizar(String entrada) {
        if (!esCadenaCssValida(entrada)) {
            System.out.println("Entrada no es CSS");
            return;
        }

        String[] lineas = entrada.split("\n");
        for (String linea : lineas) {
            if (linea.trim().isEmpty()) continue;

            // Lógica para analizar cada línea y obtener tokens
            List<Token> tokens = analizarLinea(linea);
            for (Token token : tokens) {
                System.out.println(token.getLexema());
            }
        }
    }

    private boolean esCadenaCssValida(String cadena) {
        return cadena.contains("{") && cadena.contains("}");
    }

    public List<Token> analizarLinea(String linea) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;

        while (i < linea.length()) {
            char actual = linea.charAt(i);

            // Ignorar espacios
            if (Character.isWhitespace(actual)) {
                i++;
                continue;
            }

            // Procesar comas, dos puntos, punto y coma
            if (actual == ',') {
                tokens.add(new Token(",", "", "CSS", "Coma", 0, 0));
                i++;
                continue;
            }
            if (actual == ':') {
                tokens.add(new Token(":", "", "CSS", "Dos puntos", 0, 0));
                i++;
                continue;
            }
            if (actual == ';') {
                tokens.add(new Token(";", "", "CSS", "Punto y coma", 0, 0));
                i++;
                continue;
            }

            // Procesar comillas simples
            if (actual == '\'') {
                int inicio = i;
                i++;
                while (i < linea.length() && linea.charAt(i) != '\'') {
                    i++;
                }
                if (i < linea.length()) {
                    String contenido = linea.substring(inicio, i + 1);
                    tokens.add(new Token(contenido, "", "CSS", "Cadena", 0, 0));
                    i++;
                }
                continue;
            }

            // Procesar colores hexadecimales y rgba
            if (actual == '#') {
                int inicio = i;
                i++;
                while (i < linea.length() && (Character.isDigit(linea.charAt(i)) || 
                                               (linea.charAt(i) >= 'a' && linea.charAt(i) <= 'f'))) {
                    i++;
                }
                // Validar si es un color hexadecimal
                String hexColor = linea.substring(inicio, i);
                if (hexColor.length() == 4 || hexColor.length() == 7) {
                    tokens.add(new Token(hexColor, "", "CSS", "Color Hexadecimal", 0, 0));
                    continue;
                }
            }

            // Procesar rgba
            if (linea.startsWith("rgba(", i)) {
                int inicio = i;
                i += 5; // Saltar "rgba("
                int count = 0;
                StringBuilder rgbaBuilder = new StringBuilder("rgba(");
                while (i < linea.length() && count < 4) {
                    char c = linea.charAt(i);
                    rgbaBuilder.append(c);
                    if (c == ',' || c == ')') {
                        count++;
                    }
                    i++;
                }
                if (count == 4) {
                    tokens.add(new Token(rgbaBuilder.toString(), "", "CSS", "Color RGBA", 0, 0));
                    continue;
                }
                i = inicio; // Reiniciar si no se validó
            }

            // Identificar y procesar selectores
            if (actual == '.' || actual == '#') {
                int inicio = i;
                i = procesarSelector(linea, i, tokens);
                if (i == -1) return tokens; // Error en el procesamiento
                continue;
            }

            // Procesar llaves
            if (actual == '{') {
                tokens.add(new Token("{", "", "CSS", "Llave Apertura", 0, 0));
                i++;
                continue;
            }
            if (actual == '}') {
                tokens.add(new Token("}", "", "CSS", "Llave Cierre", 0, 0));
                i++;
                continue;
            }

            // Procesar enteros
            if (Character.isDigit(actual)) {
                int inicio = i;
                while (i < linea.length() && Character.isDigit(linea.charAt(i))) {
                    i++;
                }
                String entero = linea.substring(inicio, i);
                tokens.add(new Token(entero, "", "CSS", "Entero", 0, 0));
                continue;
            }

            // Procesar combinadores
            if (actual == '>' || actual == '+' || actual == '~') {
                tokens.add(new Token(String.valueOf(actual), "", "CSS", "Combinador", 0, 0));
                i++;
                continue;
            }

            // Procesar identificadores
            if (Character.isLetter(actual)) {
                int inicio = i;
                while (i < linea.length() && (Character.isLetter(linea.charAt(i)) || Character.isDigit(linea.charAt(i)) || linea.charAt(i) == '-')) {
                    i++;
                }
                String identificador = linea.substring(inicio, i);
                // Validar si es un selector de ID
                if (identificador.startsWith("#") && identificador.matches("#[a-z][a-z0-9-]*")) {
                    tokens.add(new Token(identificador, "", "CSS", "Selector ID", 0, 0));
                } else {
                    tokens.add(new Token(identificador, "", "CSS", "Identificador", 0, 0));
                }
                continue;
            }

            // Si llegamos aquí, hemos encontrado un carácter no reconocido
            i++;
        }

        // Imprimir los tokens para verificar qué se ha agregado
        for (Token token : tokens) {
            System.out.println("Agregar " + token.getLexema());
        }
        return tokens;
    }

    private int procesarSelector(String linea, int i, List<Token> tokens) {
        char tipo = linea.charAt(i); // Tipo de selector: '.' o '#'
        i++; // Saltamos el punto o la almohadilla
        int inicio = i; // Guardamos el inicio del identificador

        // Extraer el nombre del selector
        while (i < linea.length() && (Character.isLetter(linea.charAt(i)) || Character.isDigit(linea.charAt(i)) || linea.charAt(i) == '-' || linea.charAt(i) == '.')) {
            i++;
        }

        String selector = linea.substring(inicio - 1, i); // Incluimos el '.' o '#' al inicio
        String tipoSelector = (tipo == '.') ? "Clase" : "ID";
        tokens.add(new Token(selector, "", "CSS", tipoSelector, 0, 0));

        return i; // Retornamos la posición actual del índice
    }
}
