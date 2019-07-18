
package afines;

import java.io.Serializable;

/**
 *
 * @author miguel
 */
public class MensajeFlujo implements Serializable
{

    public int getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public int getOrigen() {
        return origen;
    }

    public void setOrigen(int origen) {
        this.origen = origen;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public boolean isTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

   
    private int idMensaje;
    private int origen;
    private int destino;
    private Coordenada coordenada;
    private boolean turno;
    

    public MensajeFlujo() {
    }

    public MensajeFlujo(int idMensaje, int origen, int destino, Coordenada coordenada, 
            boolean turno) 
    {
        this.idMensaje = idMensaje;
        this.origen = origen;
        this.destino = destino;
        this.coordenada = coordenada;
        this.turno = turno;
    }

    
    
    
}
