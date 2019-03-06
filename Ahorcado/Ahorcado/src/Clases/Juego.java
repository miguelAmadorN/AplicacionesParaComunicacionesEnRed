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
public class Juego implements Serializable
{

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    private String nombre;
    private int edad;
    private int nivel;

    public Juego(String nombre, int edad, int nivel) {
        this.nombre = nombre;
        this.edad = edad;
        this.nivel = nivel;
    }

    public Juego() {
    }
    
    
}
