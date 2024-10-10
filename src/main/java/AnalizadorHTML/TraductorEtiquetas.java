package AnalizadorHTML;

import java.util.ArrayList;

/**
 *
 * @author alejandro
 */
public class TraductorEtiquetas {

    private ArrayList<String> etiquetaHTML = new ArrayList<>();
    private ArrayList<String> etiqueta = new ArrayList<>();

    // Método para inicializar etiquetas normales
    public ArrayList<String> etiquetasNormales() {

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

    // Método para inicializar etiquetas HTML traducidas
    public ArrayList<String> etiquetaTraducida() {

        etiquetaHTML.add("<main>");
        etiquetaHTML.add("</main>");
        etiquetaHTML.add("<header>");
        etiquetaHTML.add("</header>");
        etiquetaHTML.add("<nav>");
        etiquetaHTML.add("</nav>");
        etiquetaHTML.add("<aside>");
        etiquetaHTML.add("</aside>");
        etiquetaHTML.add("<ul>");
        etiquetaHTML.add("</ul>");
        etiquetaHTML.add("<ol>");
        etiquetaHTML.add("</ol>");
        etiquetaHTML.add("<li>");
        etiquetaHTML.add("</li>");
        etiquetaHTML.add("<a>");
        etiquetaHTML.add("</a>");
        etiquetaHTML.add("<div>");
        etiquetaHTML.add("</div>");
        etiquetaHTML.add("<section>");
        etiquetaHTML.add("</section>");
        etiquetaHTML.add("<article>");
        etiquetaHTML.add("</article>");
        etiquetaHTML.add("<p>");
        etiquetaHTML.add("</p>");
        etiquetaHTML.add("<span>");
        etiquetaHTML.add("</span>");
        etiquetaHTML.add("</input>");
        etiquetaHTML.add("<form>");
        etiquetaHTML.add("</form>");
        etiquetaHTML.add("<label>");
        etiquetaHTML.add("</label>");
        etiquetaHTML.add("</textarea>");
        etiquetaHTML.add("<button>");
        etiquetaHTML.add("</button>");
        etiquetaHTML.add("<footer>");
        etiquetaHTML.add("</footer>");

        return etiquetaHTML;
    }

    // Método para traducir etiquetas
    public String traducirEtiqueta(String etiquetaUsuario) {

        // Cargar etiquetas normales y traducidas
        etiquetasNormales();
        etiquetaTraducida();

        // Verificar si es una etiqueta de título (titulo#)
        if (etiquetaUsuario.startsWith("<titulo") && etiquetaUsuario.endsWith(">")) {
            String numero = etiquetaUsuario.substring(7, etiquetaUsuario.length() - 1); // Extraer el número entre <titulo#>
            try {
                int nivel = Integer.parseInt(numero);
                if (nivel >= 1 && nivel <= 6) {
                    return "<h" + nivel + ">";
                } else {
                    return "Número de título fuera de rango. Solo se permite de h1 a h6.";
                }
            } catch (NumberFormatException e) {
                return "Formato de título incorrecto. Debe ser <titulo#>, donde # es un número.";
            }
        }

        // Verificar si es una etiqueta de cierre de título
        if (etiquetaUsuario.startsWith("</titulo") && etiquetaUsuario.endsWith(">")) {
            String numero = etiquetaUsuario.substring(8, etiquetaUsuario.length() - 1); // Extraer el número entre </titulo#>
            try {
                int nivel = Integer.parseInt(numero);
                if (nivel >= 1 && nivel <= 6) {
                    return "</h" + nivel + ">";
                } else {
                    return "Número de título fuera de rango. Solo se permite de h1 a h6.";
                }
            } catch (NumberFormatException e) {
                return "Formato de título incorrecto. Debe ser </titulo#>, donde # es un número.";
            }
        }

        // Si no es una etiqueta de título, buscar en las etiquetas normales
        int lista = etiqueta.indexOf(etiquetaUsuario);
        if (lista != -1) {
            return etiquetaHTML.get(lista);
        } else {
            return "Etiqueta no encontrada";
        }
    }
    
    
    public int posiEtiquetas(String cadena){
        
        for (int i = 0; i < etiquetasNormales().size(); i++) {
            String tmp = etiquetasNormales().get(i);
            if (cadena.equals(tmp)) {
                return i;
            }
        }
        return 0;
    }
}
