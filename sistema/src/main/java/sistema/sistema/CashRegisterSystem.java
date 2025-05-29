/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema.sistema;

import Singleton.TransactionHistory;
import Strategy.PaymentMethod;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import modelo.Transaccion;
import observer.ImprimirRecibo;
import observer.observadorContable;

/**
 *
 * @author USUARIO
 */
public class CashRegisterSystem {
    
    private String nombreTienda;
    private TransactionHistory historial;

    public CashRegisterSystem(String nombreTienda) {
        this.nombreTienda = nombreTienda;
        this.historial = TransactionHistory.getInstance();

        // Registrar observadores
        historial.addObserver(new ImprimirRecibo());
        historial.addObserver(new observadorContable());
    }

    public boolean ProcesarVenta(Transaccion transaccion) {
        PaymentMethod paymentMethod = transaccion.getMetodoPago();
        boolean paymentSuccess = paymentMethod.procesoPago(transaccion.getCantidad());

        if (paymentSuccess) {
            historial.addTransaction(transaccion);
            System.out.println("Venta registrada correctamente");
        } else {
            System.out.println("La venta no pudo ser procesada");
        }

        return paymentSuccess;
    }

    public void registrarGasto(Transaccion transaccion) {
        historial.addTransaction(transaccion);
        System.out.println("Gasto registrado correctamente");
    }

    public void imprimirResumenDiario() {
        System.out.println("\n======= RESUMEN DIARIO: " + nombreTienda + " =======");
        System.out.println("Total Ventas: $" + String.format("%.2f", historial.getTotalSales()));
        System.out.println("Total Gastos: $" + String.format("%.2f", historial.getTotalExpenses()));
        System.out.println("Balance: $" + String.format("%.2f", historial.getBalance()));
        System.out.println("========================================\n");
    }
    
    public void imprimirResumenDiario2(){
        JPanel panelResumen = new JPanel();
        panelResumen.setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("RESUMEN DIARIO: " + nombreTienda, JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        // Panel central con la información
        JPanel panelInfo = new JPanel(new GridLayout(3, 2, 10, 10));

        // Etiquetas y valores
        panelInfo.add(new JLabel("Total Ventas:"));
        panelInfo.add(new JLabel("$" + String.format("%.2f", historial.getTotalSales())));

        panelInfo.add(new JLabel("Total Gastos:"));
        panelInfo.add(new JLabel("$" + String.format("%.2f", historial.getTotalExpenses())));

        panelInfo.add(new JLabel("Balance:"));
        JLabel labelBalance = new JLabel("$" + String.format("%.2f", historial.getBalance()));
        // Cambiar color según si es positivo o negativo
        if (historial.getBalance() >= 0) {
            labelBalance.setForeground(Color.GREEN);
        } else {
            labelBalance.setForeground(Color.RED);
        }
        panelInfo.add(labelBalance);

        // Botón para cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> {
            // Cerrar la ventana padre
            SwingUtilities.getWindowAncestor(panelResumen).dispose();
        });

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);

        // Agregar componentes al panel principal
        panelResumen.add(titulo, BorderLayout.NORTH);
        panelResumen.add(panelInfo, BorderLayout.CENTER);
        panelResumen.add(panelBoton, BorderLayout.SOUTH);

        // Crear y mostrar la ventana
        JFrame ventanaResumen = new JFrame("Resumen Diario");
        ventanaResumen.add(panelResumen);
        ventanaResumen.setSize(400, 250);
        ventanaResumen.setLocationRelativeTo(null); // Centrar en pantalla
        ventanaResumen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaResumen.setVisible(true);
    }
    
}
