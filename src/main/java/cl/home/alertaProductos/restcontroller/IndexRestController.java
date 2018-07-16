/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.home.alertaProductos.restcontroller;

import cl.home.alertaProductos.entity.response.CrawlerResponse;
import cl.home.alertaProductos.rest.services.CrawlerServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ext_dayanez
 */
@RestController
@RequestMapping("/api/crawler")
public class IndexRestController {
    
    private static final Logger log = LoggerFactory.getLogger(IndexRestController.class);
    
    @Autowired
    CrawlerServices crawlerServices;
    
    @GetMapping
    public CrawlerResponse crawler(){
     log.info("crawler....");
     crawlerServices.test("Dy");
     //crawlerServices.getPageLinks("https://www.falabella.com/falabella-cl/category/cat3215/Sabanas");
//     crawlerServices.getPageLinks("https://www.falabella.com/falabella-cl/product/");
//     crawlerServices.getPageLinks("https://www.falabella.com/falabella-cl/category/");
//     
     
     
     
    return new CrawlerResponse("Hi from crawler web!!");
        
    }
    
    
}
