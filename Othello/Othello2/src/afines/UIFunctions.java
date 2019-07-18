package afines;

import java.awt.Color;
import static java.awt.Frame.ICONIFIED;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
/**
 *
 * @author miguel angel amador nava
 */

public class UIFunctions {
    
    public static void closeWindow(){
        System.exit(0);
    }
    
    public static void minimizeWindow(JFrame frame){
        frame.setExtendedState(ICONIFIED);
    }    

    public static void informationMessage(String message, String window)
    {
        JOptionPane.showMessageDialog(null, message, window, JOptionPane.INFORMATION_MESSAGE);
    }
    
   public static void warningMessage(String message, String window){
          JOptionPane.showMessageDialog(null,message,window, JOptionPane.WARNING_MESSAGE);
    }
   
   
   public static String formatoDescripcion(String descripcion, int tope)
    {
        int aux = 0;
        String cadena = "<html><body>";
        
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
                    cadena += descripcion.substring(0, aux) + "<br>";
                    descripcion = descripcion.substring(aux, descripcion.length() );
                    i = 0;
                }
            }
        }
        cadena += descripcion;
        cadena += "</body></html>"; 
        return cadena;
    }
   
   public static void cambiarImagenBoton(JButton boton, String imagen, int ancho, int alto)
    {
        ImageIcon img = new ImageIcon(imagen);
        boton.setIcon(new ImageIcon(img.getImage().getScaledInstance(ancho,
                        alto, java.awt.Image.SCALE_DEFAULT)));
        
    }
   
   public static boolean isInteger(String data)
   {
       try
       {
           Integer.parseInt(data);
           return true;
       }
       catch(NumberFormatException e)
       {
           return false;
       }
   }
    
}
