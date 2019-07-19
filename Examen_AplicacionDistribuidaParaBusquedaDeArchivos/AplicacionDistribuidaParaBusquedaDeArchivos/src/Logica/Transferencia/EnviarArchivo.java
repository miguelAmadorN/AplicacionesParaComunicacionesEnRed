/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica.Transferencia;

import Logica.TransferenciaListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class EnviarArchivo implements Transferencia
{
    private File f;
    private FileInputStream fis;
    private byte[] array;
    private long tam;
   // private Remote  stub;
   // private Registry registry;
    private String ruta = "Carpetas/";
    private TransferenciaListener listener;
    
    public EnviarArchivo(int puertoRuta, TransferenciaListener listener) throws RemoteException, AlreadyBoundException 
    {   
        final Remote stub = UnicastRemoteObject.exportObject(this, 0);
        final Registry registry = LocateRegistry.createRegistry(puertoRuta + 100);
        
        registry.bind("transferir", stub);
        ruta += (puertoRuta + "/");
        this.listener = listener;
       // this.stub = stub;
      //  this.registry = registry;
    }

    @Override
    public void inicializarDescarga(String name, int totalNodos, int nodo) 
    {
        final File f = new File(ruta + name);
        this.f = f;
        long tamanio = f.length();
        long parte = (tamanio / totalNodos);
        long res =  (tamanio % totalNodos);
        long inicio = nodo * parte;
        tam = parte;
        if(nodo == totalNodos - 1)
            tam += res;
        if(listener != null)
        {
            listener.cantBytesEnviar(tam);
        }
        try {
            final FileInputStream fis = new FileInputStream(f);
            fis.skip(inicio);
            this.fis = fis;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EnviarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EnviarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public byte[] transferirArchivo(int buff) throws RemoteException
    {
        try 
        {
            array = new byte[buff];
            fis.read(array,0,buff);
            if(listener != null)
            {
                listener.bytesEnviados(buff);
                System.out.print(buff+"\n");
            }
            else
            {
                System.out.print("listener null\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(EnviarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return array;
    }

    

    @Override
    public long getTamArchivo() throws RemoteException 
    {
        return f.length();
    }
}
