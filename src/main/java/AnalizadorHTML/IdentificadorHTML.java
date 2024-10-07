/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorHTML;

import Reportes.Token;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author alejandro
 */
public class IdentificadorHTML {

    private String[] palabraReservada = {"class", "=", "href", "onClick", "id", "style", "type", "placeholder", "required", "name"};
    private TraductorEtiquetas traductor = new TraductorEtiquetas();
    private String etiEstado = ">>[html]";
    private List<Token> tokenEncontrado = new ArrayList<>();
    
    
    public String analizarHTML(String cadena) {
        StringBuilder resultado = new StringBuilder();

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

        return resultado.toString();
    }

    
    private boolean esHTMLValido(String cadena) {
        
        return true;
    }

    
    private String analizarEtiquetas(String entrada) {
        StringBuilder resultado = new StringBuilder();
        ArrayList<String> etiquetas = traductor.etiquetasNormales();

        for (String etiqueta : etiquetas) {
            if (entrada.contains(etiqueta)) {
                int index = etiquetas.indexOf(etiqueta);
                String etiquetaTraducida = traductor.etiquetaTraducida().get(index);

                resultado.append("Etiqueta detectada: ").append(etiqueta).append("\n");
                resultado.append("Etiqueta traducida: ").append(etiquetaTraducida).append("\n");

                // Aquí puedes agregar la lógica para generar el token:
                agregarToken(etiqueta, "expresión_regular_placeholder", "html", "Etiqueta Normal", 0, 0); // Ajusta fila y columna según sea necesario
            }
        }
        return resultado.toString();
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

              
                validarPalabrasReservadas(etiqueta, resultado);
                i = finEtiqueta;
            }
            i++;
        }
        return resultado.toString();
    }

   
    private void validarPalabrasReservadas(String etiqueta, StringBuilder resultado) {
        for (String palabra : palabraReservada) {
            if (etiqueta.contains(palabra)) {
                resultado.append("Palabra reservada encontrada: '").append(palabra).append("'\n");
            }
        }
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

                if (i < entrada.length() && entrada.charAt(i) == delimitador) {
                    resultado.append("Cadena válida encontrada: ").append(cadenaActual.toString()).append("\n");
                } else {
                    resultado.append("Cadena no cerrada correctamente\n");
                }
            }
            i++;
        }

        return resultado.toString();
    }

    
    private String validarTextoFueraDeEtiquetas(String cadena) {
        StringBuilder errores = new StringBuilder();
        
        // Patrón simple para buscar etiquetas y texto entre etiquetas
        boolean dentroDeEtiqueta = false;
        StringBuilder textoActual = new StringBuilder();
        
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            
            if (c == '<') {
                // Encontramos una etiqueta de apertura, empezamos a ignorar el texto dentro
                dentroDeEtiqueta = true;
                
                // Validamos si hubo texto fuera de una etiqueta justo antes de esta
                if (textoActual.length() > 0) {
                    // Agregar lógica para procesar el texto fuera de la etiqueta
                    // Por ejemplo, podrías marcarlo como correcto o incorrecto según las reglas
                    errores.append("Texto fuera de etiqueta detectado: ").append(textoActual).append("\n");
                    textoActual.setLength(0); // Limpiamos el buffer
                }
            }
            
            if (dentroDeEtiqueta && c == '>') {
                // Terminamos de procesar la etiqueta
                dentroDeEtiqueta = false;
            }
            
            if (!dentroDeEtiqueta) {
                // Guardamos el texto fuera de etiquetas
                textoActual.append(c);
            }
        }
        
        // Validación final después de recorrer el string
        if (textoActual.length() > 0) {
            errores.append("Texto fuera de etiqueta detectado al final: ").append(textoActual).append("\n");
        }
        
        // Aquí podrías personalizar más mensajes de error según el texto encontrado
        return errores.toString();
    }

    
    



    public List<Token> obtenerTokensValidos(String entrada) {
        List<Token> resultados = new ArrayList<>();
        StringBuilder tokenActual = new StringBuilder();
        boolean dentroEtiqueta = false;

        // Obtenemos las listas de etiquetas y sus traducciones
        List<String> etiquetasNormales = traductor.etiquetasNormales();
        List<String> etiquetasTraducidas = traductor.etiquetaTraducida();

        for (int i = 0; i < entrada.length(); i++) {
            char c = entrada.charAt(i);

            if (c == '<') {
                dentroEtiqueta = true;
                tokenActual.append(c); // Comienza una nueva etiqueta
            } else if (c == '>') {
                dentroEtiqueta = false;
                tokenActual.append(c); // Cierra la etiqueta

                // Convertir el token a String para su validación
                String etiquetaCompleta = tokenActual.toString();

                // Validar si la etiqueta es válida en la lista de etiquetas normales
                if (etiquetasNormales.contains(etiquetaCompleta)) {
                    // Si es válida, agregarla como un token
                    String etiquetaTraducida = traductor.traducirEtiqueta(etiquetaCompleta);
                    resultados.add(new Token(etiquetaCompleta, etiquetaTraducida, "HTML", "Etiqueta", 0, 0));
                }

                tokenActual.setLength(0); // Limpiar el buffer del token actual
            } else if (dentroEtiqueta) {
                tokenActual.append(c); // Continuar con el contenido de la etiqueta
            }
        }

        return resultados;
    }



   
    private boolean contienePalabrasReservadas(String etiqueta) {
        for (String palabra : palabraReservada) {
            if (etiqueta.indexOf(palabra) != -1) {
                return true;
            }
        }
        return false;
    }
    
   
    
    public void agregarToken(String lexema, String expresionRegular, String lenguaje, String tipo, int fila, int columna) {
        Token nuevoToken = new Token(lexema, expresionRegular, lenguaje, tipo, fila, columna); // Ajusta los parámetros según tu implementación de Token
        tokenEncontrado.add(nuevoToken);
    }


}
