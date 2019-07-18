
package afines;

import afines.MensajeDatagrama;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author miguel
 */
public class AdministradorDeOperacionesDatagrama 
{
    public static final int BUSCAR        = 1;
    public static final int ENCONTRADO    = 2;
    public static final int NO_ENCONTRADO = 3;
    public static final int CONECTAR      = 4;
    public static final int INICIAR       = 5;
    
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
    
    
    public MensajeDatagrama recibe() throws IOException
    {
        DatagramPacket p = new DatagramPacket(new byte[TAM], TAM);
	datagramaSocket.receive(p);
        return getMensaje(new String(p.getData()));
    }
    
    private MensajeDatagrama getMensaje(String data)
    {
        char separador = '$', c;
        int i = 0;
        String aux = "";
        MensajeDatagrama mensaje = new MensajeDatagrama();
        while((c = data.charAt(i)) != separador )
        {
            aux += c;
            i++;
        }
        mensaje.setIdMensaje(Integer.parseInt(aux));
        data = data.substring(i + 1);
        i = 0;
        aux = "";
        while((c = data.charAt(i)) != separador )
        {
            aux += c;
            i++;
        }
        mensaje.setIdJugador(Integer.parseInt(aux));
        
        data = data.substring(i + 1);
        i = 0;
        aux = "";
        while((c = data.charAt(i)) != separador )
        {
            aux += c;
            i++;
        }
        mensaje.setNickNameOponente(aux);
        
        data = data.substring(i + 1);
        i = 0;
        aux = "";
        while((c = data.charAt(i)) != separador )
        {
            aux += c;
            i++;
        }
        mensaje.setIdOponente(Integer.parseInt(aux));
        
        data = data.substring(i + 1);
        i = 0;
        aux = "";
        while((c = data.charAt(i)) != separador )
        {
            aux += c;
            i++;
        }
        mensaje.setPuerto(Integer.parseInt(aux));
        
        data = data.substring(i + 1);
        i = 0;
        aux = "";
        while((c = data.charAt(i)) != separador )
        {
            aux += c;
            i++;
        }
        mensaje.setIp(aux);
        
        data = data.substring(i + 1);
        i = 0;
        aux = "";
        while((c = data.charAt(i)) != separador )
        {
            aux += c;
            i++;
        }
        mensaje.setNodoId(aux);
        
        return mensaje;
    }
    
    public void enviarMensaje(MensajeDatagrama m, String destino, int puerto ) 
            throws IOException
    {
        byte b[] = m.getCadenaMensaje().getBytes();
        DatagramPacket p = new DatagramPacket(b, b.length,
	InetAddress.getByName(destino), puerto);
	datagramaSocket.send(p);
    }
    
    public void enviarMensajeConexion(MensajeDatagrama m, String destino, int puerto) 
            throws UnknownHostException, IOException
    {
        m.setPuerto(datagramaSocket.getLocalPort());
        m.setIp(InetAddress.getLocalHost().getHostAddress());
        m.setIdMensaje(CONECTAR);
        byte b[] = m.getCadenaMensaje().getBytes();
        DatagramPacket p = new DatagramPacket(b, b.length,
	InetAddress.getByName(destino), puerto);
	datagramaSocket.send(p);
    }
}
