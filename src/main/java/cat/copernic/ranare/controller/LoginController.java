/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Agent;
import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.enums.Rol;
import cat.copernic.ranare.service.mysql.AgentService;
import cat.copernic.ranare.service.mysql.ClientService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ClientService clientService;
    
    @Autowired
    private AgentService agentService;
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String pass, Model model){
        
        Optional<Client> clientExisteix = clientService.findByUsername(username);
        if(clientExisteix.isPresent() && clientService.comprovarCredencials(username, pass)){
            Client client = clientExisteix.get();
            if(!client.isActiu()){
                model.addAttribute("error", "El compte està desactivat");
                return "login";
            }
            
            return "redirect:/home";
        }
        
        Optional<Agent> agentExisteix = agentService.findByUsername(username);
        if(agentExisteix.isPresent() && agentService.comprovarCredencials(username, pass)){
            Agent agent = agentExisteix.get();
            if(!agent.isActiu()){
                model.addAttribute("error", "El compte està desactivat");
                return "login";
            }
            
            if(Rol.ADMIN.equals(agent.getRol())){
                return "redirect:/admin/agents";
            }else{
                return "redirect:/admin/vehicles";
            }
        }
        
        model.addAttribute("error", "Credencials incorrectes");
        return "login";
    }
    
    @GetMapping("/login")
    public String paginaLogin(){
        return "login";
    }
}
