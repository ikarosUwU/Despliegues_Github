/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package observer;

import modelo.Transaccion;
import modelo.TransactionType;

/**
 *
 * @author USUARIO
 */
public class observadorContable implements TransactionObserver{

    @Override
    public void update(Transaccion transaccion) {
        String transactionType = transaccion.getTipo() == TransactionType.VENTA ? "VENTA" : "GASTO";
        System.out.println("[CONTABILIDAD] Registrando " + transactionType + ": $" + String.format("%.2f", transaccion.getCantidad()));
    }
    
}
