
package Logica;

import java.io.File;

/**
 *
 * @author miguel
 */
public class OperacionesNodo 
{
    public static final String RUTA = "Carpetas/";
    
    public static boolean buscarArchivo(String nombre, int puerto)
    {
        String ruta = RUTA + puerto + "/" + nombre;
        File f = new File(ruta);
        return f.exists();
    }
}
