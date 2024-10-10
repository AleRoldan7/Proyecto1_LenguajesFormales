/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reportes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alejandro
 */
public class ReporteErrores {
    private String token;
    private String lenguajeDondeSeEncontro;
    private String lenguajeSugerido;
    private int fila;
    private int columna;

    public ReporteErrores(String token, String lenguajeDondeSeEncontro, String lenguajeSugerido, int fila, int columna) {
        this.token = token;
        this.lenguajeDondeSeEncontro = lenguajeDondeSeEncontro;
        this.lenguajeSugerido = lenguajeSugerido;
        this.fila = fila;
        this.columna = columna;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getLenguajeDondeSeEncontro() {
        return lenguajeDondeSeEncontro;
    }

    public String getLenguajeSugerido() {
        return lenguajeSugerido;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}
