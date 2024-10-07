package CreadorHTML;

import Reportes.Token;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author alejandro
 */
public class GeneradorHTML {

    // Método que genera el HTML basado en los tokens de HTML, CSS y JS
    public void generarHTML(List<Token> tokensHTML, List<Token> tokensCSS, List<Token> tokensJS) {
        // Estructura básica del archivo HTML
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("<meta charset=\"UTF-8\">\n");
        html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("<title>Página Generada</title>\n");

        // Incluir los estilos CSS dentro de la etiqueta <style>
        html.append("<style>\n");
        for (Token tokenCSS : tokensCSS) {
            html.append(tokenCSS.getLexema()).append("\n"); // Asegúrate de que cada token tenga el formato adecuado
        }
        html.append("</style>\n");

        html.append("</head>\n");
        html.append("<body>\n");

        // Comentario que indica dónde se agregarán los tokens HTML
        html.append("<!-- Inicio de los tokens HTML -->\n");
        
        // Incluir los tokens HTML dentro del body
        for (Token tokenHTML : tokensHTML) {
            html.append(tokenHTML.getLexema()).append("\n");
        }
        
        html.append("<!-- Fin de los tokens HTML -->\n");

        html.append("</body>\n");

        // Incluir los scripts JS dentro de la etiqueta <script>
        html.append("<script>\n");
        
        // Comentario que indica dónde se agregarán los tokens JS
        html.append("<!-- Inicio de los tokens JS -->\n");
        for (Token tokenJS : tokensJS) {
            html.append(tokenJS.getLexema()).append("\n");
        }
        html.append("<!-- Fin de los tokens JS -->\n");

        html.append("</script>\n");

        html.append("</html>");

        // Guardar el HTML en un archivo
        guardarArchivo(html.toString());
    }

    // Método que guarda el HTML generado en un archivo .html
    private void guardarArchivo(String contenido) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("paginaGenerada.html"))) {
            writer.write(contenido);
            System.out.println("HTML generado y guardado exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo HTML: " + e.getMessage());
        }
    }
}
