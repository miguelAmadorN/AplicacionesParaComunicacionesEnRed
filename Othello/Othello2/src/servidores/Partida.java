
package servidores;

/**
 *
 * @author miguel
 */
public class Partida 
{

    public boolean isCompleta() {
        return completa;
    }

    public void setCompleta(boolean completa) {
        this.completa = completa;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }
    private Jugador jugador1;
    private Jugador jugador2;
    private boolean completa;

    public Partida() {
    }

    public Partida(Jugador jugador1, Jugador jugador2, boolean completa) 
    {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.completa = completa;
    }

    
    
    
}
