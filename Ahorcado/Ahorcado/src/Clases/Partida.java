/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;

/**
 *
 * @author miguel
 */
public class Partida implements Serializable{

    public int getErrores() {
        return errores;
    }

    public void setErrores(int errores) {
        this.errores = errores;
    }

    public boolean isFinPartida() {
        return finPartida;
    }

    public void setFinPartida(boolean finPartida) {
        this.finPartida = finPartida;
    }

    public boolean isGanador() {
        return ganador;
    }

    public void setGanador(boolean ganador) {
        this.ganador = ganador;
    }

    public char getLetra() {
        return letra;
    }

    public void setLetra(char letra) {
        this.letra = letra;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public int[] getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(int[] posiciones) {
        this.posiciones = posiciones;
    }

    
    private int errores;
    private boolean finPartida;
    private boolean ganador;
    private char letra;
    private transient String palabra;
    private int posiciones[];

    public Partida(int errores, boolean finPartida, boolean ganador, char letra, 
            String palabra, int[] posiciones) 
    {
        this.errores = errores;
        this.finPartida = finPartida;
        this.ganador = ganador;
        this.letra = letra;
        this.palabra = palabra;
        this.posiciones = posiciones;
    }

    

    public Partida() {
    }
    
    
}
