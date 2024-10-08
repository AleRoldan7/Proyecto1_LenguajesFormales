/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorCSS;

import Reportes.Token;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alejandro
 */
public class IdentificadorCSS {
    
    private String tokenEstado = ">>[css]";
    
    private char tokenUniversal [] = {'*'};
    
    private String tokenEtiqueta [] = {"body", "header", "main", "nav", "aside", "div", "ul",
                                       "ol", "li", "a", "h1", "h2", "h3", "h4", "h5", "h6", "p",
                                       "span", "label", "textarea", "button", "section", "article", "footer"};
    
    private char tokenCLaseLetras [] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                                        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    private char tokenClaseNumero [] = {'0','1','2','3','4','5','6','7','8','9'};
      
    private char tokenClaseSigno [] = {'.', '-', '#'};
     
    private char tokenCombinador [] = {'>', '+', '~'};
    
    private String tokenReglas [] = {"color", "background-color", "background", "font-size", "font-weight", "font-family",
                                     "font-align", "width", "height", "min-width", "min-height", "max-width", "max-height", 
                                     "display", "inline", "block", "inline-block", "flex", "grid", "none", "margin", "border",
                                     "padding", "content", "border-color", "border-style", "border-width", "border-top", "border-botton",
                                     "border-left", "border-right", "box-sizing", "border-box", "position", "static", "relative", "absolute",
                                     "sticky", "fixed", "top", "bottom", "left", "right", "z-index", "justify-content", "align-items", "border-radius",
                                     "auto", "float", "list-style", "text-align", "box-shadow"};
    
    private String tokenOtros [] = {"px", "%", "rem", "em", "vw", "vh", ":hover", ":active", ":not()", ":nth-child()", "odd", "even",
                                    "::before", "::after,", ":", ";", ",", "(", ")", " "};
    
    private List<String> tokensValidos = new ArrayList<>(); // Lista para almacenar tokens válidos
    
    public String analizarCSS(String cadena) {
        StringBuilder resuCss = new StringBuilder();
        
        if (cadena.contains(tokenEstado)) {
            resuCss.append("Lenguaje Css \n");
            resuCss.append(analizarEtiquetasCss(cadena));
            
            // Agregar reconocimiento para texto dentro de etiquetas
            if (esTokenTextoValido(cadena)) {
                resuCss.append("Token de Texto detectado: " + cadena + "\n");
            }
            
            // Agregar verificación para cadenas con comillas simples
            if (esCadenaConComillasSimples(cadena)) {
                resuCss.append("Cadena con comillas simples detectada: " + cadena + "\n");
            }     
            // Agregar verificación para colores hexadecimales
            if (esColorHexadecimal(cadena)) {
                resuCss.append("Color hexadecimal detectado: " + cadena + "\n");
            }
            // Agregar verificación para colores rgba
            if (esColorRgba(cadena)) {
                resuCss.append("Color rgba detectado: " + cadena + "\n");
            }
            
        } else {
            resuCss.append("Lenguaje no existente");
        }
        return resuCss.toString();
    }
 
    private String analizarEtiquetasCss(String entrada) {
        StringBuilder resultado = new StringBuilder();

        // Uso de una sola iteración para verificar etiquetas, reglas y otros tokens
        for (String token : tokenEtiqueta) {
            if (entrada.contains(token)) {
                resultado.append("Etiqueta CSS detectada: ").append(token).append("\n");
            }
        }

        for (String regla : tokenReglas) {
            if (entrada.contains(regla)) {
                resultado.append("Regla CSS detectada: ").append(regla).append("\n");
            }
        }

        for (String otro : tokenOtros) {
            if (entrada.contains(otro)) {
                resultado.append("Otro token CSS detectado: ").append(otro).append("\n");
            }
        }

        return resultado.toString();
    }

     
   public List<Token> obtenerTokensValidos(String linea, int numeroDeLinea) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder tokenActual = new StringBuilder();
        int columnaActual = 1;

        for (char c : linea.toCharArray()) {
            // Verificar delimitadores
            if (c == ' ' || c == '{' || c == '}' || c == ';') {
                // Si hay un token acumulado, lo agregamos como token válido si está en la lista de métodos válidos
                if (tokenActual.length() > 0) {
                    String lexema = tokenActual.toString();
                    if (tokenActual.length() > 0) {  // Verificar si el token es un método válido
                        tokens.add(new Token(
                            lexema,                        // Lexema del token
                            "Método CSS",                  // Expresión regular (ajustable)
                            "Css",                         // Lenguaje
                            "Método",                      // Tipo (en este caso, sería un método)
                            numeroDeLinea,                 // Fila o línea en la que se encuentra el token
                            columnaActual - tokenActual.length() // Columna inicial del token
                        ));
                    }
                    tokenActual.setLength(0); // Reiniciamos el acumulador del token
                }

                // Si es un delimitador (como '{', '}', ';'), lo agregamos también como token individual
                if (c != ' ') {
                    tokens.add(new Token(
                        String.valueOf(c),            // Lexema del delimitador
                        "Delimitador",                // Expresión regular para delimitadores
                        "Css",                        // Lenguaje
                        "Delimitador",                // Tipo (en este caso, sería un delimitador)
                        numeroDeLinea,                // Fila o línea en la que se encuentra el token
                        columnaActual                 // Columna donde se encuentra el delimitador
                    ));
                }
            } else {
                // Si no es un delimitador, continuamos construyendo el token
                tokenActual.append(c);
            }

            columnaActual++; // Avanzamos a la siguiente columna
        }

        // Si al final hay un token acumulado, lo agregamos también si es válido
        if (tokenActual.length() > 0) {
            String lexema = tokenActual.toString();
            if (tokenActual.length() > 0) {  // Verificar si el token es un método válido
                tokens.add(new Token(
                    lexema,                        // Lexema del token
                    "Método CSS",                  // Expresión regular (ajustable)
                    "Css",                         // Lenguaje
                    "Método",                      // Tipo (ajustable)
                    numeroDeLinea,                 // Fila o línea
                    columnaActual - tokenActual.length() // Columna inicial del token
                ));
            }
        }

        return tokens;
    }



    // Método para guardar tokens válidos
    public void guardarTokensValidos(String token) {
        if (!tokensValidos.contains(token)) { // Evitar duplicados
            tokensValidos.add(token);
        }
    }

    public List<String> getTokensValidos() {
        return tokensValidos;
    }
    

  
    public boolean esTokenTextoValido(String token) {
      
        return token.length() > 0 && token.charAt(0) != '.' && token.charAt(0) != '#' &&
               !contieneCaracterEspecial(token);
    }

    
    public boolean esSelectorDeClase(String token) {
        if (token.length() == 0 || token.charAt(0) != '.') {
            return false;
        }
        String subToken = token.substring(1);

        if (!esLetraMinuscula(subToken.charAt(0))) {
            return false;
        }
        for (int i = 1; i < subToken.length(); i++) {
            char c = subToken.charAt(i);
            if (!esLetraMinuscula(c) && !Character.isDigit(c) && c != '-') {
                return false;
            }
        }
        return true;
    }

    
    public boolean esSelectorDeId(String token) {
        if (token.length() == 0 || token.charAt(0) != '#') {
            return false;
        }
        String subToken = token.substring(1);

        if (!esLetraMinuscula(subToken.charAt(0))) {
            return false;
        }
        for (int i = 1; i < subToken.length(); i++) {
            char c = subToken.charAt(i);
            if (!esLetraMinuscula(c) && !Character.isDigit(c) && c != '-') {
                return false;
            }
        }
        return true;
    }

   
    private boolean esLetraMinuscula(char c) {
        return c >= 'a' && c <= 'z';
    }

  
    private boolean contieneCaracterEspecial(String token) {
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if (c == '{' || c == '}' || c == '.' || c == '#') {
                return true;
            }
        }
        return false;
    }

    // Método para validar si una cadena está entre comillas simples y contiene cualquier conjunto de caracteres
    public boolean esCadenaConComillasSimples(String token) {
        // Verifica que la longitud sea mayor que 1 para tener al menos las comillas
        if (token.length() < 2) {
            return false;
        }
        // Verificar si la cadena empieza y termina con comillas simples
        if (token.charAt(0) == '\'' && token.charAt(token.length() - 1) == '\'') {
            return true;
        }
        return false;
    }
    
    // Método para validar colores hexadecimales (de tres o seis valores)
    public boolean esColorHexadecimal(String token) {
        // Verificar que el primer carácter sea '#' y que tenga 4 o 7 caracteres
        if (token.length() == 4 || token.length() == 7) {
            if (token.charAt(0) == '#') {
                // Verificar que todos los caracteres restantes sean valores hexadecimales válidos (0-9, A-F, a-f)
                for (int i = 1; i < token.length(); i++) {
                    char c = token.charAt(i);
                    if (!esValorHexadecimal(c)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Método para validar valores hexadecimales (0-9, A-F, a-f)
    private boolean esValorHexadecimal(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
    }

    // Método para validar colores rgba (tres valores enteros, y un valor opcional para el alfa que puede ser decimal o entero)
    public boolean esColorRgba(String token) {
        // Verificar que comience con "rgba(" y termine con ")"
        if (esRgbaInicioValido(token) && esRgbaFinValido(token)) {
            // Eliminar "rgba(" del inicio y ")" del final
            String contenido = extraerContenidoRgba(token);
            // Separar los valores por comas
            String[] valores = separarPorComas(contenido);

            // Verificar que haya 3 o 4 valores
            if (valores.length == 3 || valores.length == 4) {
                // Validar que los tres primeros valores sean enteros (0-255)
                for (int i = 0; i < 3; i++) {
                    if (!esEnteroEnRango(valores[i], 0, 255)) {
                        return false;
                    }
                }
                // Si hay un cuarto valor (alfa), debe ser un decimal o entero entre 0 y 1
                if (valores.length == 4) {
                    if (!esDecimalEnRango(valores[3], 0, 1)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Método para verificar que la cadena comience con "rgba("
    private boolean esRgbaInicioValido(String token) {
        if (token.length() > 5) {
            return token.charAt(0) == 'r' &&
                   token.charAt(1) == 'g' &&
                   token.charAt(2) == 'b' &&
                   token.charAt(3) == 'a' &&
                   token.charAt(4) == '(';
        }
        return false;
    }

    // Método para verificar que la cadena termine con ")"
    private boolean esRgbaFinValido(String token) {
        return token.charAt(token.length() - 1) == ')';
    }

    // Método para extraer el contenido entre "rgba(" y ")"
    private String extraerContenidoRgba(String token) {
        return token.substring(5, token.length() - 1).trim();
    }

    // Método para separar una cadena en valores separados por comas
    private String[] separarPorComas(String cadena) {
        int comaPos;
        List<String> valores = new ArrayList<>();
        while ((comaPos = encontrarComa(cadena)) != -1) {
            valores.add(cadena.substring(0, comaPos).trim());
            cadena = cadena.substring(comaPos + 1).trim();
        }
        valores.add(cadena.trim()); // Añadir el último valor después de la última coma
        return valores.toArray(new String[0]);
    }

    // Método para encontrar la posición de la primera coma
    private int encontrarComa(String cadena) {
        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) == ',') {
                return i;
            }
        }
        return -1; // Si no encuentra una coma, devuelve -1
    }

    // Método para verificar si una cadena es un entero en un rango dado
    private boolean esEnteroEnRango(String valor, int minimo, int maximo) {
        try {
            int numero = Integer.parseInt(valor);
            return numero >= minimo && numero <= maximo;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para verificar si una cadena es un decimal o entero en un rango dado
    private boolean esDecimalEnRango(String valor, double minimo, double maximo) {
        try {
            double numero = Double.parseDouble(valor);
            return numero >= minimo && numero <= maximo;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    
}
