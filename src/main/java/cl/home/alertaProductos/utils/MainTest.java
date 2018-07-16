/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.home.alertaProductos.utils;

import cl.home.alertaProductos.rest.services.impl.CrawlerServicesImpl;

/**
 *
 * @author ext_dayanez
 */
public class MainTest {
    
    public static void main(String[] args) {
        
        CrawlerServicesImpl c = new CrawlerServicesImpl();
        
        c.getPageLinks("https://www.falabella.com/falabella-cl/category/");
        //c.spider("https://www.falabella.com/falabella-cl/product/5846832/LED-49-49MU6500-4K-Ultra-HD-Smart-TV-Curvo/5846832");
        //c.spider("https://www.falabella.com/falabella-cl/product/4536307/MacBook-Pro-Intel-Core-i7-16GB-RAM-256GB-DD-15%2C4-/4536307");
        //c.spider("https://www.falabella.com/falabella-cl/product/6051368/AppleWatch-Serie-3-Gris/6051368");
        //c.spider("https://www.falabella.com/falabella-cl/product/5425428/Cama-Europea-Maximo-Cobre-2-Plazas-Base-Dividida-%2B-Muebles-Dresden-%2B-Textil/5425428");
        //c.getHtml("https://www.paris.cl/store/producto/par-de-piso-patas-de-color-attimo-584009-ppp-?color=Verde");
       // c.savePage("https://www.paris.cl/store/producto/par-de-piso-patas-de-color-attimo-584009-ppp-?color=Verde");
        
        //https://stackoverflow.com/questions/44525000/using-htmlunit-to-pre-render-a-javascript-website-html-snapshot
        //http://stanford.edu/~mgorkove/cgi-bin/rpython_tutorials/Scraping_a_Webpage_Rendered_by_Javascript_Using_Python.php
        //http://zetcode.com/articles/javareadwebpage/
        //c.downloadPage();
        
                
    }
}
