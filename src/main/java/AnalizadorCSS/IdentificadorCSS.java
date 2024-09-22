/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorCSS;

import java.util.ArrayList;

/**
 *
 * @author alejandro
 */
public class IdentificadorCSS {
    
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
                                    "::before", "::after,", ":", ";", ",", "(", ")"};
    
    
    public void analizarCSS(String cadena){
        if (cadena.contains(">>[css]")) {
            System.out.println("Lenguaje Css");
            analizarEtiquetasCss(cadena);
            
        }else{
            System.out.println("Lenguaje no existente");
        }
    }
    
    // Método para analizar etiquetas y mostrar las etiquetas traducidas
    private void analizarEtiquetasCss(String entrada) {
        for (String etiqueta : tokenEtiqueta) {
            if (entrada.contains(etiqueta)) {
                System.out.println("Etiqueta CSS detectada: " + etiqueta);
            }
        }
        for (String regla : tokenReglas) {
            if (entrada.contains(regla)) {
                System.out.println("Regla CSS detectada: " + regla);
            }
        }
        for (String otro : tokenOtros) {
            if (entrada.contains(otro)) {
                System.out.println("Otro token CSS detectado: " + otro);
            }
        }
    }
     
            
    
}
