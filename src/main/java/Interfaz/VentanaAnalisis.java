package Interfaz;

import AnalizadorCSS.IdentificadorCSS;
import AnalizadorGeneral.AnalizarGeneral;
import AnalizadorHTML.IdentificadorHTML;
import AnalizadorJS.IdentificadorJS;
import Reportes.Reporte;
import Reportes.Token;
import Reportes.mostrarErrores; // Importar la clase mostrarErrores
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Clase que representa la ventana de análisis de código.
 * Permite cargar texto, analizarlo y generar reportes y HTML.
 * 
 * @author Alejandro
 */
public class VentanaAnalisis extends JFrame implements ActionListener {
    
    private JTextArea areaTexto;
    private JButton botonAnalizar;
    private JButton botonCargarArchivo;
    private JButton botonVerReporte;
    private JButton botonGenerarHTML;  
    private JButton botonReporteError;
    private IdentificadorHTML identificadorHTML = new IdentificadorHTML();
    private IdentificadorCSS identificadorCSS = new IdentificadorCSS();
    private IdentificadorJS identificadorJS = new IdentificadorJS();

    public VentanaAnalisis() {
        // Configurar la ventana principal
        setTitle("Analizador de Código");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el área de texto
        areaTexto = new JTextArea();
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(areaTexto);

        // Crear los botones
        botonAnalizar = new JButton("Analizar Código");
        botonCargarArchivo = new JButton("Cargar Archivo");
        botonVerReporte = new JButton("Ver Reportes");
        botonGenerarHTML = new JButton("Generar HTML"); 
        botonReporteError = new JButton("Reporte Error");
        
        // Agregar los listeners a los botones
        botonAnalizar.addActionListener(this);
        botonCargarArchivo.addActionListener(this);
        botonVerReporte.addActionListener(this);
        botonGenerarHTML.addActionListener(this);  
        botonReporteError.addActionListener(this);
        
        // Crear un panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(1, 5, 10, 10)); // Ajustar para 5 botones
        panelBotones.add(botonAnalizar);
        panelBotones.add(botonCargarArchivo);
        panelBotones.add(botonVerReporte);
        panelBotones.add(botonGenerarHTML);  
        panelBotones.add(botonReporteError);
        
        // Agregar el área de texto y los botones al JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonAnalizar) {
            analizarCodigo();
        } else if (e.getSource() == botonCargarArchivo) {
            cargarArchivo();
        } else if (e.getSource() == botonVerReporte) {
            verReporte();
        } else if (e.getSource() == botonGenerarHTML) {
            generarHTML();
        } else if (e.getSource() == botonReporteError) {
            mostrarReporteErrores();  // Llamada al nuevo método
        }
    }

    private void analizarCodigo() {
        // Obtener el texto ingresado en el JTextArea
        String texto = areaTexto.getText();

        // Verificar si el texto contiene alguna etiqueta de estado válida
        if (!texto.contains("[html]") && !texto.contains("[css]") && !texto.contains("[js]")) {
            JOptionPane.showMessageDialog(this, "Error: No se encontraron etiquetas de estado válidas. Por favor, use [html], [css], o [js].", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear una instancia de la clase AnalizarGeneral
        AnalizarGeneral ag = new AnalizarGeneral();

        // Mostrar los resultados del análisis
        ag.mostrarResultados(texto);
        areaTexto.setText(texto);
    }

    private void verReporte() {
        String texto = areaTexto.getText().trim();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese texto para analizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si el texto contiene alguna etiqueta de estado válida
        if (!texto.contains("[html]") && !texto.contains("[css]") && !texto.contains("[js]")) {
            JOptionPane.showMessageDialog(this, "Error: No se encontraron etiquetas de estado válidas. Por favor, use [html], [css], o [js].", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los tokens válidos de cada analizador
        List<Token> tokensHTML = identificadorHTML.obtenerTokensValidos(texto);
        List<Token> tokensCSS = identificadorCSS.obtenerTokensValidosCSS(texto);
        List<Token> tokensJS = identificadorJS.obtenerTokensValidosJS(texto);

        // Mostrar el reporte
        Reporte reporte = new Reporte();
        reporte.mostrarReporte(tokensHTML, tokensCSS, tokensJS);
    }

    // Método para cargar un archivo de texto y mostrar su contenido en el área de texto
    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int opcion = fileChooser.showOpenDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
                areaTexto.read(lector, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para generar el HTML si el análisis es correcto
    private void generarHTML() {
        String texto = areaTexto.getText().trim();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese texto para analizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los tokens
        List<Token> tokensHTML = identificadorHTML.obtenerTokensValidos(texto);
        List<Token> tokensCSS = identificadorCSS.obtenerTokensValidosCSS(texto);
        List<Token> tokensJS = identificadorJS.obtenerTokensValidosJS(texto);

        // Si no hay errores, generar el archivo HTML
        if (!tokensHTML.isEmpty() || !tokensCSS.isEmpty() || !tokensJS.isEmpty()) {
            // GeneradorHTML generador = new GeneradorHTML();
            // generador.generarHTML(tokensHTML);
            JOptionPane.showMessageDialog(this, "HTML generado exitosamente.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se puede generar HTML, ya que no se encontraron tokens válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para mostrar la ventana de errores
    private void mostrarReporteErrores() {
        mostrarErrores ventanaErrores = new mostrarErrores();
        ventanaErrores.setVisible(true);
    }
}
