
package Logica.Busqueda;


import java.util.ArrayList;

/**
 *
 * @author miguel
 */
public class Archivo 
{

    public static char getSeparadorFinal() {
        return separadorFinal;
    }

    public ArrayList<Nodo> getNodos() {
        return nodos;
    }

    public void setNodos(ArrayList<Nodo> nodos) {
        this.nodos = nodos;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
    
    private static final char separador = '#';
    private static final char separadorFinal = '&';
    
    private ArrayList<Nodo> nodos = null;
    private String md5 = null;

    public Archivo( String md5, ArrayList<Nodo> nodos) {
        this.nodos = nodos;
        this.md5 = md5;
    }

    public Archivo() {
        nodos = new ArrayList();
        
    }
    
    
    public String getCadena()
    {
        String cadena = "";
        if(md5 != null)
            cadena += md5;
        cadena += separador;
        if(nodos != null)
        {
            for(int i = 0; i < nodos.size(); i++)
            {
                cadena += nodos.get(i).getCadena();
            }
        }
        
        cadena += getSeparadorFinal();
        return cadena;
    }
    
    public static Archivo getArchivo(String data)
    {
        if(data != null)
        {
            Archivo archivo = new Archivo();
            ArrayList<Nodo> nodos= new ArrayList();
            int i = 0;
            char c;
            String md5 = "";
            String nodo = "";
            while((c = data.charAt(i)) != separador)
            {
                i++;
                md5 += c;
            }
            archivo.setMd5(md5);
            i++;
            while((c = data.charAt(i)) != separadorFinal)
            {
               i++;
               nodo += c; 
               if(c == Nodo.getSeparadorFinal())
               {
                   Nodo n = Nodo.getNodo(nodo);
                   if(n != null)
                   {
                        nodos.add(n);
                   }
                   nodo = "";
               }
            }
            
            if(nodos.size() > 0)
                archivo.setNodos(nodos);
            
            return archivo;
        }
        return null;
    }
    
    public void imprimir()
    {
        
        System.out.print("Archivo\n");
        System.out.print("md5: "+ md5 + "\n");
        if(nodos.size() > 0)
        {
            for(int i = 0; i < nodos.size(); i++)
            {
                nodos.get(i).imprimir();
            }
        }
    }
}


