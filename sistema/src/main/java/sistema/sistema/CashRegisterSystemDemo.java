/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema.sistema;

import Strategy.CashPayment;
import Strategy.CreditCardPayment;
import Strategy.MobilePayment;
import Strategy.PaymentMethod;
import builder.TransaccionBuilder;
import decorator.DiscountDecorator;
import decorator.LoyaltyPointsDecorator;
import modelo.Transaccion;
import modelo.TransactionType;

/**
 *
 * @author USUARIO
 */
public class CashRegisterSystemDemo {
    
    public static void main(String[] args) {
        // Creamos nuestro sistema de caja
        CashRegisterSystem cashRegister = new CashRegisterSystem("Mi Tienda");

        // Ejemplo 1: Venta con pago en efectivo
        PaymentMethod cashPayment = new CashPayment(100.0);
        Transaccion venta = new TransaccionBuilder()
                .withAmount(75.50)
                .withDescription("Compra de groceries")
                .withType(TransactionType.VENTA)
                .withPaymentMethod(cashPayment)
                .build();

        cashRegister.ProcesarVenta(venta);

        // Ejemplo 2: Venta con tarjeta de crédito + puntos de fidelidad
        PaymentMethod creditCard = new CreditCardPayment("1234567890123456", "Juan Pérez");
        PaymentMethod creditCardWithLoyalty = new LoyaltyPointsDecorator(creditCard, "CUST-001", 10);

        Transaccion venta2 = new TransaccionBuilder()
                .withAmount(199.99)
                .withDescription("Televisor LED 32\"")
                .withType(TransactionType.VENTA)
                .withPaymentMethod(creditCardWithLoyalty)
                .build();

        cashRegister.ProcesarVenta(venta2);

        // Ejemplo 3: Venta con pago móvil y descuento
        PaymentMethod mobilePayment = new MobilePayment("555-123456", "PayApp");
        PaymentMethod mobileWithDiscount = new DiscountDecorator(mobilePayment, 15.0);

        Transaccion venta3 = new TransaccionBuilder()
                .withAmount(45.75)
                .withDescription("Zapatos deportivos")
                .withType(TransactionType.VENTA)
                .withPaymentMethod(mobileWithDiscount)
                .build();

        cashRegister.ProcesarVenta(venta3);

        // Ejemplo 4: Registrar un gasto
        Transaccion gasto = new TransaccionBuilder()
                .withAmount(150.0)
                .withDescription("Pago de servicios")
                .withType(TransactionType.GASTO)
                .withPaymentMethod(new CashPayment(150.0)) // El método de pago es solo para tracking
                .build();

        cashRegister.registrarGasto(gasto);

        // Mostrar resumen diario
        cashRegister.imprimirResumenDiario();
    }
    
}
