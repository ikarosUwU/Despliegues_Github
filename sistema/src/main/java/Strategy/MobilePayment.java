/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Strategy;

/**
 *
 * @author USUARIO
 */
public class MobilePayment implements PaymentMethod{
    private String numCelular;
    private String nombreApp;

    public MobilePayment(String numCelular, String nombreApp) {
        this.numCelular = numCelular;
        this.nombreApp = nombreApp;
    }

    @Override
    public boolean procesoPago(double cantidad) {
        // Simulación de procesamiento de pago móvil
        System.out.println("Enviando solicitud de pago a " + nombreApp + " para el número " + numCelular);
        System.out.println("Pago móvil de $" + String.format("%.2f", cantidad) + " confirmado");
        return true;
    }

    @Override
    public String getDetallesdePago() {
        return "Pago móvil con " + nombreApp + " - Teléfono: " + numCelular;
    }
    
}
