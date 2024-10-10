package Reportes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class mostrarErrores extends JFrame {

    private JTable tablaErrores;
    private DefaultTableModel modeloTabla;

    public mostrarErrores() {
        setTitle("Errores Encontrados");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear modelo de la tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Número de Línea");
        modeloTabla.addColumn("Columna");
        modeloTabla.addColumn("Descripción del Error");

        // Crear la tabla
        tablaErrores = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaErrores);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Método para agregar un error a la tabla
    public void agregarError(int linea, int columna, String descripcion) {
        modeloTabla.addRow(new Object[]{linea, columna, descripcion});
    }

    // Método para mostrar la ventana con los errores
    public static void mostrarErroresVentana() {
        mostrarErrores ventanaErrores = new mostrarErrores();
        ventanaErrores.setVisible(true);
    }
}
