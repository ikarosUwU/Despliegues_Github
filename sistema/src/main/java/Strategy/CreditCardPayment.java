/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Strategy;

/**
 *
 * @author USUARIO
 */
public class CreditCardPayment implements PaymentMethod{
    
    private String numeroTarjeta;
    private String titularTarjeta;

    public CreditCardPayment(String numeroTarjeta, String titularTarjeta) {
        this.numeroTarjeta = maskNumeroTarjeta(numeroTarjeta);
        this.titularTarjeta = titularTarjeta;
    }

    private String maskNumeroTarjeta(String num) {
        if (num.length() < 4){
            return num;
        }
        return "XXXX-XXXX-XXXX-" + num.substring(num.length() - 4);
    }

    @Override
    public boolean procesoPago(double cantidad) {
        // Simulación de procesamiento de pago con tarjeta
        System.out.println("Procesando pago con tarjeta: $" + String.format("%.2f", cantidad));
        System.out.println("Autorización aprobada para " + titularTarjeta);
        return true;
    }

    @Override
    public String getDetallesdePago() {
        return "Tarjeta de crédito: " + numeroTarjeta + " - Titular: " + titularTarjeta;
    }
    
}
