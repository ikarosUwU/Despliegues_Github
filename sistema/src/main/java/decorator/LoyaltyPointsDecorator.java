/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package decorator;

import Strategy.PaymentMethod;

/**
 *
 * @author USUARIO
 */
public class LoyaltyPointsDecorator extends TransaccionDecorator{
    
    private String clienteId;
    private int puntosPorDolar;

    public LoyaltyPointsDecorator(PaymentMethod wrapped, String clienteId, int puntosPorDolar) {
        super(wrapped);
        this.clienteId = clienteId;
        this.puntosPorDolar = puntosPorDolar;
    }

    @Override
    public boolean procesoPago(double cantidad) {
        boolean result = wrapped.procesoPago(cantidad);
        if (result) {
            int puntosGanados = (int)(cantidad * puntosPorDolar);
            System.out.println("Cliente " + clienteId + " ha ganado " + puntosGanados + " puntos de fidelidad");
        }
        return result;
    }

    @Override
    public String getDetallesdePago() {
        return wrapped.getDetallesdePago()+ " + Programa de Fidelidad (ID: " + clienteId + ")";
    }
}
