/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Strategy;

/**
 *
 * @author USUARIO
 */
public interface PaymentMethod {
    boolean procesoPago(double amount);
    String getDetallesdePago();
}
