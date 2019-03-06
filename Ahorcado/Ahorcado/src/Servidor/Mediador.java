/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Clases.Juego;
import Clases.Operaciones;
import Clases.Operacion;
import Clases.Partida;
import static Servidor.Servidor.RUTA_PALABRAS;
import java.awt.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author miguel
 */
public class Mediador 
{
    
    private Palabras palabras = null;
    private int SEPARADOR = 5;
    private final int FACIL = 0;
    private final int DIFICIL = 1;
 
    private Mediador() {
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(RUTA_PALABRAS));
            palabras = (Palabras) in.readObject();
            in.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static Mediador getInstance() {
        return MediadorHolder.INSTANCE;
    }
    
    private static class MediadorHolder {
       
        private static final Mediador INSTANCE = new Mediador();
        
    }
    
    /**
     * @param oos
     * @param op un objeto que encupsula un id para la oprecion solicitada
     * las constantes se encuentran en la clase Operaciones
     */
    public boolean ejecutarOperacion(ObjectOutputStream oos, ObjectInputStream ois, Operacion op, 
            Partida partida) throws IOException, ClassNotFoundException
    {
        switch(op.getOperacion())
        {
            case Operaciones.VALIDAR_JUEGO:
                validarJuego(oos, ois, partida);
                return true;
            case Operaciones.EVALUAR_LETRA:
                evaluarLetra( oos, ois, partida ); 
                return true;
            case Operaciones.TERMINAR_CONEXION:
                return false;   
            default:
                return true;
        }
    }
    
    private int[] posicionesDeLaLetra(char letra, String palabra)
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        int posiciones[] = null;
        int tam = palabra.length();
        int i;
        for(i = 0; i < tam; i++)
        {
            if(letra == palabra.charAt(i))
            {
                list.add(i);
            }
        }
        tam = list.size();
        if(tam > 0)
        {
            posiciones = new int[tam];
            for(i = 0; i < tam; i++)
            {
                posiciones[i] = list.get(i);
            }
        }
        return posiciones;
    }
    
    private boolean juegoCompletado(String palabra)
    {
        int tam = palabra.length();
        for(int i = 0; i < tam ; i++)
        {
            if(palabra.charAt(i) != '1')
                return false;
        }
        return true;
    }
    
    private String eliminarLetraDeLaPalabra(char letra, String palabra)
    {
        for(int i = 0; i < palabra.length(); i++)
        {
            if(palabra.charAt(i) == letra)
            {
                palabra = palabra.replace(letra, '1');
            }
        }
        return palabra;
    }
    
    private void evaluarLetra(ObjectOutputStream oos, ObjectInputStream ois, Partida partida1)
            throws IOException, ClassNotFoundException
    {
        Partida partida = new Partida();
        partida.setErrores(partida1.getErrores());
        int errores = partida.getErrores();
        char letra = (char) ois.readChar();
        int posiciones[] = posicionesDeLaLetra(letra, partida1.getPalabra());
        if(posiciones != null)
        {
            partida1.setPalabra(eliminarLetraDeLaPalabra(letra, partida1.getPalabra()));
            partida.setPosiciones(posiciones);
            if(juegoCompletado(partida1.getPalabra()))
            {
                partida.setFinPartida(true);
                partida.setGanador(true);
            }
        }
        else
        {
            partida.setErrores(++errores);
            partida1.setErrores(partida.getErrores());
            partida.setPosiciones(null);
            if(partida.getErrores() > 3)
            {
                partida.setFinPartida(true);
                partida.setGanador(false);
                System.out.print("Perdedor");
            }
            else
            {
                partida.setFinPartida(false);
            }
        }
        oos.writeObject(partida);
        oos.flush();
    }
      
    
    private void validarJuego(ObjectOutputStream oos, ObjectInputStream ois, Partida partida)
            throws IOException, ClassNotFoundException
    {
        Juego juego = (Juego) ois.readObject();
        mostrarJugador(juego);
        String palabra = conseguirPalabra(juego.getNivel());
        System.out.println(palabra);
        oos.writeInt(palabra.length());
        oos.flush();
        //inicializamos la partida
        partida.setErrores(0);
        partida.setPalabra(palabra.trim().toUpperCase());
        partida.setFinPartida(false);
        
    }
    
    private void mostrarJugador(Juego juego)
    {
        System.out.println("Jugador: " + juego.getNombre() + "\n" +
                            "Edad: " + juego.getEdad() + "\n" +
                            "Dificultad " + (juego.getNivel() == DIFICIL ? "Dificil" : "Facil"));
    }
    
    private String conseguirPalabra(int dificultad)
    {
        String palabra = null;
        int tam = palabras.getPalabras().length;
        if(dificultad == FACIL)//
        {
            do
            {
                palabra = palabras.getPalabras()[(int) (Math.random() * tam)];
            }while(palabra.length() > SEPARADOR);
        }
        else if(dificultad == DIFICIL)
        {
            do
            {
                palabra = palabras.getPalabras()[(int) (Math.random() * tam)];   
            }while(palabra.length() <= SEPARADOR);
        }
        return palabra;
    }
}