/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.home.alertaProductos.entity.response;

/**
 *
 * @author ext_dayanez
 */
public class CrawlerResponse {
    String mensaje;

    public CrawlerResponse() {
    }

    public CrawlerResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    
}
