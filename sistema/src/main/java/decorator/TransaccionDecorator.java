/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package decorator;

import Strategy.PaymentMethod;

/**
 *
 * @author USUARIO
 */
public abstract class TransaccionDecorator implements PaymentMethod{
    
    protected Strategy.PaymentMethod wrapped;

    public TransaccionDecorator(Strategy.PaymentMethod wrapped) {
        this.wrapped = wrapped;
    }
}
