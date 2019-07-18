
package afines;

/**
 *
 * @author miguel
 */
public class MensajeDatagrama 
{

    public int getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public String getNickNameOponente() {
        return nickNameOponente;
    }

    public void setNickNameOponente(String nickNameOponente) {
        this.nickNameOponente = nickNameOponente;
    }

    public int getIdOponente() {
        return idOponente;
    }

    public void setIdOponente(int idOponente) {
        this.idOponente = idOponente;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNodoId() {
        return nodoId;
    }

    public void setNodoId(String nodoId) {
        this.nodoId = nodoId;
    }

    
    private final char SEPARADOR = '$';
    
    private int idMensaje;
    private int idJugador;
    private String nickNameOponente;//20
    private int idOponente;
    private int puerto;     //Jugador ->//Servidor
    private String ip;//3    //Jugador ->//Servidor
    private String nodoId;

    public MensajeDatagrama() {
    }

    public MensajeDatagrama(int idMensaje, int idJugador, String nickNameOponente,
                            int idOponente, int puerto, String ip, String nodoId) {
        this.idMensaje = idMensaje;
        this.idJugador = idJugador;
        this.nickNameOponente = nickNameOponente;
        this.idOponente = idOponente;
        this.puerto = puerto;
        this.ip = ip;
        this.nodoId = nodoId;
    }
    
    public String getCadenaMensaje()
    {
        return getIdMensaje() + "" + SEPARADOR + "" + getIdJugador() + "" + SEPARADOR +
                getNickNameOponente() + SEPARADOR + "" + getIdOponente()  + ""  + SEPARADOR + 
                "" + getPuerto() + "" + SEPARADOR + getIp() + SEPARADOR + getNodoId() + SEPARADOR;
    }
    
    public void imprimir()
    {
        System.out.print("ID mensaje: " +getIdMensaje() + "\n");
        System.out.print("idJugador: " +getIdJugador()+ "\n");
        System.out.print("nickNameOponente: " + getNickNameOponente()+"\n");
        System.out.print("idOpenente: " +getIdOponente()+ "\n");
        System.out.print("puerto: " + getPuerto()+"\n");
        System.out.print("ip: " + getIp() + "\n");
        if(getNodoId() != null)
            System.out.print("nodoId: " + getNodoId() + "\n");
    }
}
