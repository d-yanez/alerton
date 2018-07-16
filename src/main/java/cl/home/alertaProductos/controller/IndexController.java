/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.home.alertaProductos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ext_dayanez
 */
@Controller
public class IndexController {
    
    @Value("${spring.application.name}")
    String appName;
    
    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("appName", appName);
        return "index";
    }
    
}
