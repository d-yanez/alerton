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
public class Price {
    String originalPrice;
    Integer type;

    public Price() {
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Price{" + "originalPrice=" + originalPrice + ", type=" + type + '}';
    }
    
    
}
