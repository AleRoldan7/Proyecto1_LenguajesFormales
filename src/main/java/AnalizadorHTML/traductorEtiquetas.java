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
public class traductorEtiquetas {
    
    private ArrayList<String> etiquetaHTML = new ArrayList<>();
    private ArrayList<String> etiqueta = new ArrayList<>();
    
    
    private ArrayList<String> etiquetasNormales(){
        
        etiqueta.add("<principal>");
        etiqueta.add("<principal/>");
        etiqueta.add("<encabezado>");
        etiqueta.add("<encabezado/>");
        etiqueta.add("<navegacion>");
        etiqueta.add("<navegacion/>");
        etiqueta.add("<apartado>");
        etiqueta.add("<apartado/>");
        etiqueta.add("<listaordenada>");
        etiqueta.add("<listaordenada/>");
        etiqueta.add("<listadesordenada>");
        etiqueta.add("<listadesordenada/>");
        etiqueta.add("<itemlista>");
        etiqueta.add("<itemlista/>");
        etiqueta.add("<anclaje>");
        etiqueta.add("<anclaje/>");
        etiqueta.add("<contenedor>");
        etiqueta.add("<contenedor/>");
        etiqueta.add("<seccion>");
        etiqueta.add("<seccion/>");
        etiqueta.add("<articulo>");
        etiqueta.add("<articulo/>");
        etiqueta.add("<parrafo>");
        etiqueta.add("<parrafo/>");
        etiqueta.add("<span>");
        etiqueta.add("<span/>");
        etiqueta.add("<entrada/>");
        etiqueta.add("<formulario>");
        etiqueta.add("<formulario/>");
        etiqueta.add("<label>");
        etiqueta.add("<label/>");
        etiqueta.add("<area/>");
        etiqueta.add("<boton>");
        etiqueta.add("<boton/>");
        etiqueta.add("<piepagina>");
        etiqueta.add("<piepagina/>");   
        
        return etiqueta;
    }
    
    
    private ArrayList<String> etiquetaTraducida(){
        
        etiquetaHTML.add("<main>");
        etiquetaHTML.add("<main/>");
        etiquetaHTML.add("<header>");
        etiquetaHTML.add("<header/>");
        etiquetaHTML.add("<nav>");
        etiquetaHTML.add("<nav/>");
        etiquetaHTML.add("<aside>");
        etiquetaHTML.add("<aside/>");
        etiquetaHTML.add("<ul>");
        etiquetaHTML.add("<ul/>");
        etiquetaHTML.add("<ol>");
        etiquetaHTML.add("<ol/>");
        etiquetaHTML.add("<li>");
        etiquetaHTML.add("<li/>");
        etiquetaHTML.add("<a>");
        etiquetaHTML.add("<a/>");
        etiquetaHTML.add("<div>");
        etiquetaHTML.add("<div/>");
        etiquetaHTML.add("<section>");
        etiquetaHTML.add("<section/>");
        etiquetaHTML.add("<article>");
        etiquetaHTML.add("<article/>");
        etiquetaHTML.add("<p>");
        etiquetaHTML.add("<p/>");
        etiquetaHTML.add("<span>");
        etiquetaHTML.add("<span/>");
        etiquetaHTML.add("<input/>");
        etiquetaHTML.add("<form>");
        etiquetaHTML.add("<form/>");
        etiquetaHTML.add("<label>");
        etiquetaHTML.add("<label/>");
        etiquetaHTML.add("<textarea/>");
        etiquetaHTML.add("<button>");
        etiquetaHTML.add("<button/>");
        etiquetaHTML.add("<footer>");
        etiquetaHTML.add("<footer/>");   
        
        return etiquetaHTML;
    }
    
    public String traducirEtiqueta(String etiquetaUsuario) {

        etiquetasNormales();
        etiquetaTraducida();
        
        int lista = etiqueta.indexOf(etiquetaUsuario);
        
        if (lista != -1) {
            return etiquetaHTML.get(lista);
        } else {
            return "Etiqueta no encontrada";
        }
    }
   
    
    
}
