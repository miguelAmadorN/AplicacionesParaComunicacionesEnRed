
package Logica.Busqueda;

import java.util.ArrayList;

/**
 *
 * @author miguel
 */

public class Busqueda 
{

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Archivo> getArchivos() {
        return archivos;
    }

    public void setArchivos(ArrayList<Archivo> archivos) {
        this.archivos = archivos;
    }

    public String getNodoOrigen() {
        return nodoOrigen;
    }

    public void setNodoOrigen(String nodoOrigen) {
        this.nodoOrigen = nodoOrigen;
    }

    private static final char separador = '%';
    private static final char separadorFinal = '+';
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

   
    private int id;
    private String nodoOrigen = null;
    private String nombreArchivo = null;
    private ArrayList<Archivo> archivos = null;

    public Busqueda() {
        archivos = new ArrayList();
    }

    public Busqueda(int id,String nodoOrigen, String nombreArchivo, ArrayList<Archivo> archivos) 
    {
        this.id = id;
        this.nodoOrigen = nodoOrigen;
        this.nombreArchivo = nombreArchivo;
        this.archivos = archivos;
    }
    
    
    
    public String getCadena()
    {
        String cadena = "";
        cadena += (id + "" + separador);
        cadena += (nodoOrigen + separador);
        cadena += (nombreArchivo + separador);
        
        if(getArchivos().size() > 0 )
        {
            for(int i = 0; i < archivos.size(); i++)
            {
                cadena +=(archivos.get(i).getCadena());
            }
        }
         cadena += separadorFinal;
        return cadena;
    }
    
    public static Busqueda getBusqueda(String data)
    {
        if(data != null)
        {
            Busqueda busqueda = new Busqueda();
            ArrayList<Archivo> ar = new ArrayList();
            String nodoInicio = "";
            String archivo = "";
            String archivos = "";
            char c;
            int i = 0;
            
            while ((c = data.charAt(i)) != separador) {
                i++;
                nodoInicio += c;
            }
            busqueda.setId(Integer.parseInt(nodoInicio));
            i++;
            nodoInicio = "";
            
            while ((c = data.charAt(i)) != separador) {
                i++;
                nodoInicio += c;
            }
            busqueda.setNodoOrigen(nodoInicio);
            i++;

            while ((c = data.charAt(i)) != separador) {
                i++;
                archivo += c;
            }
            busqueda.setNombreArchivo(archivo);
            i++;

            while ((c = data.charAt(i)) != separadorFinal) 
            {
                i++;
                archivos += c;
                if (c == Archivo.getSeparadorFinal()) 
                {
                    Archivo a = Archivo.getArchivo(archivos);
                    if(a != null)
                        ar.add(a);
                    archivos = "";
                }
            }
            if(ar.size() > 0)
            {
                busqueda.setArchivos(ar);
            }
            
            return busqueda;
        }
        return null;
    }
    
    public void imprimir()
    {
        System.out.print("Busqueda\n");
        System.out.print("ID mensaje:" + id + "\n");
        System.out.print("Nodo Origen:" + nodoOrigen + "\n");
        System.out.print("nombreArchivo:" + nombreArchivo + "\n");
        if(archivos.size() > 0)
        {
            for(int i = 0; i < archivos.size(); i++)
            {
                archivos.get(i).imprimir();
            }
        }
    }
}
