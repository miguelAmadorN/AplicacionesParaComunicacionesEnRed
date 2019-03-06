/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Clases.Operacion;
import Clases.Partida;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author miguel
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    private static final boolean EVER = true;
    protected static final String RUTA_PALABRAS = "Archivos/Palabras.out";
    
    
    public static void run() throws IOException, ClassNotFoundException
    {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        File file = new File(RUTA_PALABRAS);
        Palabras palabras = null;
        if(!file.exists())
        {
            palabras = new Palabras(PalabrasDelJuego.getPalabras());
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(RUTA_PALABRAS));
            o.writeObject(palabras);
            o.close();
        }
        
        try 
        {
            ServerSocket s = new ServerSocket(9999);
            System.out.print("Servidor iniciado...\n");
            Mediador mediador = Mediador.getInstance();
            
            for (;EVER;) 
            {
                Socket cl = s.accept();
                System.out.print("Cliente conectado desde "
                        + cl.getInetAddress() + ":" + cl.getPort() + "\n");
                oos = new ObjectOutputStream(cl.getOutputStream());
                ois = new ObjectInputStream(cl.getInputStream());
                boolean seguir = true;
                Partida partida = new Partida();
                partida.setErrores(0);
                partida.setFinPartida(false);
                while(seguir)
                {
                    Operacion op = (Operacion) ois.readObject();
                    seguir = mediador.ejecutarOperacion(oos, ois, op, partida);
                }
                    
                /**
                 * El GestorDeDatos es el encargado de recibir la operacion
                 * ejecutarla y devolver lo solicitado
                 */    

            }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        Servidor.run();
    }
    
}
