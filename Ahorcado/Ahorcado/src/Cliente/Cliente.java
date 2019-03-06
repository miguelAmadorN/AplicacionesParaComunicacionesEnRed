/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Clases.Partida;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JPanel;
import Interfaz.PanelFondo;
import Interfaz.UIFunctions;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
/**
 *
 * @author miguel
 */
public class Cliente extends javax.swing.JFrame implements ActionListener{

    /**
     * Creates new form Cliente
     */
    private final static int NUMERO_DE_LETRAS  = 26;
    private final static int INICIO_DE_LETRAS  = 65;
    private final String NADA  = "src/Imagenes/nada.png";
    private final String ERROR_1  = "src/Imagenes/ERROR_1.png";
    private final String ERROR_2  = "src/Imagenes/ERROR_2.png";
    private final String ERROR_3  = "src/Imagenes/ERROR_3.png";
    private final String GANADOR  = "src/Imagenes/GANADOR.png";
    private final String PERDEDOR = "src/Imagenes/PERDEDOR.png";
    
    private final int ANCHO_IMAGEN = 150;
    private final int ALTO_IMAGEN  = 200;
    
    
    Hashtable <JButton, String> abecedario = new Hashtable <JButton, String>();
    private final PanelFondo contenedor = new PanelFondo("/Imagenes/fondo.png");
    private ObjectOutputStream oos = null;
    private ObjectInputStream  ois = null;
    private int errores = 0, tamPalabra = 0;
    private JPanel layout = new JPanel(new GridLayout(3,1)), JPpalabra;
    private JLabel JLpalabra, imagen;
    private Partida partida;
    private String PALABRA = "";
    private boolean seguirJugando = true; 
            
    public Cliente() 
    {     
        setContentPane(contenedor);
        init();
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("AHORCADO"); 
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                try {
                    close();
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
            }
        });
        
    }

    private void close() throws IOException{
        if (JOptionPane.showConfirmDialog(rootPane, "¿Deseas salir del juego?",
                "Salir del juego", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            if(oos != null && ois != null)
            {
                AdministradorDeOperaciones.finalizarConexion(oos, ois);
            }
            
            System.exit(0);
        }else
        {
            
        }
    }
    private void agregarBotones()
    {
        char letra;
        JPanel Letras = new JPanel(new GridLayout(3,7));
        Letras.setBackground(new Color(0,0,0,30));
        JButton boton;
        String texto;
        int i = 0;
        for( i = 0; i < NUMERO_DE_LETRAS; i++ )
        {
            if(i == 14)
            {
                letra = 'Ñ';
                boton = new JButton();
                texto = String.valueOf(letra);
                boton.setText(texto);
                boton.addActionListener(this);
                abecedario.put(boton, texto);
                Letras.add(boton); 
            }
            letra = (char) (INICIO_DE_LETRAS + i);
            boton = new JButton();
            texto = String.valueOf(letra);
            boton.setText(texto);
            boton.addActionListener(this);
            abecedario.put(boton, texto);
            Letras.add(boton);  
        }
        layout.add(Letras);
    }
    
    
    private void agregarLineas()
    {
        
        JPpalabra = new JPanel(new GridLayout(1,0));
        JPpalabra.setBackground(new Color(0,0,0,0));
        JLpalabra = new JLabel();
        PALABRA = "";
        for(int i = 0; i < this.tamPalabra; i++)
        {
            PALABRA += "_ ";
        }
        
        JLpalabra.setFont(new Font("Agency FB", Font.BOLD, 25));
        JLpalabra.setHorizontalAlignment(JLabel.CENTER);
        JLpalabra.setText(UIFunctions.formatoDescripcion(PALABRA, 23));
        JPpalabra.add(JLpalabra);
        layout.add(JPpalabra);
    }
    
    
    private String getImagenAhorcado(int errores)
    {
        switch(errores)
        {
            case -1:
                return GANADOR;
            case 1:
                return ERROR_1;
            case 2:
                return ERROR_2;
            case 3:
                return ERROR_3;
            case 4:
                return PERDEDOR;
            default:
                return NADA;
        }
    }
    
    private void agregarImagen()
    {
        JPanel panel = new JPanel(new GridLayout(0,1));
        imagen = new JLabel();
        imagen.setSize(ANCHO_IMAGEN, ALTO_IMAGEN);
        imagen.setHorizontalAlignment(JLabel.CENTER);
        imagen.setVerticalAlignment(JLabel.BOTTOM);
        panel.setBackground(new Color(0,0,0,0));
        ImageIcon ii = new ImageIcon(getImagenAhorcado(0));
        imagen.setIcon(new ImageIcon(ii.getImage().getScaledInstance(ANCHO_IMAGEN,ALTO_IMAGEN, 
                                                                       java.awt.Image.SCALE_DEFAULT)));
        panel.add(imagen);
        layout.add(panel);
    }
    
    private void iniciarConexion()
    {
        Conexion dialogo = null;
        dialogo = new Conexion(new javax.swing.JFrame(), true);
        dialogo.setVisible(true);
        if(dialogo.getTamanioPalabra() == -1)
        {
            if (dialogo.getOos() != null && dialogo.getOis() != null) {
                try {
                    AdministradorDeOperaciones.finalizarConexion(dialogo.getOos() , dialogo.getOis());
                } catch (Exception e) {
                     e.printStackTrace();
                }
            }
            System.exit(0);
        }
        else
        {
            this.oos = dialogo.getOos();
            this.ois = dialogo.getOis();
            this.tamPalabra = dialogo.getTamanioPalabra();
            
        }
    }
    
    private void init()
    {
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass()
                        .getResource("/Imagenes/Icono.png"));
        setIconImage(icon);
       partida = new Partida();
       iniciarConexion();
       layout.setBackground(new Color(0,0,0,0));
       agregarBotones();
       agregarImagen();
       agregarLineas();
       add(layout);
       pack();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void actualizarAhorcado(int errores)
    {
        setContentPane(contenedor);
        ImageIcon ii = new ImageIcon(getImagenAhorcado(errores));
        imagen.setIcon(new ImageIcon(ii.getImage().getScaledInstance(ANCHO_IMAGEN,ALTO_IMAGEN, 
                                                                    java.awt.Image.SCALE_DEFAULT)));
    }
    
    private String reemplazarLetraEnDiagonales(int posiciones[], char letra, String palabra)
    {
        String nuevaPalabra = "";
        if(posiciones.length > 0)
        {
            int indice = 0, tam = posiciones.length - 1;
            for(int i = 0 ; i < palabra.length(); i++)
            {
                if(i == posiciones[indice] * 2)
                {
                    nuevaPalabra += letra;
                    if(indice < tam)
                        indice++;
                }
                else
                    nuevaPalabra += palabra.charAt(i);
            }
        }
        else
        {
            nuevaPalabra = palabra;
        }
        return nuevaPalabra;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        if (seguirJugando) 
        {
            JButton a = (JButton) ae.getSource();
            a.setEnabled(false);
            char letra = abecedario.get(ae.getSource()).charAt(0);
            try {
                partida = AdministradorDeOperaciones.evaluarLetra(oos, ois, letra);
                if (partida.isFinPartida()) 
                {
                    if (partida.isGanador()) 
                    {
                        //Poner palabras en el label
                        setContentPane(contenedor);
                        PALABRA = reemplazarLetraEnDiagonales(partida.getPosiciones(), letra, PALABRA);
                        JLpalabra.setText(UIFunctions.formatoDescripcion(PALABRA, 23));
                        System.out.print("Ganaste!");
                        actualizarAhorcado(-1);
                        seguirJugando = false;
                    } else 
                    {
                        System.out.print("Perdiste!");
                        actualizarAhorcado(partida.getErrores());
                        seguirJugando = false;
                    }
                } else 
                {
                    if (partida.getPosiciones() != null) 
                    {
                        setContentPane(contenedor);
                        PALABRA = reemplazarLetraEnDiagonales(partida.getPosiciones(), letra, PALABRA);
                        JLpalabra.setText(UIFunctions.formatoDescripcion(PALABRA, 23));
                    } else 
                    {
                        
                        actualizarAhorcado(partida.getErrores());
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
