
package Logica;

import java.io.Serializable;

/**
 *
 * @author miguel angel amador nava
 */
public class Mensaje implements Serializable
{

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }

    private final char separador = '$';
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
        this.mensaje = mensaje;
        this.nombreDestino = nombreDestino;
    }
    
    public void imprimir()
    {
        System.out.print("Id mensaje: " + id + "\n");
        System.out.print("Origen: " + nombreOrigen + "\n");
        System.out.print("Destino: " + nombreDestino + "\n");
        System.out.print("Mensaje: " + mensaje + "\n");
    }

    public Mensaje() 
    {
        this.id = 0;
        this.nombreOrigen = "";
        this.mensaje = "";
        this.nombreDestino = "";
    }
    
    public String getCadenaMensaje()
    {
        return id + "" +separador + nombreOrigen + separador + nombreDestino + separador 
               +mensaje + separador;
    }
}
