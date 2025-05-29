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
public class DiscountDecorator extends TransaccionDecorator{

    private double porcentajeDescuento;

    public DiscountDecorator(PaymentMethod wrapped, double discountPercentage) {
        super(wrapped);
        this.porcentajeDescuento = discountPercentage;
    }
    
    @Override
    public boolean procesoPago(double cantidad) {
        double cantidadDescuento = cantidad * (porcentajeDescuento / 100);
        double cantidadFinal = cantidad - cantidadDescuento;
        System.out.println("Aplicando descuento de " + porcentajeDescuento + "% ($" + String.format("%.2f", cantidadDescuento) + ")");
        return wrapped.procesoPago(cantidadFinal);
    }

    @Override
    public String getDetallesdePago() {
        return wrapped.getDetallesdePago()+ " con descuento del " + porcentajeDescuento + "%";
    }
    
}
