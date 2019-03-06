/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.Serializable;

/**
 *
 * @author miguel
 */
public class Palabras implements Serializable
{

    public String[] getPalabras() {
        return palabras;
    }

    public void setPalabras(String[] palabras) {
        this.palabras = palabras;
    }
    private String palabras[];

    public Palabras(String[] palabras) {
        this.palabras = palabras;
    }

    public Palabras() {
    }
    
    
    
}
