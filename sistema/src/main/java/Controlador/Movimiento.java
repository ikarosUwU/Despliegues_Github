/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Singleton.TransactionHistory;
import Strategy.CashPayment;
import Strategy.CreditCardPayment;
import Strategy.MobilePayment;
import Strategy.PaymentMethod;
import builder.TransaccionBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import modelo.Transaccion;
import modelo.TransactionType;
import observer.ImprimirRecibo;
import sistema.sistema.CashRegisterSystem;

/**
 *
 * @author USUARIO
 */
public class Movimiento {

    
    public Movimiento() {
    }
    
    
    public void registrarMovimiento(JTextField txtFecha, 
                                  JTextField txtMonto, 
                                  JTextField txtDescripcion,
                                  JComboBox<String> cmbTipo,
                                  JComboBox<String> cmbMetodoPago,
                                  JTextField txtDatosAdicionales,
                                  JTable tblHistorial) {
        
        try {
            // 1. Validar y obtener los datos
            Date fecha = validarFecha(txtFecha.getText().trim());
            double monto = validarMonto(txtMonto.getText().trim());
            String descripcion = validarDescripcion(txtDescripcion.getText().trim());
            TransactionType tipo = validarTipo((String) cmbTipo.getSelectedItem());
            PaymentMethod metodoPago = crearMetodoPago((String) cmbMetodoPago.getSelectedItem(), txtDatosAdicionales.getText().trim(), monto);
            
            // 2. Crear la transacción usando el Builder
            Transaccion transaccion = new TransaccionBuilder()
                .withAmount(monto)
                .withDescription(descripcion)
                .withType(tipo)
                .withPaymentMethod(metodoPago)
                .build();
            
            if (!esFechaActual(fecha)) {
                System.out.println("Nota: Usando fecha actual. Fecha ingresada: " + fecha);
            }
            
            CashRegisterSystem sistema = new CashRegisterSystem("Mi Tienda");
            boolean exito;
            
            if (tipo == TransactionType.VENTA) {
                exito = sistema.ProcesarVenta(transaccion);
            } else {
                sistema.registrarGasto(transaccion);
                exito = true;
            }
            
            if (exito) {
                JOptionPane.showMessageDialog(null, "Movimiento registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos(txtFecha, txtMonto, txtDescripcion, txtDatosAdicionales);
                
                if (tblHistorial != null) {
                    HistorialController historialController = new HistorialController();
                    historialController.actualizarTabla(tblHistorial);
                }
            }
            
        } catch (Exception e) {
            // Mostrar mensaje de error
            JOptionPane.showMessageDialog(null, "Error al registrar el movimiento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Date validarFecha(String fechaTexto) throws ParseException {
        if (fechaTexto.isEmpty()) {
            return new Date(); // Fecha actual si está vacío
        }
        
        SimpleDateFormat formato1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            return formato1.parse(fechaTexto);
        } catch (ParseException e) {
            try {
                return formato2.parse(fechaTexto);
            } catch (ParseException e2) {
                throw new ParseException("Formato de fecha inválido. Use: dd/MM/yyyy o dd/MM/yyyy HH:mm:ss", 0);
            }
        }
    }
    
    private double validarMonto(String montoTexto) throws NumberFormatException {
        if (montoTexto.isEmpty()) {
            throw new NumberFormatException("El monto no puede estar vacío");
        }
        
        // Remover símbolos de moneda si existen
        montoTexto = montoTexto.replace("$", "").replace(",", "");
        
        double monto = Double.parseDouble(montoTexto);
        
        if (monto <= 0) {
            throw new NumberFormatException("El monto debe ser mayor que 0");
        }
        
        return monto;
    }
    
    private String validarDescripcion(String descripcion) {
        if (descripcion.isEmpty()) {
            return "Sin descripción";
        }
        return descripcion;
    }
    
    private TransactionType validarTipo(String tipoSeleccionado) {
        if (tipoSeleccionado == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de transacción");
        }
        
        switch (tipoSeleccionado.toUpperCase()) {
            case "VENTA":
                return TransactionType.VENTA;
            case "GASTO":
                return TransactionType.GASTO;
            default:
                throw new IllegalArgumentException("Tipo de transacción no válido: " + tipoSeleccionado);
        }
    }
    
    private PaymentMethod crearMetodoPago(String tipoMetodo, String datosAdicionales, double monto) {
        if (tipoMetodo == null) {
            throw new IllegalArgumentException("Debe seleccionar un método de pago");
        }
        
        switch (tipoMetodo.toUpperCase()) {
            case "EFECTIVO":
                double efectivoRecibido = datosAdicionales.isEmpty() ? monto : Double.parseDouble(datosAdicionales);
                return new CashPayment(efectivoRecibido);
                
            case "TARJETA DE CREDITO":
                String[] datosTarjeta = datosAdicionales.split(",");
                String numeroTarjeta = datosTarjeta.length > 0 ? datosTarjeta[0].trim() : "1234567890123456";
                String titular = datosTarjeta.length > 1 ? datosTarjeta[1].trim() : "Usuario";
                return new CreditCardPayment(numeroTarjeta, titular);
                
            case "PAGO MOVIL":
                String[] datosMobile = datosAdicionales.split(",");
                String telefono = datosMobile.length > 0 ? datosMobile[0].trim() : "555-0000";
                String app = datosMobile.length > 1 ? datosMobile[1].trim() : "PayApp";
                return new MobilePayment(telefono, app);
                
            default:
                throw new IllegalArgumentException("Método de pago no válido: " + tipoMetodo);
        }
    }
    
    private boolean esFechaActual(Date fecha) {
        Date ahora = new Date();
        long diferencia = Math.abs(ahora.getTime() - fecha.getTime());
        return diferencia < 60000; // Menos de 1 minuto de diferencia
    }
    
    private void limpiarCampos(JTextField txtFecha, JTextField txtMonto, JTextField txtDescripcion, JTextField txtDatosAdicionales) {
        txtFecha.setText("");
        txtMonto.setText("");
        txtDescripcion.setText("");
        txtDatosAdicionales.setText("");
    }
    
    public void guardarDatos() {
        try {
            TransactionHistory historial = TransactionHistory.getInstance();
            historial.guardarHistorial();
            
            JOptionPane.showMessageDialog(null, 
                "Datos guardados correctamente en archivo", 
                "Guardado Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al guardar: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Método para botón "Cargar Datos"
     */
    public void cargarDatos(JTable tblHistorial) {
        try {
            TransactionHistory historial = TransactionHistory.getInstance();
            historial.cargarHistorial();
            
            // Actualizar tabla
            HistorialController controller = new HistorialController();
            controller.actualizarTabla(tblHistorial);
            
            JOptionPane.showMessageDialog(null, 
                "Datos cargados correctamente", 
                "Carga Exitosa", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al cargar: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
