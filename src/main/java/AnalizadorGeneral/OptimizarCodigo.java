/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorGeneral;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alejandro
 */
public class OptimizarCodigo {
    
 
    public static List<String> optimizarCodigo(List<String> codigoFuente) {
        List<String> codigoOptimizado = new ArrayList<>();

        for (String linea : codigoFuente) {
            String lineaOptimizada = linea.trim(); // Elimina espacios en blanco al inicio y final

            if (lineaOptimizada.isEmpty() || lineaOptimizada.startsWith("//")) {
                // Saltar línea vacía o que solo tiene un comentario
                continue;
            }
            
            // Si la línea tiene código y un comentario en la misma línea, opcionalmente podemos mantenerla.
            codigoOptimizado.add(linea);
        }
        
        return codigoOptimizado;
    }

}
