
package serviciodechat;

/**
 *
 * @author miguel angel amador nava
 */
public class Mensaje 
{

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreOrigen() {
        return nombreOrigen;
    }

    public void setNombreOrigen(String nombreOrigen) {
        this.nombreOrigen = nombreOrigen;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    private int id;
    private String nombreOrigen;
    private String nombreDestino;
    private String mensaje;

    public Mensaje(int id, String nombreOrigen, String nombreDestino, String mensaje) 
    {
        this.id = id;
        this.nombreOrigen = nombreOrigen;
        this.nombreDestino = nombreDestino;
        this.mensaje = mensaje;
    }

    public Mensaje() {}
}
