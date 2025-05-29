/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package preintentos;

/**
 *
 * @author USUARIO
 */
import sistema.sistema.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 1. Patrón Singleton - Historial de transacciones
class TransactionHistory {
    // Instancia única
    private static TransactionHistory instance;
    
    // Lista de transacciones
    private List<Transaction> transactions;
    
    // Observadores
    private List<TransactionObserver> observers;
    
    // Constructor privado
    private TransactionHistory() {
        transactions = new ArrayList<>();
        observers = new ArrayList<>();
    }
    
    // Método para obtener la instancia única
    public static synchronized TransactionHistory getInstance() {
        if (instance == null) {
            instance = new TransactionHistory();
        }
        return instance;
    }
    
    // Añadir una transacción al historial
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        notifyObservers(transaction);
    }
    
    // Obtener todas las transacciones
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
    
    // Calcular el total de ventas
    public double getTotalSales() {
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.SALE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    
    // Calcular el total de gastos
    public double getTotalExpenses() {
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    
    // Obtener el balance (ventas - gastos)
    public double getBalance() {
        return getTotalSales() - getTotalExpenses();
    }
    
    // Métodos para el patrón Observer
    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(TransactionObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers(Transaction transaction) {
        for (TransactionObserver observer : observers) {
            observer.update(transaction);
        }
    }
}

// Enum para tipos de transacción
enum TransactionType {
    SALE, EXPENSE
}

// Clase Transaction para almacenar los datos de cada transacción
class Transaction {
    private Date date;
    private double amount;
    private String description;
    private TransactionType type;
    private PaymentMethod paymentMethod;
    
    public Transaction(double amount, String description, TransactionType type, PaymentMethod paymentMethod) {
        this.date = new Date();
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.paymentMethod = paymentMethod;
    }
    
    public Date getDate() {
        return date;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f - %s - Método: %s", 
                type, date, amount, description, paymentMethod.getClass().getSimpleName());
    }
}

// 2. Patrón Strategy - Métodos de Pago
interface PaymentMethod {
    boolean processPayment(double amount);
    String getPaymentDetails();
}

// Implementaciones concretas de métodos de pago
class CashPayment implements PaymentMethod {
    private double cashReceived;
    
    public CashPayment(double cashReceived) {
        this.cashReceived = cashReceived;
    }
    
    @Override
    public boolean processPayment(double amount) {
        if (cashReceived >= amount) {
            double change = cashReceived - amount;
            System.out.println("Pago en efectivo procesado. Cambio: $" + String.format("%.2f", change));
            return true;
        } else {
            System.out.println("Efectivo insuficiente");
            return false;
        }
    }
    
    @Override
    public String getPaymentDetails() {
        return "Pago en efectivo - Recibido: $" + String.format("%.2f", cashReceived);
    }
}

class CreditCardPayment implements PaymentMethod {
    private String cardNumber;
    private String cardholderName;
    
    public CreditCardPayment(String cardNumber, String cardholderName) {
        this.cardNumber = maskCardNumber(cardNumber);
        this.cardholderName = cardholderName;
    }
    
    private String maskCardNumber(String number) {
        if (number.length() < 4) return number;
        return "XXXX-XXXX-XXXX-" + number.substring(number.length() - 4);
    }
    
    @Override
    public boolean processPayment(double amount) {
        // Simulación de procesamiento de pago con tarjeta
        System.out.println("Procesando pago con tarjeta: $" + String.format("%.2f", amount));
        System.out.println("Autorización aprobada para " + cardholderName);
        return true;
    }
    
    @Override
    public String getPaymentDetails() {
        return "Tarjeta de crédito: " + cardNumber + " - Titular: " + cardholderName;
    }
}

class MobilePayment implements PaymentMethod {
    private String phoneNumber;
    private String appName;
    
    public MobilePayment(String phoneNumber, String appName) {
        this.phoneNumber = phoneNumber;
        this.appName = appName;
    }
    
    @Override
    public boolean processPayment(double amount) {
        // Simulación de procesamiento de pago móvil
        System.out.println("Enviando solicitud de pago a " + appName + " para el número " + phoneNumber);
        System.out.println("Pago móvil de $" + String.format("%.2f", amount) + " confirmado");
        return true;
    }
    
    @Override
    public String getPaymentDetails() {
        return "Pago móvil con " + appName + " - Teléfono: " + phoneNumber;
    }
}

// 3. Patrón Observer - Para monitoreo de transacciones
interface TransactionObserver {
    void update(Transaction transaction);
}

// Implementaciones concretas de observadores
/*
class PrintReceiptObserver implements TransactionObserver {
    @Override
    public void update(Transaction transaction) {
        if (transaction.getType() == TransactionType.SALE) {
            System.out.println("\n======= RECIBO =======");
            System.out.println("Fecha: " + transaction.getDate());
            System.out.println("Descripción: " + transaction.getDescription());
            System.out.println("Monto: $" + String.format("%.2f", transaction.getAmount()));
            System.out.println("Método de pago: " + transaction.getPaymentMethod().getPaymentDetails());
            System.out.println("======================\n");
        }
    }
}

class AccountingObserver implements TransactionObserver {
    @Override
    public void update(Transaction transaction) {
        String transactionType = transaction.getType() == TransactionType.SALE ? "VENTA" : "GASTO";
        System.out.println("[CONTABILIDAD] Registrando " + transactionType + ": $" + 
                           String.format("%.2f", transaction.getAmount()));
    }
}

// 4. Patrón Builder - Para construir transacciones
class TransactionBuilder {
    private double amount;
    private String description;
    private TransactionType type;
    private PaymentMethod paymentMethod;
    
    public TransactionBuilder() {
        // Valores por defecto
        this.description = "Sin descripción";
        this.type = TransactionType.SALE;
    }
    
    public TransactionBuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }
    
    public TransactionBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public TransactionBuilder withType(TransactionType type) {
        this.type = type;
        return this;
    }
    
    public TransactionBuilder withPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }
    
    public Transaction build() {
        if (paymentMethod == null) {
            throw new IllegalStateException("El método de pago no puede ser nulo");
        }
        
        return new Transaction(amount, description, type, paymentMethod);
    }
}

// 5. Patrón Decorator - Para añadir funcionalidades a las transacciones
abstract class TransactionDecorator implements PaymentMethod {
    protected PaymentMethod wrapped;
    
    public TransactionDecorator(PaymentMethod wrapped) {
        this.wrapped = wrapped;
    }
}

class LoyaltyPointsDecorator extends TransactionDecorator {
    private String customerId;
    private int pointsPerDollar;
    
    public LoyaltyPointsDecorator(PaymentMethod wrapped, String customerId, int pointsPerDollar) {
        super(wrapped);
        this.customerId = customerId;
        this.pointsPerDollar = pointsPerDollar;
    }
    
    @Override
    public boolean processPayment(double amount) {
        boolean result = wrapped.processPayment(amount);
        if (result) {
            int pointsEarned = (int)(amount * pointsPerDollar);
            System.out.println("Cliente " + customerId + " ha ganado " + pointsEarned + " puntos de fidelidad");
        }
        return result;
    }
    
    @Override
    public String getPaymentDetails() {
        return wrapped.getPaymentDetails() + " + Programa de Fidelidad (ID: " + customerId + ")";
    }
}

class DiscountDecorator extends TransactionDecorator {
    private double discountPercentage;
    
    public DiscountDecorator(PaymentMethod wrapped, double discountPercentage) {
        super(wrapped);
        this.discountPercentage = discountPercentage;
    }
    
    @Override
    public boolean processPayment(double amount) {
        double discountAmount = amount * (discountPercentage / 100);
        double finalAmount = amount - discountAmount;
        System.out.println("Aplicando descuento de " + discountPercentage + "% ($" + 
                           String.format("%.2f", discountAmount) + ")");
        return wrapped.processPayment(finalAmount);
    }
    
    @Override
    public String getPaymentDetails() {
        return wrapped.getPaymentDetails() + " con descuento del " + discountPercentage + "%";
    }
}

// 6. Clase Principal - Sistema de Caja
class CashRegisterSystem {
    private String storeName;
    private TransactionHistory history;
    
    public CashRegisterSystem(String storeName) {
        this.storeName = storeName;
        this.history = TransactionHistory.getInstance();
        
        // Registrar observadores
        history.addObserver(new PrintReceiptObserver());
        history.addObserver(new AccountingObserver());
    }
    
    public boolean processSale(Transaction transaction) {
        PaymentMethod paymentMethod = transaction.getPaymentMethod();
        boolean paymentSuccess = paymentMethod.processPayment(transaction.getAmount());
        
        if (paymentSuccess) {
            history.addTransaction(transaction);
            System.out.println("Venta registrada correctamente");
        } else {
            System.out.println("La venta no pudo ser procesada");
        }
        
        return paymentSuccess;
    }
    
    public void registerExpense(Transaction transaction) {
        history.addTransaction(transaction);
        System.out.println("Gasto registrado correctamente");
    }
    
    public void printDailySummary() {
        System.out.println("\n======= RESUMEN DIARIO: " + storeName + " =======");
        System.out.println("Total Ventas: $" + String.format("%.2f", history.getTotalSales()));
        System.out.println("Total Gastos: $" + String.format("%.2f", history.getTotalExpenses()));
        System.out.println("Balance: $" + String.format("%.2f", history.getBalance()));
        System.out.println("========================================\n");
    }
}

// Demo del sistema
public class Sistema {
    public static void main(String[] args) {
        // Creamos nuestro sistema de caja
        CashRegisterSystem cashRegister = new CashRegisterSystem("Mi Tienda");
        
        // Ejemplo 1: Venta con pago en efectivo
        PaymentMethod cashPayment = new CashPayment(100.0);
        Transaction sale1 = new TransactionBuilder()
                .withAmount(75.50)
                .withDescription("Compra de groceries")
                .withType(TransactionType.SALE)
                .withPaymentMethod(cashPayment)
                .build();
        
        cashRegister.processSale(sale1);
        
        // Ejemplo 2: Venta con tarjeta de crédito + puntos de fidelidad
        PaymentMethod creditCard = new CreditCardPayment("1234567890123456", "Juan Pérez");
        PaymentMethod creditCardWithLoyalty = new LoyaltyPointsDecorator(creditCard, "CUST-001", 10);
        
        Transaction sale2 = new TransactionBuilder()
                .withAmount(199.99)
                .withDescription("Televisor LED 32\"")
                .withType(TransactionType.SALE)
                .withPaymentMethod(creditCardWithLoyalty)
                .build();
        
        cashRegister.processSale(sale2);
        
        // Ejemplo 3: Venta con pago móvil y descuento
        PaymentMethod mobilePayment = new MobilePayment("555-123456", "PayApp");
        PaymentMethod mobileWithDiscount = new DiscountDecorator(mobilePayment, 15.0);
        
        Transaction sale3 = new TransactionBuilder()
                .withAmount(45.75)
                .withDescription("Zapatos deportivos")
                .withType(TransactionType.SALE)
                .withPaymentMethod(mobileWithDiscount)
                .build();
        
        cashRegister.processSale(sale3);
        
        // Ejemplo 4: Registrar un gasto
        Transaction expense = new TransactionBuilder()
                .withAmount(150.0)
                .withDescription("Pago de servicios")
                .withType(TransactionType.EXPENSE)
                .withPaymentMethod(new CashPayment(150.0)) // El método de pago es solo para tracking
                .build();
        
        cashRegister.registerExpense(expense);
        
        // Mostrar resumen diario
        cashRegister.printDailySummary();
    }

}
*/