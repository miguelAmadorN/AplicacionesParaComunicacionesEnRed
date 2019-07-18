package Logica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author miguel angel amador nava
 */
public class AdministradorDeOperacionesMulticast 
{
    private final String IP = "228.1.1.1";
    private final int PUERTO = 3000;
    private MulticastSocket cl;
    private DatagramPacket packet;
    private byte b[];
    private InetAddress grupo = null;
    public final int TAM_BUFFER = 100;
    
    public final String INICIO         = "<inicio>";
    public final String FIN            = "<fin>";
    
    public final static  int DESCONOCIDO_ID    = 0;
    public final static  int INICIO_ID         = 1;
    public final static  int FIN_ID            = 2;
   
    
    private AdministradorDeOperacionesMulticast() 
    {
        try
        {
            cl = new MulticastSocket(PUERTO);
            System.out.println("Cliente conectado desde: " + cl.getLocalPort());
            cl.setReuseAddress(true);
            cl.setTimeToLive(1);
            grupo = InetAddress.getByName(IP);
            cl.joinGroup(grupo);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static AdministradorDeOperacionesMulticast getInstance() {
        return AdministradorDeOperacionesHolder.INSTANCE;
    }
    
    private static class AdministradorDeOperacionesHolder {

        private static final AdministradorDeOperacionesMulticast INSTANCE 
                = new AdministradorDeOperacionesMulticast();
    }
    
    public void anunciar(String nombreOrigen) throws IOException
    {
        String msj = INICIO + nombreOrigen;
        b = msj.getBytes();
        packet = new DatagramPacket(b, b.length, grupo, PUERTO);
        cl.send(packet); 
    }
    
    public void salirDelAnillo(String nombreOrigen) throws IOException
    {
        String msj = FIN + nombreOrigen;
        b = msj.getBytes();
        packet = new DatagramPacket(b, b.length, grupo, PUERTO);
        cl.send(packet); 
    }
    
    public Mensaje recibe() throws IOException
    {
        DatagramPacket packet = new DatagramPacket(new byte[TAM_BUFFER], TAM_BUFFER);
        cl.receive(packet);
        String mensaje = new String(packet.getData());
        return getMensaje(mensaje);
    }
    
    
    private Mensaje getMensaje(String mensaje)
    {
        Mensaje msj = new Mensaje();
        switch(getEtiqueta(mensaje))
        {
            case INICIO:
                msj.setId(INICIO_ID);
                msj.setNombreOrigen(getNodoInicio(mensaje));
                break;
                
            case FIN:
                msj.setId(FIN_ID);
                msj.setNombreOrigen(getNodoFin(mensaje));
                break;
            default:
                msj.setId(DESCONOCIDO_ID);
                break;
        }
        
        return msj;       
    }
    
    private String getEtiqueta(String mensaje)
    {
        int tam = mensaje.length(), i = 0;
        char c;
        String etiqueta = "";
        if (mensaje.charAt(0) == '<') 
        {
            while ((c = mensaje.charAt(i)) != '>' && i < tam) 
            {
                etiqueta += c;
                i++;
            }
            if (c == '>') 
                etiqueta += c;
        }
        return etiqueta;
    }
    
    private String getNodoInicio(String mensaje)
    {
        return mensaje.substring(INICIO.length()).trim();
    }
    
    private String getNodoFin(String mensaje)
    {
        return mensaje.substring(FIN.length()).trim();
    }
    
    
}
