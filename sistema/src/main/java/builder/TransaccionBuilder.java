/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package builder;

import Strategy.PaymentMethod;
import modelo.Transaccion;
import modelo.TransactionType;

/**
 *
 * @author USUARIO
 */
public class TransaccionBuilder {private double amount;
    private String description;
    private TransactionType type;
    private PaymentMethod paymentMethod;

    public TransaccionBuilder() {
        // Valores por defecto
        this.description = "Sin descripción";
        this.type = TransactionType.VENTA;
    }

    public TransaccionBuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public TransaccionBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TransaccionBuilder withType(TransactionType type) {
        this.type = type;
        return this;
    }

    public TransaccionBuilder withPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public Transaccion build() {
        if (paymentMethod == null) {
            throw new IllegalStateException("El método de pago no puede ser nulo");
        }

        return new Transaccion(amount, description, type, paymentMethod);
    }
    
}
