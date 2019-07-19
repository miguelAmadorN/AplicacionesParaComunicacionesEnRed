/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Logica.Busqueda.Busqueda;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author miguel
 */
public class AdministradorDeOperacionesDatagrama 
{

    
    public static final int BUSCAR        = 1;
    public static final int RESPUESTA    = 2;
    
    private final static int TAM = 1024;
    private static AdministradorDeOperacionesDatagrama 
                        INSTANCE = null;
    
    DatagramSocket datagramaSocket = null;
    private AdministradorDeOperacionesDatagrama(int puerto) 
            throws SocketException 
    {
        datagramaSocket =  new DatagramSocket(puerto);

        
    }
    
    public static AdministradorDeOperacionesDatagrama getInstance(int puerto) 
            throws SocketException 
    {
        if(INSTANCE == null)
        {
            INSTANCE =  new AdministradorDeOperacionesDatagrama(puerto);
        }
        return AdministradorDeOperacionesDatagrama.INSTANCE;
    }
    
    
    public Busqueda recibe() throws IOException
    {
        DatagramPacket p = new DatagramPacket(new byte[TAM], TAM);
	datagramaSocket.receive(p);
        return getBusqueda(new String(p.getData()));
    }
    
    
    private Busqueda getBusqueda(String data)
    {
        return Busqueda.getBusqueda(data);
    }
    
    public void enviarMensaje(Busqueda m, String destino, int puerto ) throws IOException
    {
        byte b[] = m.getCadena().getBytes();
        DatagramPacket p = new DatagramPacket(b, b.length,
	InetAddress.getByName(destino), puerto);
	datagramaSocket.send(p);
    }
}
