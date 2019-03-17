
package serviciodechat;

/**
 *
 * @author miguel angel amador nava
 */
public class AnalisisDeMensajes 
{
    private final String ALEGRE = " <img src=" +this.getClass().getClassLoader()
                                    .getResource("Emojis/alegre.png").toString() 
                                    + " width='50' height='50' alt='emoji'> ";
    
    private final String LENGUA = " <img src=" +this.getClass().getClassLoader()
                                    .getResource("Emojis/lengua.png").toString() 
                                    + " width='50' height='50' alt='emoji'> ";
    
    private final  String RISA = " <img src=" +this.getClass().getClassLoader()
                                    .getResource("Emojis/risa.png").toString() 
                                    + " width='50' height='50' alt='emoji'> ";
    
    private final String TRISTE = " <img src=" +this.getClass().getClassLoader()
                                    .getResource("Emojis/triste.png").toString() 
                                    + " width='50' height='50' alt='emoji'> ";
    
    
    public String formatoAMensaje(String mensaje)
    {
        int tam = mensaje.length();
        char c;
        int i = 0;
        String msj = "";
        while(i < tam)
        {
            c = mensaje.charAt(i);
            if(c == ' ')
            {   
                c = mensaje.charAt(i + 1); 
                if( c == 'x')
                {   //risa
                    if(mensaje.charAt(i + 2) == 'd' && mensaje.charAt(i + 3) == ' ')
                    {
                        msj += RISA;
                        i += 3;
                        System.out.print("imgr");
                    }
                    else
                        msj += ' ';
                }
                else if(c == ':')
                {
                    
                    if(mensaje.charAt(i + 3) == ' ')
                    {
                        c = mensaje.charAt(i + 2);
                        if(c == 'p')
                        {
                            msj += LENGUA;
                            i += 3;
                            System.out.print("imgl");
                        }
                        else if(c == '(')
                        {
                           msj += TRISTE;
                            i += 3;
                            System.out.print("imgt");
                        }
                        else if(c == ')')
                        {
                           msj += ALEGRE;
                            i += 3;
                            System.out.print("imga");
                        }
                        else
                            msj += ' ';
                    }
                    else
                        msj += ' ';
                }
                else
                    msj += ' ';
            }
            else
                msj += c;
             
            i++;
        }
        return "<html> " + msj + "</html>";
    }
    
    
    //funcion alterada para solucion rápida 
    public String formatoDescripcion(String descripcion, int tope)
    {
        int aux = 0;
        String msj = "<html><body>";
        char c;
        String msjF = "";
        for(int i = 0; i < descripcion.length(); i++)
        {   
            
            if(descripcion.charAt(i) == ' ' || i == tope)
            {
                if(i < tope)
                {
                    aux = i;
                }
                else
                {
                    if(i == tope && aux == 0)
                        aux = tope;
                    msj += descripcion.substring(0, aux) + " <br> ";
                    
                    descripcion = descripcion.substring(aux, descripcion.length() );
                    i = 0;
                }
            } 
        }
        msj += descripcion;
        msj += "</body></html>"; 
        //return formatoAMensaje(msj);
        return msj;
    }
   
}
