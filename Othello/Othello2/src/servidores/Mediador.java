
package servidores;

import afines.AdministradorDeOperacionesDatagrama;
import static afines.AdministradorDeOperacionesDatagrama.BUSCAR;
import static afines.AdministradorDeOperacionesDatagrama.CONECTAR;
import static afines.AdministradorDeOperacionesDatagrama.INICIAR;
import static afines.AdministradorDeOperacionesDatagrama.NO_ENCONTRADO;
import afines.MensajeDatagrama;
import afines.MensajeFlujo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static servidores.AdministradorDeOperacionesMulticast.FIN_ID;
import static servidores.AdministradorDeOperacionesMulticast.INICIO_ID;

/**
 *
 * @author miguel
 */
public class Mediador 
{
    public final static int INICIAR_JUEGO   = 1;
    public final static  int FIN_JUEGO      = 2;
    public final static  int MOVIMIENTO     = 3;
    public final static  int SALIR          = 4;
    private ServerSocket s = null;

    
    private static Mediador INSTANCE = null;
    ArrayList jugadores;
    private final int MAX_JUGADORES = 2;
    private int numeroJugadores;
    private int ids;
    private ArrayList partidas;
    private int puerto;
    
    
    private ArrayList nodos;
    private Nodo nodoSiguiente;
    
    private Mediador(int puerto) throws IOException 
    {
        this.puerto     = puerto;
        partidas        = new ArrayList();
        nodos           = new ArrayList();
        jugadores       = new ArrayList();
        numeroJugadores = 0;
        
        s = new ServerSocket(puerto + 100);
    }
    
    public static Mediador getInstance(int puerto) throws IOException 
    {
        if(INSTANCE == null)
            INSTANCE = new Mediador(puerto);
        return Mediador.INSTANCE;
    }
    
     
    public void actualizarDatagrama(MediadorListener ml, String nodoId) throws IOException
    {
        MensajeDatagrama mensaje = AdministradorDeOperacionesDatagrama
                            .getInstance(puerto).recibe();
        //mensaje.imprimir();
        switch (mensaje.getIdMensaje()) 
        {
            case CONECTAR:
                if (numeroJugadores < MAX_JUGADORES) 
                {
                    if(ml != null)
                    {
                        ml.mensaje("Alojado en este nodo");
                    }
                    agregarJugador(mensaje);
                    
                } else {
                    if (nodos.size() > 0) 
                    {

                        try {
                            mensaje.setIdMensaje(BUSCAR);
                            AdministradorDeOperacionesDatagrama.getInstance(puerto)
                                    .enviarMensaje(mensaje, nodoSiguiente.getIp(),
                                            nodoSiguiente.getPuerto());

                        } catch (SocketException ex) {
                            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } 
                    else 
                    {
                        if(ml != null)
                        {
                            ml.mensaje("No hay servidores disponibles");
                        }
                        mensaje.setIdMensaje(NO_ENCONTRADO);
                        AdministradorDeOperacionesDatagrama.getInstance(puerto)
                                .enviarMensaje(mensaje, mensaje.getIp(), mensaje.getPuerto());
                    }
                }

                break;
            case BUSCAR:
                if (mensaje.getNodoId().equals(nodoId)) 
                {
                    if(ml != null)
                    {
                            ml.mensaje("No hay servidores disponibles");
                    }
                    mensaje.setIdMensaje(NO_ENCONTRADO);
                        AdministradorDeOperacionesDatagrama.getInstance(puerto)
                                    .enviarMensaje(mensaje, mensaje.getIp(), mensaje.getPuerto());
                }
                else
                {
                    if (numeroJugadores < MAX_JUGADORES) 
                    {
                        if(ml != null)
                        {
                            ml.mensaje("Alojado en este nodo");
                        }
                        
                        agregarJugador(mensaje);

                    } else 
                    {
                        if(ml != null)
                        {
                            ml.mensaje("No hay disponibilidad en este nodo");
                        }
                        AdministradorDeOperacionesDatagrama
                                .getInstance(puerto).enviarMensaje(mensaje,
                                nodoSiguiente.getIp(), nodoSiguiente.getPuerto());
                    }
                    
                }
                break;
                
            default:

        }
        
    }
    
    private void agregarJugador(MensajeDatagrama mensaje) 
    throws UnknownHostException, SocketException, IOException {
        if (numeroJugadores % 2 == 0)//esperará j2 para iniciar partida
        {
            Jugador j = new Jugador();
            j.setGanador(false);
            j.setId(++ids);
            j.setIp(mensaje.getIp());
            j.setNickName(mensaje.getNickNameOponente());
            j.setPuerto(mensaje.getPuerto());

            Partida p = new Partida();
            p.setJugador1(j);
            p.setCompleta(false);
            partidas.add(p);

        } 
        else 
        {
            Jugador j = new Jugador();
            j.setGanador(false);
            j.setId(++ids);
            j.setIp(mensaje.getIp());
            j.setNickName(mensaje.getNickNameOponente());
            j.setPuerto(mensaje.getPuerto());
            Partida p = (Partida) partidas.get(partidas.size() - 1);

            p.setJugador2(j);
            p.setCompleta(true);
            partidas.add(partidas.size() - 1, p);

            mensaje.setIdMensaje(INICIAR);
            mensaje.setIdJugador(p.getJugador1().getId());
            mensaje.setIdOponente(j.getId());
            mensaje.setIp(InetAddress.getLocalHost().getHostAddress());
            mensaje.setPuerto(puerto);
            mensaje.setNodoId(mensaje.getIp() + ":" + puerto);
            mensaje.setNickNameOponente(j.getNickName());

            AdministradorDeOperacionesDatagrama
                    .getInstance(puerto).enviarMensaje(mensaje, p.getJugador1().getIp(),
                    p.getJugador1().getPuerto());

            mensaje.setIdJugador(j.getId());
            mensaje.setNickNameOponente(p.getJugador1().getNickName());
            mensaje.setIdOponente(p.getJugador1().getId());

            AdministradorDeOperacionesDatagrama
                    .getInstance(puerto).enviarMensaje(mensaje, j.getIp(),
                    j.getPuerto());

        }
        numeroJugadores++;

    }
    
    
    public void actualizarMulticast(MulticastListener ml, String nodoId) throws IOException
    {
        MensajeMulticast mensaje = AdministradorDeOperacionesMulticast.getInstance().recibe();
        if (mensaje.getNombreOrigen() != null) {
            switch (mensaje.getId()) {
                case INICIO_ID:
                    if (!mensaje.getNombreOrigen().equals(nodoId)) 
                    {
                        if(inicioNodo(mensaje.getNombreOrigen()))
                        {
                            nodoSiguiente = getNodoSiguiente(puerto);
                            if(ml != null)
                            {
                                
                                actualizarListaDeNodos(ml);
                                ml.nodoSiguiente(nodoSiguiente.getId());
                            }
                        }
                    }
                    break;
                case FIN_ID:
                    if(finNodo(mensaje.getNombreOrigen()))
                    {
                        if(ml != null)
                        {        
                            actualizarListaDeNodos(ml);
                        }
                        if(nodos.size() > 0)
                        {
                            nodoSiguiente = getNodoSiguiente(puerto);
                            if(ml != null)
                            {
                                ml.nodoSiguiente(nodoSiguiente.getId());
                            }
                        }
                        else
                        {
                            if(ml != null)
                            {
                                ml.nodoSiguiente(nodoSiguiente.getId());
                            }
                        }
                    }
                    break;
                
                default:

            }
        }
    }
    
    
    
    
    
    private boolean esElMayor(int idNodo)
    {
        int tam = nodos.size();
        Nodo n;
        for(short i = 0; i < tam ; i++)
        {
            n = (Nodo) nodos.get(i);
            if(n.getPuerto() > idNodo)
                   return false;
        }
        return true;
        
    }
    
    
    
    private Nodo getNodoMenor()
    {
        int tam = nodos.size();
        Nodo n = (Nodo) nodos.get(0);
        int menor = n.getPuerto();
        int j = 0;
        for (short i = 1; i < tam; i++) 
        {
            n = (Nodo) nodos.get(i);
            if (n.getPuerto() < menor) 
            {
                menor = n.getPuerto();
                j = i;
            }
        }
        return (Nodo) nodos.get(j);
    }
    
    
    
    
    private Nodo getNodoSiguiente(int idNodoPuerto)
    {
        Nodo n = null;
        if(esElMayor(idNodoPuerto))
        {
            n = getNodoMenor();
        }
        else
        {
            int tam = nodos.size();
            int resta = 2147483647;
            int aux, j = 0;
            for (short i = 0; i < tam; i++) 
            {
                n = (Nodo) nodos.get(i);
                aux = n.getPuerto() - idNodoPuerto;
                if (aux < resta && aux > 0) 
                {
                    resta = aux;
                    j = i;
                }
            }
            n = (Nodo) nodos.get(j);
        }
        return n;
        
    }
   
    
    
    private void actualizarListaDeNodos(MulticastListener ml)
    {
        String nodosDisponibles = "";
        int tam = nodos.size();
        Nodo n;
        for(short i = 0; i < tam ; i++)
        {
            n = (Nodo) nodos.get(i);
            nodosDisponibles += ("<br>" + n.getId());  
        }
        ml.log(nodosDisponibles);
    }
    
    private boolean finNodo(String id)
    {
        int tam = nodos.size();
        Nodo n;
        for(short i = 0; i < tam ; i++)
        {
            n = (Nodo) nodos.get(i);
            if(n.getId().equals(id))
            {
                nodos.remove(i);
                return true;
            }
        }
        return false;
        
    }
    
    private boolean inicioNodo(String id)
    {
        int tam = nodos.size();
        Nodo n;
        for(short i = 0; i < tam ; i++)
        {
            n = (Nodo) nodos.get(i);
            if(n.getId().equals(id))
                return false;
        }
        nodos.add(new Nodo(id, getIP(id), getPuerto(id)));
        return true;
    }
    
    
   
    
    private int getPuerto(String idNodo)
    {
        String puerto = "";
        char c;
        int i = 0;
        while((c = idNodo.charAt(i)) != ':')
        {
            i++;
        }
        puerto = idNodo.substring(i + 1);
        
        return Integer.parseInt(puerto);
    }
    
    private String getIP(String idNodo)
    {
        String ip = "";
        char c;
        int i = 0;
        while((c = idNodo.charAt(i)) != ':')
        {
            ip+=c;
            i++;
        }
        return ip;
    }
    
    
    
    
    public void nuevaConexion() throws IOException
    {
        while(true)
        {
            final Socket cl = s.accept();
            Thread hilo = new Thread()
                {
                    public void run() 
                    {
                        boolean permanecer = true;
                        try 
                        {
                            
                            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
                            ObjectInputStream ois  = new ObjectInputStream(cl.getInputStream());
                            
                            while(permanecer)
                            {
                                permanecer = actualizar(ois, oos);
                            }
                       
                        } catch (IOException ex) { } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Mediador.class.getName()).log(Level.SEVERE, null, ex);
                        }
        
                    }
                };
            hilo.start();
        }
        
    }
    
    private boolean actualizar(ObjectInputStream ois, ObjectOutputStream oos) 
            throws IOException, ClassNotFoundException
    {
        MensajeFlujo mensaje = (MensajeFlujo) ois.readObject();
        boolean continuar = true;
        Partida p;
        Jugador j;
        int tam;
        switch(mensaje.getIdMensaje())
        {
            case INICIAR_JUEGO:
                int id = mensaje.getOrigen();
                tam= partidas.size();
                for(int i = 0; i < tam; i++)
                {
                    p = (Partida) partidas.get(i);
                    j = p.getJugador1();
                    if( j.getId() == id)
                    {
                        j.setOis(ois);
                        j.setOos(oos);
                        j.setConectado(true);
                        p.setJugador1(j);
                        if(p.getJugador2().isConectado())
                        {
                            iniciarPartida(p);
                        }
                        break;
                    }
                    j = p.getJugador2();
                    if( j.getId() == id)
                    {
                        j.setOis(ois);
                        j.setOos(oos);
                        j.setConectado(true);
                        if(p.getJugador1().isConectado())
                        {
                            iniciarPartida(p);
                        }
                    }
                }
                
                break;
            case MOVIMIENTO:
                System.out.print("Procesando movimiento\n");
                int destino = mensaje.getDestino();
                tam = partidas.size();
                for(int i = 0; i < tam; i++)
                {
                    p = (Partida) partidas.get(i);
                    j = p.getJugador1();
                    if(j.getId() == destino)
                    {
                        enviarMensaje(j.getOos(), mensaje);
                        break;
                    }
                    j = p.getJugador2();
                    if(j.getId() == destino)
                    {
                        enviarMensaje(j.getOos(), mensaje);
                        break;
                    }
                }
                
                break;
            case FIN_JUEGO:
                break;
                
            case SALIR:
                oos.close();
                ois.close();
                tam = partidas.size();
                for(int i = 0; i < tam; i++)
                {
                    p = (Partida) partidas.get(i);
                    j = p.getJugador1();
                    if(j.getId() == mensaje.getDestino())
                    {
                        enviarMensaje(j.getOos(), mensaje);
                        j.getOos().close();
                        j.getOis().close();
                        partidas.remove(i);
                        break;
                    }
                    j = p.getJugador2();
                    if(j.getId() == mensaje.getDestino())
                    {
                        enviarMensaje(j.getOos(), mensaje);
                        j.getOos().close();
                        j.getOis().close();
                        partidas.remove(i);
                        break;
                    }
                }
                    
                numeroJugadores -= 2;
                continuar = false;
                break;
               
            default:
        }
        
        return continuar;
    }
    
    private void iniciarPartida(Partida partida) throws IOException
    {
        MensajeFlujo mensaje = new MensajeFlujo();
        mensaje.setIdMensaje(INICIAR_JUEGO);
        mensaje.setTurno(true);
        int iniciar = (int)((Math.random() * 10)) % 2;
        ObjectOutputStream oos;
        if(iniciar == 0)//inicia Jugador1
        {
            oos = partida.getJugador1().getOos();
            oos.writeObject(mensaje);
            oos.flush();
            mensaje.setTurno(false);
            oos = partida.getJugador2().getOos();
            oos.writeObject(mensaje);
            oos.flush();
        }
        else
        {
            oos = partida.getJugador2().getOos();
            oos.writeObject(mensaje);
            oos.flush();
            mensaje.setTurno(false);
            oos = partida.getJugador1().getOos();
            oos.writeObject(mensaje);
            oos.flush();
            
        }
    }
    
    private void enviarMensaje(ObjectOutputStream oos, MensajeFlujo mensaje) 
            throws IOException
    {
        oos.writeObject(mensaje);
        oos.flush();
    }
    
    
}
