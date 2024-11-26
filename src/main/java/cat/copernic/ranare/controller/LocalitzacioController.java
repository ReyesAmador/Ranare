/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Agent;
import cat.copernic.ranare.entity.mysql.Localitzacio;
import cat.copernic.ranare.exceptions.InvalidCodiPostalException;
import cat.copernic.ranare.service.mysql.LocalitzacioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author reyes
 */

@Controller
@RequestMapping("/localitzacio")
public class LocalitzacioController {
    
    @Autowired  
    LocalitzacioService localitzacioService;
    
    @GetMapping
    public String mostrarLocalitzacions(Model model) {
        List<Localitzacio> localitzacions = localitzacioService.getallLocalitzacio();
        model.addAttribute("localitzacions", localitzacions);
        return "localitzacio"; 
    }
    
    @GetMapping("/crear-localitzacio")
    public String showCrearLocalitzacioPage(Model model){
        model.addAttribute("localitzacio", new Localitzacio());
        
        return "crear-localitzacio";
    }
    
    //Processar formulari
    
    @PostMapping("/crear-localitzacio")
    public String createLocalitzacio(@ModelAttribute Localitzacio local, RedirectAttributes redirectAttributes, Model model){
        
        try{
        localitzacioService.saveLocalitzacio(local);
        
        /*Los flash attributes son ideales cuando necesitas pasar datos entre diferentes peticiones sin que estos datos aparezcan en la URL.
        Lo utilizamos porque con redirect se pierden los atributos y los mensajes que podemos pasar*/
        redirectAttributes.addFlashAttribute("success", "Localització afegida correctament!");
        
        return "redirect:/localitzacio";
        
        }catch(InvalidCodiPostalException e){
            model.addAttribute("error", e.getMessage());
            return "crear-localitzacio";
        }
    }
}
