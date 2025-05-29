/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package observer;

import modelo.Transaccion;

/**
 *
 * @author USUARIO
 */
public interface TransactionObserver {
    void update(Transaccion transaccion);
    
}
