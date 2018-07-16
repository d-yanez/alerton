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
public class ProductConfig {
    State state;

    public ProductConfig() {
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ProductConfig{" + "state=" + state + '}';
    }
    
}
