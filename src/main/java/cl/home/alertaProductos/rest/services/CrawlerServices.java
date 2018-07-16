/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.home.alertaProductos.rest.services;

/**
 *
 * @author ext_dayanez
 */
public interface CrawlerServices {
    
    public void test(String mensaje);
    public void getPageLinks(String URL);
    public void getInfoProductos(String urlProducto);
    
}
