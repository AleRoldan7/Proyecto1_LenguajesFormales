import Reportes.Token;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/*
public class GeneradorHTML {

    public void generarArchivoHTML(List<Token> tokensHTML, List<Token> tokensCSS, List<Token> tokensJS, String rutaArchivo) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Estructura básica del archivo HTML
        htmlBuilder.append("<!DOCTYPE html>\n");
        htmlBuilder.append("<html lang=\"es\">\n");
        htmlBuilder.append("<head>\n");
        htmlBuilder.append("    <meta charset=\"UTF-8\">\n");
        htmlBuilder.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        htmlBuilder.append("    <title>Resultados de Análisis</title>\n");
        
        // Añadir la sección de CSS si hay tokens CSS
        if (!tokensCSS.isEmpty()) {
            htmlBuilder.append("    <style>\n");
            for (Token token : tokensCSS) {
                htmlBuilder.append("        ").append(token.getLexema()).append("\n");
            }
            htmlBuilder.append("    </style>\n");
        }
        
        htmlBuilder.append("</head>\n");
        htmlBuilder.append("<body>\n");

        // Añadir los tokens HTML en el cuerpo
        for (Token token : tokensHTML) {
            htmlBuilder.append("    ").append(token.getLexema()).append("\n");
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
}
*/