/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Clases.Juego;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import Clases.Operacion;
import Clases.Operaciones;
import Clases.Partida;
/**
 *
 * @author miguel
 */
public class AdministradorDeOperaciones {
   
    
    public static void finalizarConexion(ObjectOutputStream oos, ObjectInputStream ois) 
            throws IOException
    {
        oos.writeObject(new Operacion(Operaciones.TERMINAR_CONEXION, null));
        oos.flush();
    }
    
    public static int iniciarJuego(ObjectOutputStream oos, ObjectInputStream ois, Juego juego) 
            throws IOException, ClassNotFoundException
    {
        oos.writeObject(new Operacion(Operaciones.VALIDAR_JUEGO, null));
        oos.flush();
        oos.writeObject(juego);
        oos.flush();
        return (int) ois.readInt();
    }
    
    public static Partida evaluarLetra(ObjectOutputStream oos, ObjectInputStream ois, char letra) 
            throws IOException, ClassNotFoundException
    {
        oos.writeObject(new Operacion(Operaciones.EVALUAR_LETRA, null));
        oos.flush();
        oos.writeChar(letra);
        oos.flush();
        return (Partida) ois.readObject();
    }
    
}
