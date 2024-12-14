/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.enums.Rol;
import cat.copernic.ranare.exceptions.DuplicateResourceException;
import cat.copernic.ranare.service.mysql.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author reyes
 */
@Controller
@RequestMapping("/public/registre")
public class RegistreController {
    
    @Autowired
    ClientService clientService;
    
    @GetMapping("/pas1")
    public String showForm(Model model) {
        model.addAttribute("client", new Client());
        return "registre-client"; // Plantilla Thymeleaf para el formulario de creación
    }
    
    @PostMapping("/pas1")
    public String validarPas1(@ModelAttribute @Valid Client client,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes, Model model){
        
        if (client.getDni() != null) {
            client.setDni(client.getDni().toUpperCase());
        }
        
        try{
            Client savedClient = clientService.saveClient(client, false, bindingResult);
            
            if (savedClient == null || bindingResult.hasErrors()) {
                return "registre-client";  // Si hay errores de duplicados, vuelve al formulario
            }
        }catch (DuplicateResourceException e) {
            // Manejar errores de duplicados
            if (e.getMessage().contains("DNI")) {
                bindingResult.rejectValue("dni", "duplicate.dni", e.getMessage());
            }
            if (e.getMessage().contains("email")) {
                bindingResult.rejectValue("email", "duplicate.email", e.getMessage());
            }
            if (e.getMessage().contains("username)")){
                bindingResult.rejectValue("username", "duplicate.username", e.getMessage());
            }
            return "registre-client";  // Vuelve al formulario si hay errores
        }
        
        return "redirect:/public/registre/pas2";
    }
    
    
}
