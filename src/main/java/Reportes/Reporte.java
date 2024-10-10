package Reportes;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alejandro
 */
public class Reporte {

    // Método para mostrar el reporte de tokens de diferentes analizadores
    public void mostrarReporte(List<Token> tokensHTML, List<Token> tokensCSS, List<Token> tokensJS) {
        
        // Crear un JDialog para mostrar la tabla
        JDialog dialog = new JDialog();
        dialog.setTitle("Reporte de Tokens");
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        dialog.add(panel);

        String[] columnNames = {"Token", "Expresión Regular", "Lenguaje", "Tipo", "Fila", "Columna"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        // Usar un Set para evitar duplicados
        Set<Token> todosLosTokens = new HashSet<>();
        todosLosTokens.addAll(tokensHTML);
        todosLosTokens.addAll(tokensCSS);
        todosLosTokens.addAll(tokensJS);

        // Agregar los tokens únicos al modelo de la tabla
        for (Token token : todosLosTokens) {
            Object[] rowData = {
                token.getLexema(),
                token.getExpresionRegular(), // Suponiendo que Token tiene este método
                token.getLenguaje(),          // Suponiendo que Token tiene este método
                token.getTipo(),              // Suponiendo que Token tiene este método
                token.getFila(),              // Suponiendo que Token tiene este método
                token.getColumna()            // Suponiendo que Token tiene este método
            };
            tableModel.addRow(rowData);
        }

        // Agregar etiquetas de estado si no hay tokens
        if (tokensHTML.isEmpty() && tokensCSS.isEmpty() && tokensJS.isEmpty()) {
            agregarEstado(tableModel, "[html]", null);
            agregarEstado(tableModel, "[css]", null);
            agregarEstado(tableModel, "[js]", null);
        } else {
            // Agregar etiquetas de estado
            agregarEstado(tableModel, "[html]", tokensHTML);
            agregarEstado(tableModel, "[css]", tokensCSS);
            agregarEstado(tableModel, "[js]", tokensJS);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dialog.dispose());
        panel.add(closeButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // Método para agregar etiquetas de estado al modelo de la tabla
    private void agregarEstado(DefaultTableModel tableModel, String lenguaje, List<Token> tokens) {
        // Agregar una fila que indica el lenguaje
        Object[] rowData = {
            lenguaje, 
            "", // Expresión Regular
            lenguaje, // Lenguaje
            "Estado", // Tipo
            "", // Fila
            ""  // Columna
        };
        tableModel.addRow(rowData);
    }
}
