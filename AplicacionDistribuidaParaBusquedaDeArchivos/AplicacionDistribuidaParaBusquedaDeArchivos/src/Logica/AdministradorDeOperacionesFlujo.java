/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import static Logica.OperacionesNodo.RUTA;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author miguel
 */
public class AdministradorDeOperacionesFlujo {
    
    ServerSocket s;
    private static AdministradorDeOperacionesFlujo INSTANCE = null;
    private static final int TAM = 1024;
    
    private AdministradorDeOperacionesFlujo(int puerto) throws IOException 
    {
        s = new ServerSocket(puerto);
    }
    
    public static AdministradorDeOperacionesFlujo getInstance(int puerto) 
            throws IOException 
    {
        if(INSTANCE == null)
        {
            INSTANCE = new AdministradorDeOperacionesFlujo(puerto);
        }
        return INSTANCE;
        
    }
    
    public boolean enviarArchivo(String carpeta, TransferenciaListener tl) 
            throws IOException 
    {
        //Esperamos una conexión 
        Socket cl = s.accept();
        if(tl != null)
        {
            tl.mensaje("Conexión establecida desde" + cl.getInetAddress() + ":"
                        + cl.getPort());  
        }
            

        DataInputStream disa = new DataInputStream(
                cl.getInputStream());

        byte b[] = new byte[TAM];
        String nombre = disa.readUTF();
        if(tl != null)
        {
            tl.mensaje("Transferir " + nombre);
        }
        
        String path = RUTA + "/" + carpeta  + "/" + nombre;
        File f = new File(path);
        DataInputStream dis = new DataInputStream(
                new FileInputStream(path));
        
        DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
        
        long enviados = 0;
        int porcentaje, n;
        long tam = f.length();
        dos.writeLong(tam);
        
        while ((n = dis.read(b)) > 0) 
        {
            
            dos.write(b, 0, n);
            dos.flush();
            
            
            if(tl != null)
            {
                enviados = enviados + n;
                porcentaje = (int) (enviados * 100 / tam);
                tl.porcentaje(porcentaje);
            }
        }
        dos.close();
        dis.close();
        disa.close();
        cl.close();
        if(tl != null)
        {
            tl.mensaje("Archivo transferido");
        }
        return true;
    }
    
    public boolean solicitarArchivo(String ip, int puerto, String nombreArchivo, 
            String carpeta, TransferenciaListener tl) throws IOException
    {
        Socket cl = new Socket(ip, puerto);
        DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
        dos.writeUTF(nombreArchivo);
        DataInputStream dis = new DataInputStream(cl.getInputStream());

        Long tam = dis.readLong();
        String path = RUTA + "/" + carpeta+ "/" + nombreArchivo;
        DataOutputStream dosa = new DataOutputStream(new FileOutputStream(path));

        long recibidos = 0;
        int n, porcentaje;
        byte b[] = new byte[TAM];
        if(tl!= null)
        {
            tl.mensaje("Recibiendo archivo...");
        }
        while ((n = dis.read(b)) > 0) 
        {
            
            dosa.write(b, 0, n);
            dosa.flush();
            if(tl != null)
            {
                
                recibidos = recibidos + n;
                porcentaje = (int) (recibidos * 100 / tam);
                tl.porcentaje(porcentaje);
            }
        }
        
        tl.mensaje("Archivo recibido");
        dos.close();
        dosa.close();
        dis.close();
        cl.close();
        return true;
    }
    
    
}
