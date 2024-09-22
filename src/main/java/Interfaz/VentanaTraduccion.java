/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author alejandro
 */
public class VentanaTraduccion extends JFrame{
    
    public VentanaTraduccion(String textoTraducido) {
        setTitle("Traducción");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTextArea textArea = new JTextArea();
        textArea.setText(textoTraducido);
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);
        
        setLocationRelativeTo(null); // Centrar la ventana
        setVisible(true);
    }
}
