/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.home.alertaProductos.model;

/**
 *
 * @author ext_dayanez
 */
public class State {
    Product product;

    public State() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "State{" + "product=" + product + '}';
    }
    
}
