/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorHTML;

import java.util.ArrayList;

/**
 *
 * @author alejandro
 */
public class IdentificadorHTML {
    
    private String palabraReservada [] = {"class", "=", "href", "onClick", "id", "style", "type", "placeholder", "required", "name"};
    private String tokenEstado = ">>[html]";
    private TraductorEtiquetas traductor = new TraductorEtiquetas();
    
    
    public void anlizarHTML(String cadena){
        if (cadena.contains(tokenEstado)) {
            System.out.println("Lenguaje HTML");
            analizarEtiquetas(cadena);
            validarAtributos(cadena);
            validarCadenas(cadena);
        }else{
            System.out.println("Lenguaje no existente");
        }
    }
    
    // Método para analizar etiquetas y mostrar las etiquetas traducidas
    private void analizarEtiquetas(String entrada) {
        ArrayList<String> etiquetas = traductor.etiquetasNormales();
        for (String etiqueta : etiquetas) {
            if (entrada.contains(etiqueta)) {
                System.out.println("Etiqueta encontrada: " + etiqueta);
                int index = etiquetas.indexOf(etiqueta);
                String etiquetaTraducida = traductor.etiquetaTraducida().get(index);
                System.out.println("Etiqueta traducida: " + etiquetaTraducida);
            }
        }
    }
    
  
    // Método para validar que las palabras reservadas solo están en etiquetas de apertura
    private void validarAtributos(String entrada) {
        int i = 0;
        while (i < entrada.length()) {
            // Buscar el inicio de una etiqueta '<'
            if (entrada.charAt(i) == '<' && entrada.charAt(i + 1) != '/') {
                int inicioEtiqueta = i;

                // Buscar el final de la etiqueta '>'
                int finEtiqueta = entrada.indexOf('>', inicioEtiqueta);
                if (finEtiqueta == -1) {
                    // Si no se encuentra '>', no es una etiqueta válida
                    break;
                }

                // Extraer la etiqueta
                String etiqueta = entrada.substring(inicioEtiqueta, finEtiqueta + 1);
                System.out.println("Etiqueta de apertura encontrada: " + etiqueta);

                // Verificar si contiene alguna palabra reservada
                for (String palabra : palabraReservada) {
                    if (etiqueta.contains(palabra)) {
                        System.out.println("Palabra reservada '" + palabra + "' encontrada en la etiqueta de apertura.");
                    }
                }

                // Avanzar después de la etiqueta procesada
                i = finEtiqueta;
            }
            i++;
        }
    }
    
    // Método para validar cadenas que inician con un espacio
    private void validarCadenas(String entrada) {
        int i = 0;
        String cadenaActual = "";

        while (i < entrada.length()) {
            char actual = entrada.charAt(i);
            
            // Si se encuentra un espacio, verificar si la cadena actual es válida
            if (actual == ' ') {
                // Comenzar a construir la cadena
                cadenaActual = " "; // Iniciar con un espacio
                i++; // Pasar al siguiente carácter

                // Continuar hasta encontrar un espacio o el final de la cadena
                while (i < entrada.length() && entrada.charAt(i) != ' ') {
                    cadenaActual += entrada.charAt(i);
                    i++;
                }

                System.out.println("Cadena válida encontrada: " + cadenaActual);
            } else {
                // Avanzar al siguiente carácter
                i++;
            }
        }
    }
}
