
package Logica.Transferencia;

/**
 *
 * @author miguel
 */

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Transferencia extends Remote {
    
   public void inicializarDescarga(String name, int totalNodos, int nodo)throws RemoteException;
   public long getTamArchivo()throws RemoteException;
   public byte[] transferirArchivo(int buff)  throws RemoteException, FileNotFoundException ;
}