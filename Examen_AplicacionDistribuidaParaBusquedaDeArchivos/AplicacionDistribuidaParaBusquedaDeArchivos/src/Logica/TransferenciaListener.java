
package Logica;

/**
 *
 * @author miguel
 */
public interface TransferenciaListener {
    
    
    public void mensaje(String message);
    public void bytesEnviados(int bytes);
    public void bytesRecibidos(int bytes);
    public void cantBytesEnviar(long bytes);
    public void cantBytesRecibir(long bytes);
    
}
