/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Inicio;

import AnalizadorHTML.IdentificadorHTML;
import AnalizadorHTML.TraductorEtiquetas;
import Interfaz.VentanaAnalisis;
import java.util.Scanner;

/**
 *
 * @author alejandro
 */
public class Proyecto1AnalizadorLexico {

    public static void main(String[] args) {
        
        
        VentanaAnalisis vn = new VentanaAnalisis();
        vn.setVisible(true);
        vn.setLocationRelativeTo(null);
        
        /*
        JFrameInicio jf = new JFrameInicio();
        jf.setSize(760, 687);
        jf.setVisible(true);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrameInicio.EXIT_ON_CLOSE);    
        */
        /*
        Scanner scanner = new Scanner(System.in);
        String a;
        TraductorEtiquetas te = new TraductorEtiquetas();
        System.out.println("Ingrese etiqueta ");
        a = scanner.nextLine();
        
        System.out.println("Traducida: "+te.traducirEtiqueta(a));
        */
        
    }     
}
