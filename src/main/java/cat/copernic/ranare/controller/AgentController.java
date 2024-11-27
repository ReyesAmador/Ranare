/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Agent;
import cat.copernic.ranare.service.mysql.AgentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author reyes
 */
@Controller
@RequestMapping("/agents")
public class AgentController {
    
    @Autowired
    private AgentService agentService;
    
    
    @GetMapping
    public String llistarAgents(Model model){
        model.addAttribute("agents", agentService.getAllAgents());
        
        return "llista-agents";
    }
    
    /**
     * Mostrar el formulari per crear un client.
     *
     * @param model El model per passar un objecte de client buit a la vista.
     * @return El nom de la plantilla Thymeleaf per mostrar el formulari de creació.
     */
    @GetMapping("/crear-agent")
    public String showForm(Model model) {
        model.addAttribute("agent", new Agent()); // Modelo para el formulario
        return "crear-agent";
    }

    // Crear un  agente con un rol
    @PostMapping("/crear-agent")
    public String createAgent(@ModelAttribute @Valid Agent agent, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "crear_agent"; // Volver al formulario si hay errores
        }
        
        agentService.guardarAgent(agent); // Guardar el nuevo agente
        return "redirect:/agents"; // Redirigir a la lista de agentes
    }
}
