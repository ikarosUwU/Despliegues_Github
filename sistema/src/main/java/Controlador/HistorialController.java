/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Singleton.TransactionHistory;
import Strategy.PaymentMethod;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Transaccion;
import modelo.TransactionType;

/**
 *
 * @author USUARIO
 */
public class HistorialController {
    
    public void llenarTablaHistorial(JTable tblHistorial) {
        try {
            // Obtener el historial del singleton
            TransactionHistory historial = TransactionHistory.getInstance();
            List<Transaccion> transacciones = historial.getAllTransactions();
            
            // Definir las columnas de la tabla
            String[] columnas = {"Fecha", "Tipo", "Descripción", "Monto", "Método de Pago"};
            
            // Crear el modelo de datos para la tabla
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Hacer la tabla no editable
                }
            };
            
            // Formato para mostrar la fecha
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            
            // Llenar la tabla con las transacciones
            for (Transaccion transaccion : transacciones) {
                Object[] fila = {
                    formatoFecha.format(transaccion.getFecha()),
                    transaccion.getTipo() == TransactionType.VENTA ? "VENTA" : "GASTO",
                    transaccion.getDescripcion(),
                    String.format("$%.2f", transaccion.getCantidad()),
                    transaccion.getMetodoPago()
                };
                modelo.addRow(fila);
            }
            
            // Asignar el modelo a la tabla
            tblHistorial.setModel(modelo);
            
            // Configurar el ancho de las columnas (opcional)
            configurarAnchoColumnas(tblHistorial);
            
            // Mostrar mensaje si no hay transacciones
            if (transacciones.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "No hay transacciones registradas", 
                    "Historial vacío", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al cargar el historial: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    /**
     * Método auxiliar para configurar el ancho de las columnas
     */
    private void configurarAnchoColumnas(JTable tabla) {
        // Configurar anchos preferidos para cada columna
        tabla.getColumnModel().getColumn(0).setPreferredWidth(150); // Fecha
        tabla.getColumnModel().getColumn(1).setPreferredWidth(80);  // Tipo
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200); // Descripción
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100); // Monto
        tabla.getColumnModel().getColumn(4).setPreferredWidth(150); // Método de Pago
    }
    
    /**
     * Método para actualizar la tabla (útil después de agregar nuevas transacciones)
     */
    public void actualizarTabla(JTable tblHistorial) {
        llenarTablaHistorial(tblHistorial);
    }
    
    /**
     * Método para limpiar la tabla
     */
    public void limpiarTabla(JTable tblHistorial) {
        DefaultTableModel modelo = (DefaultTableModel) tblHistorial.getModel();
        modelo.setRowCount(0);
    }
}
