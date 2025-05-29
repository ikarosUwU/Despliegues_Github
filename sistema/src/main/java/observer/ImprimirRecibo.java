/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package observer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.FileWriter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import modelo.Transaccion;
import modelo.TransactionType;

/**
 *
 * @author USUARIO
 */
public class ImprimirRecibo implements TransactionObserver{

    private String nombreTienda; // Necesitas esta variable
    
    // Constructor para inicializar el nombre de la tienda
    public ImprimirRecibo(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }
    
    // Constructor sin parámetros (usa nombre por defecto)
    public ImprimirRecibo() {
        this.nombreTienda = "Mi Tienda";
    }

    @Override
    public void update(Transaccion transaccion) {
        if (transaccion.getTipo() == TransactionType.VENTA) {
            // Cambiar para mostrar el panel en lugar de consola
            mostrarReciboVentaModal(transaccion);
            
            System.out.println("\n======= RECIBO =======");
            System.out.println("Fecha: " + transaccion.getFecha());
            System.out.println("Descripción: " + transaccion.getDescripcion());
            System.out.println("Monto: $" + String.format("%.2f", transaccion.getCantidad()));
            System.out.println("Método de pago: " + transaccion.getMetodoPago().getDetallesdePago());
            System.out.println("======================\n");
            
        }
    }

    // Método específico para recibos de venta con información completa
    public void mostrarReciboVentaModal(Transaccion transaccion) {
        JDialog dialog = new JDialog((Frame) null, "Recibo de Venta", true);
        dialog.setLayout(new BorderLayout());

        // Panel principal del recibo
        JPanel panelRecibo = new JPanel();
        panelRecibo.setLayout(new BorderLayout());
        panelRecibo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelRecibo.setBackground(Color.WHITE);

        // Encabezado
        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("RECIBO DE VENTA", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (nombreTienda != null && !nombreTienda.isEmpty()) {
            JLabel lblTienda = new JLabel(nombreTienda, JLabel.CENTER);
            lblTienda.setFont(new Font("Arial", Font.BOLD, 16));
            lblTienda.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelHeader.add(lblTienda);
            panelHeader.add(Box.createVerticalStrut(5));
        }

        panelHeader.add(lblTitulo);
        panelHeader.add(Box.createVerticalStrut(10));
        panelHeader.add(new JSeparator());

        // Detalles de la transacción
        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new GridBagLayout());
        panelDetalles.setBackground(Color.WHITE);
        panelDetalles.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 0, 8, 15);

        // Fecha
        gbc.gridx = 0; gbc.gridy = 0;
        panelDetalles.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        JLabel lblFecha = new JLabel(transaccion.getFecha().toString());
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 12));
        panelDetalles.add(lblFecha, gbc);

        // Descripción
        gbc.gridx = 0; gbc.gridy = 1;
        panelDetalles.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        JLabel lblDescripcion = new JLabel(transaccion.getDescripcion());
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        panelDetalles.add(lblDescripcion, gbc);

        // Método de pago
        gbc.gridx = 0; gbc.gridy = 2;
        panelDetalles.add(new JLabel("Método de pago:"), gbc);
        gbc.gridx = 1;
        JLabel lblMetodoPago = new JLabel(transaccion.getMetodoPago().getDetallesdePago());
        lblMetodoPago.setFont(new Font("Arial", Font.PLAIN, 12));
        panelDetalles.add(lblMetodoPago, gbc);

        // Separador antes del monto
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelDetalles.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy = 4;
        panelDetalles.add(new JSeparator(), gbc);

        // Monto (destacado)
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel lblMontoTexto = new JLabel("TOTAL:");
        lblMontoTexto.setFont(new Font("Arial", Font.BOLD, 14));
        panelDetalles.add(lblMontoTexto, gbc);

        gbc.gridx = 1;
        JLabel lblMonto = new JLabel("$" + String.format("%.2f", transaccion.getCantidad()));
        lblMonto.setFont(new Font("Arial", Font.BOLD, 16));
        lblMonto.setForeground(new Color(0, 128, 0)); // Verde para ventas
        panelDetalles.add(lblMonto, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(Color.WHITE);

        JButton btnImprimir = new JButton("Imprimir");
        btnImprimir.addActionListener(e -> {
            imprimirReciboVenta(transaccion);
            JOptionPane.showMessageDialog(dialog, "Recibo enviado a impresora", "Impresión", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            guardarReciboComoArchivo(transaccion);
        });

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());

        panelBotones.add(btnImprimir);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCerrar);

        // Ensamblar el recibo
        panelRecibo.add(panelHeader, BorderLayout.NORTH);
        panelRecibo.add(panelDetalles, BorderLayout.CENTER);
        panelRecibo.add(panelBotones, BorderLayout.SOUTH);

        dialog.add(panelRecibo);
        dialog.setSize(420, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }
    
    // Método para imprimir recibo de venta (FALTABA ESTE)
    private void imprimirReciboVenta(Transaccion transaccion) {
        try {
            StringBuilder recibo = new StringBuilder();
            recibo.append("======================================\n");
            if (nombreTienda != null && !nombreTienda.isEmpty()) {
                recibo.append("         ").append(nombreTienda).append("\n");
            }
            recibo.append("           RECIBO DE VENTA\n");
            recibo.append("======================================\n");
            recibo.append("Fecha: ").append(transaccion.getFecha()).append("\n");
            recibo.append("Descripción: ").append(transaccion.getDescripcion()).append("\n");
            recibo.append("Método de pago: ").append(transaccion.getMetodoPago().getDetallesdePago()).append("\n");
            recibo.append("--------------------------------------\n");
            recibo.append("TOTAL: $").append(String.format("%.2f", transaccion.getCantidad())).append("\n");
            recibo.append("======================================\n");
            recibo.append("       Gracias por su compra\n");
            recibo.append("======================================\n");
            
            // Aquí puedes implementar la lógica real de impresión
            System.out.println(recibo.toString()); // Por ahora en consola
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al imprimir: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para guardar recibo como archivo (FALTABA ESTE)
    private void guardarReciboComoArchivo(Transaccion transaccion) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("recibo_" + 
                transaccion.getFecha().toString().replace(":", "-").replace(" ", "_") + ".txt"));
            
            int resultado = fileChooser.showSaveDialog(null);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                java.io.File archivo = fileChooser.getSelectedFile();
                
                try (FileWriter writer = new FileWriter(archivo)) {
                    writer.write("======================================\n");
                    if (nombreTienda != null && !nombreTienda.isEmpty()) {
                        writer.write("         " + nombreTienda + "\n");
                    }
                    writer.write("           RECIBO DE VENTA\n");
                    writer.write("======================================\n");
                    writer.write("Fecha: " + transaccion.getFecha() + "\n");
                    writer.write("Descripción: " + transaccion.getDescripcion() + "\n");
                    writer.write("Método de pago: " + transaccion.getMetodoPago().getDetallesdePago() + "\n");
                    writer.write("--------------------------------------\n");
                    writer.write("TOTAL: $" + String.format("%.2f", transaccion.getCantidad()) + "\n");
                    writer.write("======================================\n");
                    writer.write("       Gracias por su compra\n");
                    writer.write("======================================\n");
                    
                    JOptionPane.showMessageDialog(null, 
                        "Recibo guardado exitosamente en: " + archivo.getAbsolutePath(), 
                        "Guardado", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al guardar: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Getter y Setter para el nombre de la tienda
    public String getNombreTienda() {
        return nombreTienda;
    }
    
    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }
}