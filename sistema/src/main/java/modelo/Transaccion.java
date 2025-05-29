/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import Strategy.PaymentMethod;
import java.util.Date;

/**
 *
 * @author USUARIO
 */
public class Transaccion {

    private Date fecha;
    private double cantidad;
    private String descripcion;
    private TransactionType tipo;
    private PaymentMethod metodoPago;

    public Transaccion(double cantidad, String descripcion, TransactionType tipo, PaymentMethod metodoPago) {
        this.fecha = new Date();
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.metodoPago = metodoPago;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TransactionType getTipo() {
        return tipo;
    }

    public PaymentMethod getMetodoPago() {
        return metodoPago;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f - %s - Método: %s", 
                tipo, fecha, cantidad, descripcion, metodoPago.getClass().getSimpleName());
    }
}

