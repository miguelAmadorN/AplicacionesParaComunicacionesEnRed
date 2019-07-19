
package Logica.Transferencia;

/**
 *
 * @author miguel
 */
import Logica.TransferenciaListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecibirArchivo extends Thread
{

     private byte array[];   
    private String ip;
    private int puerto;
    private int totalNodos;
    private int nodo;
    private String nombreArchivo;
    private static final int TAM = 10000;
    private String ruta = "Carpetas/";
    private TransferenciaListener listener;
    
    public RecibirArchivo(String ip, int puerto, int totalNodos, int nodo, String nombreArchivo,
            int carpeta, TransferenciaListener listener) 
    throws RemoteException, NotBoundException, FileNotFoundException, IOException
    {
        this.ip = ip;
        this.puerto = puerto + 100;//
        this.totalNodos = totalNodos;
        this.nodo = nodo;
        this.nombreArchivo = nombreArchivo;
        ruta += (carpeta +"/");
        this.listener = listener;
        
    }
    
    public static void unirArchivos(int totalNodos,  String nombreArchivo, String ruta) {
        try {
            
                // Se abre el fichero donde se hará la copia
            FileOutputStream fileOutput = new FileOutputStream(ruta + nombreArchivo);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
            byte[] array = new byte[TAM];
            for (int i = 0; i < totalNodos; i++) 
            {
                // Se abre el fichero original para lectura
                String nombre = ruta + "(" + i + ")" + nombreArchivo;
                FileInputStream fileInput = new FileInputStream(nombre);
                BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);


                // Bucle para leer de un fichero y escribir en el otro.
                
                int leidos = bufferedInput.read(array);
                while (leidos > 0) 
                {
                    bufferedOutput.write(array, 0, leidos);
                    leidos = bufferedInput.read(array);
                }

                // Cierre de los ficheros
                bufferedInput.close();
                File f = new File(nombre);
                f.delete();
            }
            bufferedOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() 
    {
        String nombreCopia = ruta + "(" + nodo + ")" + nombreArchivo;
        try {
            
           // final Registry registry = LocateRegistry.getRegistry(ip, puerto);//ip,puerto
            final Transferencia look_up = (Transferencia) Naming.lookup("//"+ip+":"+puerto+"/transferir");
            look_up.inicializarDescarga(nombreArchivo, totalNodos, nodo);
            long tam = look_up.getTamArchivo();
            if(listener != null)
            {
                listener.cantBytesRecibir(tam);
            }
            
            long cant = tam / totalNodos;
            if (totalNodos - 1 == nodo) {
                cant += tam % totalNodos;
            }
            FileOutputStream fileOutput = new FileOutputStream(nombreCopia);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
            int buff = 1024;
            while (cant > 0) 
            {
                if (cant < buff) 
                {
                    buff = (int) cant;
                }
                array = look_up.transferirArchivo(buff);
                bufferedOutput.write(array, 0, buff);
                if(listener != null)
                {
                    System.out.print("Hilo: " + nodo +"\n");
                    listener.bytesRecibidos(buff);
                }

                cant -= buff;
            }
            bufferedOutput.close();
            if(listener != null)
            {
                listener.mensaje("Finalizada recepcion de " + ip + ":"+ puerto);
            }
            
        } catch (RemoteException ex) {
            Logger.getLogger(RecibirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(RecibirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RecibirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RecibirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}