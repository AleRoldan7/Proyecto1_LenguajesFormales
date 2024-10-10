package AnalizadorGeneral;

import Reportes.Token;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class GeneradorHTML {

    private List<String> etiquetasEnEspanol;

    public GeneradorHTML() {
        // Inicializa las etiquetas en español
        etiquetasEnEspanol = etiquetasNormales();
    }

    // Método para inicializar etiquetas normales
    public ArrayList<String> etiquetasNormales() {
        ArrayList<String> etiqueta = new ArrayList<>();
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

    public void generarArchivoHTML(List<Token> tokensHTML, List<Token> tokensCSS, List<Token> tokensJS, String rutaArchivo) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Estructura básica del archivo HTML
        htmlBuilder.append("<!DOCTYPE html>\n");
        htmlBuilder.append("<html lang=\"es\">\n");
        htmlBuilder.append("<head>\n");
        htmlBuilder.append("    <meta charset=\"UTF-8\">\n");
        htmlBuilder.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        htmlBuilder.append("    <title>Resultados de Análisis</title>\n");
        
        // Agregar CSS solo si hay tokens
        if (!tokensCSS.isEmpty()) {
            htmlBuilder.append("    <style>\n");
            for (Token token : tokensCSS) {
                htmlBuilder.append("        ").append(token.getLexema()).append("\n");
            }
            htmlBuilder.append("    </style>\n");
        }

        htmlBuilder.append("</head>\n");
        htmlBuilder.append("<body>\n");

        // Añadir los tokens HTML en el cuerpo, filtrando las etiquetas en español
        for (Token token : tokensHTML) {
            if (!esEtiquetaEnEspanol(token.getLexema())) {
                htmlBuilder.append("    ").append(token.getLexema()).append("\n");
            }
        }
        
        // Añadir la sección de JavaScript si hay tokens JS
        if (!tokensJS.isEmpty()) {
            htmlBuilder.append("    <script>\n");
            for (Token token : tokensJS) {
                htmlBuilder.append("        ").append(token.getLexema()).append("\n");
            }
            htmlBuilder.append("    </script>\n");
        }

        htmlBuilder.append("</body>\n");
        htmlBuilder.append("</html>");

        // Escribir el contenido generado en un archivo HTML
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(htmlBuilder.toString());
            System.out.println("Archivo HTML generado correctamente en: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al generar el archivo HTML: " + e.getMessage());
        }
    }

    // Método para verificar si una etiqueta es en español
    private boolean esEtiquetaEnEspanol(String lexema) {
        return etiquetasEnEspanol.contains(lexema);
    }
}
