/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reportes;

import java.util.Objects;

/**
 *
 * @author alejandro
 */
public class Token {
    
    private String lexema;
    private String expresionRegular;
    private String lenguaje;
    private String tipo;
    private int fila;
    private int columna;

  
    public Token(String lexema, String expresionRegular, String lenguaje, String tipo, int fila, int columna) {
        this.lexema = lexema;
        this.expresionRegular = expresionRegular;
        this.lenguaje = lenguaje;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
    }

    public String getLexema() {
        return lexema;
    }

    public String getExpresionRegular() {
        return expresionRegular;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public String getTipo() {
        return tipo;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Token)) return false;
        Token other = (Token) obj;
        return lexema.equals(other.lexema) && 
               lenguaje.equals(other.lenguaje) &&
               fila == other.fila && 
               columna == other.columna;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexema, lenguaje, fila, columna);
    }


    
}
