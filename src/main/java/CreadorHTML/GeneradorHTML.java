package CreadorHTML;

import Reportes.Token;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Clase para generar un archivo HTML basado en los tokens de HTML.
 * 
 * @author Alejandro
 */
public class GeneradorHTML {

    // Método que genera el HTML basado en los tokens de HTML
    public void generarHTML(List<Token> tokensHTML) {
        // Estructura básica del archivo HTML
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("<meta charset=\"UTF-8\">\n");
        html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("<title>Página Generada</title>\n");
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
        html.append("</html>");

        // Guardar el HTML en un archivo
        guardarArchivo(html.toString());

        // Guardar los tokens en un archivo de texto
        guardarTokensEnArchivo(tokensHTML);
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

    // Método que guarda los tokens en un archivo de texto
    private void guardarTokensEnArchivo(List<Token> tokensHTML) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tokensGenerados.txt"))) {
            writer.write("Tokens Generados:\n");
            for (Token token : tokensHTML) {
                writer.write(token.getLexema() + "\n");
            }
            System.out.println("Tokens guardados en 'tokensGenerados.txt' exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de tokens: " + e.getMessage());
        }
    }
}
