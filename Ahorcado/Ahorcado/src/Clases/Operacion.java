/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;

/**
 *
 * @author miguel
 */
public class Operacion implements Serializable {

    public int getOperacion() {
        return operacion;
    }

    public void setOperacion(int operacion) {
        this.operacion = operacion;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }
        protected int operacion;
        protected String parametros;

    public Operacion(int operacion, String parametros) {
        this.operacion = operacion;
        this.parametros = parametros;
    }

    public Operacion() {
    }
        
    
}
