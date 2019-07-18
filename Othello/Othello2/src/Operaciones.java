
import afines.AdministradorDeOperacionesDatagrama;
import static afines.AdministradorDeOperacionesDatagrama.BUSCAR;
import static afines.AdministradorDeOperacionesDatagrama.CONECTAR;
import static afines.AdministradorDeOperacionesDatagrama.INICIAR;
import static afines.AdministradorDeOperacionesDatagrama.NO_ENCONTRADO;
import afines.Coordenada;
import afines.MensajeDatagrama;
import afines.MensajeFlujo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import static servidores.Mediador.FIN_JUEGO;
import static servidores.Mediador.INICIAR_JUEGO;
import static servidores.Mediador.MOVIMIENTO;
import static servidores.Mediador.SALIR;
import servidores.Nodo;


/**
 *
 * @author miguel
 */
public class Operaciones 
{

    public String getNickOponente() {
        return nickOponente;
    }

    public boolean isIniciar() {
        return iniciar;
    }
    private final static String IP_SERVIDOR = "127.0.1.1";
    private final static int PUERTO = 9000;
    private Socket cl = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;   
    private String nickOponente;
    private int idOponente;
    private int idJugador;
    private boolean iniciar;
    
    public boolean conectar(String nickName) throws SocketException, IOException, ClassNotFoundException
    {
        MensajeDatagrama m = new MensajeDatagrama();
        m.setNickNameOponente(nickName);
        m.setNodoId(IP_SERVIDOR + ":" + PUERTO);
        
        AdministradorDeOperacionesDatagrama.getInstance(0)
                .enviarMensajeConexion(m, IP_SERVIDOR, PUERTO);
        
        m = AdministradorDeOperacionesDatagrama.getInstance(0).recibe();
      
        if(m.getIdMensaje() == NO_ENCONTRADO)
        {
            return false;
        }
        else if(m.getIdMensaje() == INICIAR)
        {
            nickOponente = m.getNickNameOponente();
            idOponente = m.getIdOponente();
            idJugador = m.getIdJugador();
            cl = new Socket(m.getIp(), m.getPuerto() + 100);
            oos = new ObjectOutputStream(cl.getOutputStream());
            ois = new ObjectInputStream(cl.getInputStream());
            
            MensajeFlujo mensaje = new MensajeFlujo();
            mensaje.setIdMensaje(INICIAR_JUEGO);
            mensaje.setOrigen(idJugador);
            
            oos.writeObject(mensaje);
            oos.flush();
            
            MensajeFlujo m2 = (MensajeFlujo) ois.readObject();
            acciones(m2);
            
            return true;
        }
        
        return false;
    }
    
    
    public void enviarMovimiento(Coordenada coordenada, boolean turno)
            throws IOException
    {
        MensajeFlujo m = new MensajeFlujo();
        m.setIdMensaje(MOVIMIENTO);
        m.setCoordenada(coordenada);
        m.setDestino(idOponente);
        m.setOrigen(idJugador);
        m.setTurno(turno);
        oos.writeObject(m);
        oos.flush();
    }
    
    public MensajeFlujo esperarMensaje() throws IOException, ClassNotFoundException
    {
         MensajeFlujo m = (MensajeFlujo) ois.readObject();
         return m;
    }
    
    public void acciones(MensajeFlujo mensaje)
    {
        switch(mensaje.getIdMensaje())
        {
            case INICIAR_JUEGO:
                iniciar = mensaje.isTurno();
                break;
            case FIN_JUEGO:
                break;
            case MOVIMIENTO:
                break;
            default:
        }
    }
    
    
    public void enviarMensajeSalir() throws IOException
    {
        MensajeFlujo m = new MensajeFlujo();
        m.setIdMensaje(SALIR);
        m.setDestino(idOponente);
        m.setOrigen(idJugador);
        oos.writeObject(m);
        oos.flush();
        oos.close();
        ois.close();
    }
    
    public void clear() throws IOException
    {
        ois.close();
        oos.close();
        cl.close();
    }
    
    
}
