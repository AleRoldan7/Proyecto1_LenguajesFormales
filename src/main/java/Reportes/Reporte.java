/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reportes;


import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
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

        // Combinar las listas de tokens de diferentes analizadores
        List<Token> todosLosTokens = new ArrayList<>();
        todosLosTokens.addAll(tokensHTML);
        todosLosTokens.addAll(tokensCSS);
        todosLosTokens.addAll(tokensJS);

        // Agregar los tokens al modelo de la tabla
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

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dialog.dispose());
        panel.add(closeButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
