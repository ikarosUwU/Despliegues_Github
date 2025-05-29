/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Singleton;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import modelo.Transaccion;
import modelo.TransactionType;
import observer.TransactionObserver;

/**
 *
 * @author USUARIO
 */
public class TransactionHistory {
    
    private static final String ARCHIVO_HISTORIAL = "historial.dat";
    
    private static TransactionHistory instance;

    private ArrayList<Transaccion> transacciones;

    private List<TransactionObserver> observers;
    
    private TransactionHistory() {
        observers = new ArrayList<>();
        cargarHistorial(); // Carga datos al inicializar
    }
    
    // Método para obtener la instancia única
    public static synchronized TransactionHistory getInstance() {
        if (instance == null) {
            instance = new TransactionHistory();
        }
        return instance;
    }
    
    // Añadir una transacción al historial
    public void addTransaction(Transaccion transaccion) {
        if (transaccion == null) {
            throw new IllegalArgumentException("La transacción no puede ser null");
        }
        transacciones.add(transaccion);
        notifyObservers(transaccion);
        guardarHistorial(); // Auto-guardado
    }
    
    // Obtener todas las transacciones
    public ArrayList<Transaccion> getAllTransactions() {
        return new ArrayList<>(transacciones);
    }
    
    // Calcular el total de ventas
   public double getTotalSales() {
        double totalVentas = 0.0;
        for (Transaccion transaccion : transacciones) {
            if (transaccion.getTipo() == TransactionType.VENTA) {
                totalVentas += transaccion.getCantidad();
            }
        }
        return totalVentas;
    }
    
    // Calcular el total de gastos
    public double getTotalExpenses() {
        double totalGastos = 0.0;
        for (Transaccion transaccion : transacciones) {
            if (transaccion.getTipo() == TransactionType.GASTO) {
                totalGastos += transaccion.getCantidad();
            }
        }
        return totalGastos;
    }
    public double getBalance() {
        return getTotalSales() - getTotalExpenses();
    }
    
    public void addObserver(TransactionObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }
    
    public void removeObserver(TransactionObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers(Transaccion transaction) {
        for (TransactionObserver observer : observers) {
            observer.update(transaction);
        }
    }
    
    public void cargarHistorial() {
        try (FileInputStream archivo = new FileInputStream(ARCHIVO_HISTORIAL);
             ObjectInputStream lector = new ObjectInputStream(archivo)) {
            
            Object obj = lector.readObject();
            if (obj instanceof ArrayList) {
                this.transacciones = (ArrayList<Transaccion>) obj;
            } else {
                this.transacciones = new ArrayList<>();
            }
            
        } catch (IOException | ClassNotFoundException ex) {
            // Si no existe el archivo o hay error, inicializar lista vacía
            this.transacciones = new ArrayList<>();
            System.out.println("Inicializando historial vacío: " + ex.getMessage());
        }
    }
    
    // Guardar historial a archivo
    public void guardarHistorial() {
        try (FileOutputStream archivo = new FileOutputStream(ARCHIVO_HISTORIAL);
             ObjectOutputStream escritor = new ObjectOutputStream(archivo)) {
            
            escritor.writeObject(transacciones);
            
        } catch (IOException ex) {
            System.err.println("Error al guardar historial: " + ex.getMessage());
        }
    }
    
}
