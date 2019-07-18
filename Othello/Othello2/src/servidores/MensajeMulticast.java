
package servidores;

import java.io.Serializable;

/**
 *
 * @author miguel angel amador nava
 */
public class MensajeMulticast implements Serializable
{
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

    
    private int id;
    private String nombreOrigen;
    public MensajeMulticast(int id, String nombreOrigen) 
    {
        this.id = id;
        this.nombreOrigen = nombreOrigen;
    }
    
    public void imprimir()
    {
        System.out.print("Id mensaje: " + id + "\n");
        System.out.print("Origen: " + nombreOrigen + "\n");
    }

    public MensajeMulticast() 
    {
        this.id = 0;
        this.nombreOrigen = "";
    }
    
    
}
