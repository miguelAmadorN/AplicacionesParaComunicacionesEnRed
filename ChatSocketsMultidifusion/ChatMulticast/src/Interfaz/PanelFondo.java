
package Interfaz;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class PanelFondo extends javax.swing.JPanel{
    private Image imagen;
    
    public PanelFondo(String nombreImagen)
    {
        if (nombreImagen != null) {
            imagen = new ImageIcon(
                   getClass().getResource(nombreImagen)
                   ).getImage();
        } else {
            imagen = null;
        }
 
        repaint();
    }
    
    @Override
    public void paint(Graphics g)
    {
        g.drawImage(imagen,0,0, getWidth(), getHeight(), this);
        setOpaque(false);
        super.paint(g);
    }
    
    
}
