/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.configuration.ValidadorUsuaris;
import cat.copernic.ranare.entity.mysql.Agent;
import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.enums.Rol;
import cat.copernic.ranare.exceptions.AgentNotFoundException;
import cat.copernic.ranare.exceptions.ClientNotFoundException;
import cat.copernic.ranare.service.mysql.AgentService;
import cat.copernic.ranare.service.mysql.ClientService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author reyes
 */
@Controller
public class LoginController {
    
    @Autowired
    private ValidadorUsuaris validador;
    
    @Autowired
    private PasswordEncoder passEncoder;
    
    @PostMapping("/public/login")
    public String login(@RequestParam String username, @RequestParam String pass, Model model){
        try{
            UserDetails user = validador.loadUserByUsername(username);
            if(!passEncoder.matches(pass, user.getPassword())){
                model.addAttribute("error", "Usuari i/o contrasenya incorrectes");
                return "login";
            }
            
            if (user.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || 
                                      auth.getAuthority().equals("ROLE_AGENT"))) {
                return "redirect:/admin/clients";
            }
            
            return "redirect:/index";
        }catch(ClientNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "login";
        }catch(AgentNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    
    }
    
    @GetMapping("/public/login")
    public String paginaLogin(){
        return "login";
    }
}
