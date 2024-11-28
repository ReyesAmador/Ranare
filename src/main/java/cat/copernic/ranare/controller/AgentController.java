/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Agent;
import cat.copernic.ranare.exceptions.AgentNotFoundException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String crearAgent(@ModelAttribute @Valid Agent agent, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "crear_agent"; // Volver al formulario si hay errores
        }
        
        agentService.guardarAgent(agent); // Guardar el nuevo agente
        redirectAttributes.addFlashAttribute("success", "Agent creat correctament!");
        return "redirect:/agents"; // Redirigir a la lista de agentes
    }
    
    //Eliminar agent
    @PostMapping("/eliminar-agent")
    public String eliminarAgent(@RequestParam("dni") String dni, RedirectAttributes redirectAttributes){
        try{
            agentService.eliminarAgent(dni);
            redirectAttributes.addFlashAttribute("success", "Agent amb dni: " + dni +" eliminat correctament");
        }catch(AgentNotFoundException e){
            redirectAttributes.addFlashAttribute("error", "No s'ha pogut eliminar l'agent: " + e.getMessage());
        }
        
        return "redirect:/agents";
    }
}
