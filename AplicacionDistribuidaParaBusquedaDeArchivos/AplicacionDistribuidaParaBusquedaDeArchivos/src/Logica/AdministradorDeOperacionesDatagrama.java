/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

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
    public static final int ENCONTRADO    = 2;
    
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
    
    
    public Mensaje recibe() throws IOException
    {
        DatagramPacket p = new DatagramPacket(new byte[TAM], TAM);
	datagramaSocket.receive(p);
        return getMensaje(new String(p.getData()));
    }
    
    
    private Mensaje getMensaje(String data)
    {
        char separador = '$', c;
        int i = 0;
        String aux = "";
        Mensaje mensaje = new Mensaje();
        while((c = data.charAt(i)) != separador )
        {
            aux += c;
            i++;
        }
        mensaje.setId(Integer.parseInt(aux));
        data = data.substring(i + 1);
        i = 0;
        aux = "";
        while((c = data.charAt(i)) != separador )
        {
            aux += c;
            i++;
        }
        mensaje.setNombreOrigen(aux);
        
        data = data.substring(i + 1);
        i = 0;
        aux = "";
        while((c = data.charAt(i)) != separador )
        {
            aux += c;
            i++;
        }
        mensaje.setNombreDestino(aux);
        
        data = data.substring(i + 1);
        i = 0;
        aux = "";
        while((c = data.charAt(i)) != separador )
        {
            aux += c;
            i++;
        }
        mensaje.setMensaje(aux);
        
        return mensaje;
    }
    
    public void enviarMensaje(Mensaje m, String destino, int puerto ) throws IOException
    {
        byte b[] = m.getCadenaMensaje().getBytes();
        DatagramPacket p = new DatagramPacket(b, b.length,
	InetAddress.getByName(destino), puerto);
	datagramaSocket.send(p);
    }
}
