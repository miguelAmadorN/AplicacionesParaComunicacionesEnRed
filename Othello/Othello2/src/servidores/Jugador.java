
package servidores;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author miguel
 */
public class Jugador 
{

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isGanador() {
        return ganador;
    }

    public void setGanador(boolean ganador) {
        this.ganador = ganador;
    }
    private String ip;
    private int puerto;
    private int id;
    private String nickName;
    private boolean ganador;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private boolean conectado;

    public Jugador() {
        conectado = false;
    }

    public Jugador(String ip, int puerto, int id, String nickName, boolean ganador,
            boolean conectado) 
    {
        this.ip = ip;
        this.puerto = puerto;
        this.id = id;
        this.nickName = nickName;
        this.ganador = ganador;
        this.conectado = conectado;
        
    }
    
    
}
