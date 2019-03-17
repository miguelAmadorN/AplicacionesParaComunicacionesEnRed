package serviciodechat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author miguel angel amador nava
 */
public class AdministradorDeOperaciones 
{
    private final String IP = "230.1.1.1";
    private final int PUERTO = 4000;
    private MulticastSocket cl;
    private DatagramPacket packet;
    private byte b[];
    private InetAddress grupo = null;
    public final int TAM_BUFFER = 100;
    
    private final String INICIO          = "<inicio>";
    private final String MENSAJE_PRIVADO = "<privado>";
    private final String MENSAJE_PUBLICO = "<msj>";
    private final String FIN             = "<fin>";
    public final int DESCONOCIDO_ID     = 0;
    public final int INICIO_ID          = 1;
    public final int MENSAJE_PRIVADO_ID = 2;
    public final int MENSAJE_PUBLICO_ID = 3;
    public final int FIN_ID             = 4;
   
    
    private AdministradorDeOperaciones() 
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
    
    public static AdministradorDeOperaciones getInstance() {
        return AdministradorDeOperacionesHolder.INSTANCE;
    }
    
    private static class AdministradorDeOperacionesHolder {

        private static final AdministradorDeOperaciones INSTANCE = new AdministradorDeOperaciones();
    }
    
    public void iniciarSesion(String nombreOrigen) throws IOException
    {
        String msj = "<inicio>" + nombreOrigen;
        b = msj.getBytes();
        packet = new DatagramPacket(b, b.length, grupo, PUERTO);
        cl.send(packet); 
    }
    //Con espacios solucionamos de manera temporal el analisis de la cadena
    public void mensajeASala(String mensaje, String nombreOrigen) throws IOException
    {
        String msj = "<msj>" + "<" + nombreOrigen + ">" + " " + mensaje + " ";
        b = msj.getBytes();
        packet = new DatagramPacket(b, b.length, grupo, PUERTO);
        cl.send(packet); 
    }
    
    public void mensajePrivado(String nombreOrigen , String nombreDestino, String mensaje) 
            throws IOException
    {
        String msj = "<privado>" + "<" + nombreOrigen + ">" +  "<" + nombreDestino + ">"
                     + " " + mensaje + " ";
        b = msj.getBytes();
        packet = new DatagramPacket(b, b.length, grupo, PUERTO);
        cl.send(packet); 
    } 
    
    public void salirSesion(String nombreOrigen) throws IOException
    {
        String msj = "<fin>" + nombreOrigen;
        b = msj.getBytes();
        packet = new DatagramPacket(b, b.length, grupo, PUERTO);
        cl.send(packet); 
    }
    
    /**
     * La práctica solicita etiquetas para hacer operaciones 
     * pero en el datagrama es mas elegante envíar el numero de
     * operacion y tener un responsable que ejecute la operacion
     * correspondiente
     * @return 
     */
    public Mensaje recibe() throws IOException
    {
        DatagramPacket packet = new DatagramPacket(new byte[TAM_BUFFER], TAM_BUFFER);
        cl.receive(packet);
        String mensaje = new String(packet.getData());
        System.out.print(mensaje + "\n");
        return getMensaje(mensaje);
    }
    
    
    private Mensaje getMensaje(String mensaje)
    {
        Mensaje msj = new Mensaje();
        switch(getEtiqueta(mensaje))
        {
            case INICIO:
                msj.setId(INICIO_ID);
                msj.setNombreOrigen(getUsuarioDeLaSesion(mensaje));
                break;
            
            case MENSAJE_PUBLICO:
                msj.setId(MENSAJE_PUBLICO_ID);
                String msjpublico[] = getUsuarioMensajeDeOrigenPublico(mensaje);
                if(msjpublico != null)
                {
                    msj.setNombreOrigen(msjpublico[0]);
                    msj.setMensaje(msjpublico[1]);
                }
                break;
                
            case MENSAJE_PRIVADO:
                msj.setId(MENSAJE_PRIVADO_ID);
                String msjprivado[] = getUsuariosMensajeDeOrigenPrivado(mensaje);
                if(msjprivado != null)
                {
                    msj.setNombreOrigen(msjprivado[0]);
                    msj.setNombreDestino(msjprivado[1]);
                    msj.setMensaje(msjprivado[2]);
                }
                break;
                
            case FIN:
                msj.setId(FIN_ID);
                msj.setNombreOrigen(getUsuarioDeSalidaSesion(mensaje));
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
    
    private String getUsuarioDeLaSesion(String mensaje)
    {
        return mensaje.substring(INICIO.length()).trim();
    }
    
    private String getUsuarioDeSalidaSesion(String mensaje)
    {
        return mensaje.substring(FIN.length()).trim();
    }
    
    private String[] getUsuarioMensajeDeOrigenPublico(String mensaje)
    {
        mensaje = mensaje.substring(MENSAJE_PUBLICO.length());
        String usuario = "", msj = "";
        char c;
        if(mensaje.charAt(0) == '<')
        {
            int i = 1;
            while((c = mensaje.charAt(i)) != '>' && i < mensaje.length())
            {
                usuario += c;
                i++;
            }
            i++;
            if(i < mensaje.length())
                msj = mensaje.substring(i);
            
            return new String[]{usuario, msj};
        }
        else
            return null;
    }
    
    private String[] getUsuariosMensajeDeOrigenPrivado(String mensaje)
    {
        mensaje = mensaje.substring(MENSAJE_PRIVADO.length());
        String origen = "", destino = "", msj = "";
        char c;
        int i = 1;
        if(mensaje.charAt(0) == '<')
        {
            while((c = mensaje.charAt(i)) != '>' && i < mensaje.length())
            {
                origen += c;
                i++;
            }
        }
        else
            return null;
        i++;
        if(i < mensaje.length() && mensaje.charAt(i) == '<')
        {
            i++;
            while((c = mensaje.charAt(i)) != '>' && i < mensaje.length())
            {
                destino += c;
                i++;
            }
        }
        else
            return null;
        i++;
        if(i < mensaje.length())
            msj = mensaje.substring(i);
        
        return new String[]{origen, destino, msj};
            
    }
}
