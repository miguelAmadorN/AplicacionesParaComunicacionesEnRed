
package Logica.Busqueda;

/**
 *
 * @author miguel
 */
public class Nodo {

    public static char getSeparadorFinal() {
        return separadorFinal;
    }

    private static final char separadorFinal = '$';
    private static final char separador = '"';
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    private String id;
    private String ip;
    private int puerto;
    public short temporalizador;

    public Nodo(String id, String ip, int puerto) {
        this.id = id;
        this.ip = ip;
        this.puerto = puerto;
    }

    public Nodo() 
    {
    }
    
    
    public String getCadena()
    {
        return id + separador + ip + separador + puerto + getSeparadorFinal();
    }
    
    public static Nodo getNodo(String data)
    {
        Nodo nodo = new Nodo();
        char c;
        String id = "";
        String ip = "";
        String p ="";
        int puerto;
        int i = 0;
        while((c = data.charAt(i)) != separador)
        {
            i++;
            id += c;
        }
        nodo.setId(id);
        i++;
        while((c = data.charAt(i)) != separador)
        {
            i++;
            ip += c;
        }
        nodo.setIp(ip);
        i++;
        
        while((c = data.charAt(i)) != separadorFinal)
        {
            i++;
            p += c;
        }
        puerto = Integer.parseInt(p);
        nodo.setPuerto(puerto);
        
        return nodo;
    }
    
    public void imprimir()
    {
        System.out.print("Nodo\n");
        System.out.print("Id: " + id + "\n");
        System.out.print("Ip: " + ip  + "\n");
        System.out.print("Puerto: " + puerto  + "\n");
    }
    
}
