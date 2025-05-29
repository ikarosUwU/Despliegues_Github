/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Strategy;

/**
 *
 * @author USUARIO
 */
public class CashPayment implements PaymentMethod{
    
    private double cashReceived;

    public CashPayment(double cashReceived) {
        this.cashReceived = cashReceived;
    }

    @Override
    public boolean procesoPago(double amount) {
        if (cashReceived >= amount) {
            double change = cashReceived - amount;
            System.out.println("Pago en efectivo procesado. Cambio: $" + String.format("%.2f", change));
            return true;
        } else {
            System.out.println("Efectivo insuficiente");
            return false;
        }
    }

    @Override
    public String getDetallesdePago() {
        return "Pago en efectivo - Recibido: $" + String.format("%.2f", cashReceived);
    }
    
}
