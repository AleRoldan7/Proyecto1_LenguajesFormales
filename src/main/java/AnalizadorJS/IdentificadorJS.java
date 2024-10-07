/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorJS;

import Reportes.Token;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alejandro
 */
public class IdentificadorJS {

    private String tokenEstadoJS = ">>[js]";
    
    private char identificador [] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                                     'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                                     'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                                     'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    
    private char numeros [] = {'0','1', '2', '3','4', '5', '6','7', '8', '9'};
    
    private char signos [] = {'_', '.'};
    
    private char aritmeticos [] = {'+', '-', '/', '*'};
    
    private String relacionales [] = {"==", "<", ">", "<=", ">=", "!="};
    
    private String logicos [] = {"||", "&&", "!"};
    
    private String incremento [] = {"++", "--"};
    
    private String booleano [] = {"true", "false"};
    
    private String palabrasReservadasJS [] = {"function", "const", "let", "document", "event", "alert", "for", "while", "if", "else",
                                              "return", "console.log", "null"};
    
    private char otrosJS [] = {'(', ')', '[', ']', '{', '}', '=', ';', ',', '.', ':'};
    
    // Método principal para analizar cadenas JavaScript
    public String analizarJS(String cadena) {
        StringBuilder resuJs = new StringBuilder();

        // Verifica si la cadena contiene el estado JavaScript
        if (cadena.contains(tokenEstadoJS)) {
            resuJs.append("Lenguaje JavaScript detectado \n");
            resuJs.append(analizarTokensJS(cadena));
        } else {
            resuJs.append("Lenguaje no detectado");
        }

        return resuJs.toString();
    }

    public List<Token> obtenerTokensValidos(String linea, int numeroDeLinea) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder tokenActual = new StringBuilder();
        int columnaActual = 1;

        for (char c : linea.toCharArray()) {
            if (c == ' ' || c == ';' || c == '{' || c == '}' || c == '(' || c == ')') {
                if (tokenActual.length() > 0) {
                    // Agregar el token encontrado
                    tokens.add(new Token(
                            tokenActual.toString(),
                            "Desconocido", // Puedes ajustar la expresión regular según corresponda
                            "JavaScript",
                            "Identificador",
                            numeroDeLinea,
                            columnaActual - tokenActual.length()
                    ));
                    tokenActual.setLength(0);
                }
                if (c != ' ') {
                    // Agregar el carácter especial como token
                    tokens.add(new Token(
                            String.valueOf(c),
                            "Desconocido", // Ajustar la expresión regular si es necesario
                            "JavaScript",
                            "Símbolo",
                            numeroDeLinea,
                            columnaActual
                    ));
                }
            } else {
                tokenActual.append(c);
            }
            columnaActual++;
        }

        // Añadir el último token que pudo haber quedado
        if (tokenActual.length() > 0) {
            tokens.add(new Token(
                    tokenActual.toString(),
                    "Desconocido",
                    "JavaScript",
                    "Identificador",
                    numeroDeLinea,
                    columnaActual - tokenActual.length()
            ));
        }

        return tokens;
    }



    // Método auxiliar para buscar palabras reservadas
    private List<String> buscarPalabrasReservadas(String entrada) {
        List<String> tokens = new ArrayList<>();
        for (String palabra : palabrasReservadasJS) {
            if (entrada.contains(palabra)) {
                tokens.add(palabra);
            }
        }
        return tokens;
    }

    // Método auxiliar para buscar valores booleanos
    private List<String> buscarValoresBooleanos(String entrada) {
        List<String> tokens = new ArrayList<>();
        for (String bool : booleano) {
            if (entrada.contains(bool)) {
                tokens.add(bool);
            }
        }
        return tokens;
    }

    // Método auxiliar para buscar operadores relacionales
    private List<String> buscarOperadoresRelacionales(String entrada) {
        List<String> tokens = new ArrayList<>();
        for (String rel : relacionales) {
            if (entrada.contains(rel)) {
                tokens.add(rel);
            }
        }
        return tokens;
    }

    // Método auxiliar para buscar operadores lógicos
    private List<String> buscarOperadoresLogicos(String entrada) {
        List<String> tokens = new ArrayList<>();
        for (String log : logicos) {
            if (entrada.contains(log)) {
                tokens.add(log);
            }
        }
        return tokens;
    }

    // Método auxiliar para buscar operadores de incremento/decremento
    private List<String> buscarOperadoresIncremento(String entrada) {
        List<String> tokens = new ArrayList<>();
        for (String inc : incremento) {
            if (entrada.contains(inc)) {
                tokens.add(inc);
            }
        }
        return tokens;
    }

    // Método auxiliar para buscar operadores aritméticos
    private List<String> buscarOperadoresAritmeticos(String entrada) {
        List<String> tokens = new ArrayList<>();
        for (char arit : aritmeticos) {
            if (entrada.indexOf(arit) != -1) {
                tokens.add(String.valueOf(arit));
            }
        }
        return tokens;
    }

    // Método auxiliar para buscar otros tokens (caracteres especiales)
    private List<String> buscarOtrosTokens(String entrada) {
        List<String> tokens = new ArrayList<>();
        for (char otro : otrosJS) {
            if (entrada.indexOf(otro) != -1) {
                tokens.add(String.valueOf(otro));
            }
        }
        return tokens;
    }
    
    // Analiza los tokens en la cadena JS y devuelve una descripción detallada
    private String analizarTokensJS(String entrada) {
        StringBuilder resultado = new StringBuilder();

        // Verificar y analizar palabras reservadas de JS
        for (String palabra : palabrasReservadasJS) {
            if (entrada.contains(palabra)) {
                resultado.append("Palabra reservada JS detectada: " + palabra + "\n");
            }
        }

        // Verificar y analizar valores booleanos
        for (String bool : booleano) {
            if (entrada.contains(bool)) {
                resultado.append("Valor booleano detectado: " + bool + "\n");
            }
        }

        // Verificar y analizar operadores relacionales
        for (String rel : relacionales) {
            if (entrada.contains(rel)) {
                resultado.append("Operador relacional detectado: " + rel + "\n");
            }
        }

        // Verificar y analizar operadores lógicos
        for (String log : logicos) {
            if (entrada.contains(log)) {
                resultado.append("Operador lógico detectado: " + log + "\n");
            }
        }

        // Verificar y analizar operadores de incremento o decremento
        for (String inc : incremento) {
            if (entrada.contains(inc)) {
                resultado.append("Operador de incremento/decremento detectado: " + inc + "\n");
            }
        }

        // Verificar y analizar operadores aritméticos
        for (char arit : aritmeticos) {
            if (entrada.indexOf(arit) != -1) {
                resultado.append("Operador aritmético detectado: " + arit + "\n");
            }
        }

        // Verificar y analizar otros tokens como paréntesis, corchetes, etc.
        for (char otro : otrosJS) {
            if (entrada.indexOf(otro) != -1) {
                resultado.append("Otro token detectado: " + otro + "\n");
            }
        }

        return resultado.toString();
    }
    
     

}