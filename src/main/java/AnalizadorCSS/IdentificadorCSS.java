package AnalizadorCSS;

import Reportes.Token;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para analizar y validar entradas CSS.
 * Esta clase identifica selectores, reglas y propiedades en el CSS.
 */
public class IdentificadorCSS {
    
    private String tokenEstado = ">>[css]";
    
    private char tokenUniversal[] = {'*'};
    
    private String tokenEtiqueta[] = {"body", "header", "main", "nav", "aside", "div", "ul",
                                       "ol", "li", "a", "h1", "h2", "h3", "h4", "h5", "h6", "p",
                                       "span", "label", "textarea", "button", "section", "article", "footer"};
    
    private char tokenClaseLetras[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                                        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    private char tokenClaseNumero[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    
    private char tokenClaseSigno[] = {'.', '-', '#'};
    
    private char tokenCombinador[] = {'>', '+', '~'};
    
    private String tokenReglas[] = {"color", "background-color", "background", "font-size", "font-weight", "font-family",
                                     "font-align", "width", "height", "min-width", "min-height", "max-width", "max-height", 
                                     "display", "inline", "block", "inline-block", "flex", "grid", "none", "margin", "border",
                                     "padding", "content", "border-color", "border-style", "border-width", "border-top", "border-bottom",
                                     "border-left", "border-right", "box-sizing", "border-box", "position", "static", "relative", "absolute",
                                     "sticky", "fixed", "top", "bottom", "left", "right", "z-index", "justify-content", "align-items", "border-radius",
                                     "auto", "float", "list-style", "text-align", "box-shadow"};
    
    private String tokenOtros[] = {"px", "%", "rem", "em", "vw", "vh", ":hover", ":active", ":not()", ":nth-child()", "odd", "even",
                                    "::before", "::after", ":", ";", ",", "(", ")", " "};
    
    private List<String> tokensValidos = new ArrayList<>();

    public void analizar(String entrada) {
        if (!esCadenaCssValida(entrada)) {
            System.out.println("Entrada no es CSS");
            return;
        }

        // Procesar entrada CSS
        String[] lineas = entrada.split("\n");
        for (String linea : lineas) {
            // Lógica para analizar cada línea y obtener tokens
            String resultado = analizarEtiquetasCss(linea);
            System.out.println(resultado); // Imprimir resultado de la línea analizada
            if (linea.contains("{")) {
                // Extraer propiedades
                extraerPropiedades(linea);
            }
        }
    }

    private boolean esCadenaCssValida(String cadena) {
        return cadena.contains("{") && cadena.contains("}");
    }

    private void extraerPropiedades(String linea) {
        String propiedades = linea.substring(linea.indexOf('{') + 1, linea.indexOf('}')).trim();
        if (!propiedades.isEmpty()) {
            tokensValidos.add(propiedades);
        }
    }
 
    private String analizarEtiquetasCss(String entrada) {
        StringBuilder resultado = new StringBuilder();

        // Uso de una sola iteración para verificar etiquetas, reglas y otros tokens
        for (String token : tokenEtiqueta) {
            if (entrada.contains(token)) {
                resultado.append("Etiqueta CSS detectada: ").append(token).append("\n");
                guardarTokensValidos(token);
            }
        }

        for (String regla : tokenReglas) {
            if (entrada.contains(regla)) {
                resultado.append("Regla CSS detectada: ").append(regla).append("\n");
                guardarTokensValidos(regla);
            }
        }

        for (String otro : tokenOtros) {
            if (entrada.contains(otro)) {
                resultado.append("Otro token CSS detectado: ").append(otro).append("\n");
                guardarTokensValidos(otro);
            }
        }

        // Detectar selectores universales
        for (char universal : tokenUniversal) {
            if (entrada.indexOf(universal) != -1) {
                resultado.append("Selector Universal detectado: ").append(universal).append("\n");
                guardarTokensValidos(String.valueOf(universal));
            }
        }

        // Detectar combinadores
        for (char combinador : tokenCombinador) {
            if (entrada.indexOf(combinador) != -1) {
                resultado.append("Combinador detectado: ").append(combinador).append("\n");
                guardarTokensValidos(String.valueOf(combinador));
            }
        }

        return resultado.toString();
    }

    public List<Token> obtenerTokensValidosCSS(String linea) {
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

            // Verificar si es el inicio de un comentario
            if (i + 1 < linea.length() && linea.charAt(i) == '/' && linea.charAt(i + 1) == '*') {
                int inicioComentario = i;
                i += 2; // Saltamos '/*'

                // Buscar el cierre del comentario
                while (i + 1 < linea.length() && !(linea.charAt(i) == '*' && linea.charAt(i + 1) == '/')) {
                    i++;
                }

                if (i + 1 < linea.length()) { // Si se encontró el cierre
                    String comentario = linea.substring(inicioComentario, i + 2);
                    tokens.add(new Token(comentario, "", "CSS", "Comentario", 0, 0));
                    i += 2; // Saltamos '*/'
                }
                continue; // Continuar al siguiente ciclo para evitar procesar más caracteres
            }

            // Extraer un selector
            StringBuilder selector = new StringBuilder();
            while (i < linea.length() && linea.charAt(i) != '{') {
                selector.append(linea.charAt(i));
                i++;
            }

            // Si encontramos '{', es el inicio de una regla
            if (i < linea.length() && linea.charAt(i) == '{') {
                String selectorTrimmed = selector.toString().trim();
                if (!selectorTrimmed.isEmpty()) {
                    tokens.add(new Token(selectorTrimmed, "", "CSS", "Selector", 0, 0));
                    i++; // Saltamos '{'

                    // Extraer las propiedades
                    String propiedades = extraerPropiedades(linea, i);
                    if (propiedades != null) {
                        tokens.add(new Token(propiedades, "", "CSS", "Propiedades", 0, 0));
                        i += propiedades.length() + 1; // Saltamos '}'
                    }
                }
            }
        }

        return tokens;
    }

    // Método para extraer las propiedades hasta encontrar '}'
    private String extraerPropiedades(String linea, int startIndex) {
        StringBuilder propiedades = new StringBuilder();
        int i = startIndex;

        while (i < linea.length() && linea.charAt(i) != '}') {
            // Saltar espacios en blanco
            while (i < linea.length() && Character.isWhitespace(linea.charAt(i))) {
                i++;
            }

            // Agregar propiedades válidas
            StringBuilder propiedad = new StringBuilder();
            while (i < linea.length() && linea.charAt(i) != ';' && linea.charAt(i) != '}') {
                propiedad.append(linea.charAt(i));
                i++;
            }

            // Agregar propiedad si no está vacía
            if (propiedad.length() > 0) {
                propiedades.append(propiedad.toString().trim()).append(';');
            }

            // Si encontramos un ';', continuamos
            if (i < linea.length() && linea.charAt(i) == ';') {
                i++; // Saltamos ';'
            }
        }

        // Si encontramos '}', es el final de las propiedades
        if (i < linea.length() && linea.charAt(i) == '}') {
            return propiedades.toString().trim();
        }
        
        return null; // No se encontraron propiedades válidas
    }

    private void guardarTokensValidos(String token) {
        if (!tokensValidos.contains(token)) {
            tokensValidos.add(token);
        }
    }
    
    public List<String> getTokensValidos() {
        return tokensValidos;
    }
}
