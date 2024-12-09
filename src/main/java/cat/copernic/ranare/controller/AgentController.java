/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Agent;
import cat.copernic.ranare.exceptions.AgentNotFoundException;
import cat.copernic.ranare.exceptions.DuplicateResourceException;
import cat.copernic.ranare.service.mysql.AgentService;
import cat.copernic.ranare.service.mysql.LocalitzacioService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @Autowired
    private LocalitzacioService localitzacioService;
    
    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);
    
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
        model.addAttribute("modificar", false);
        return "crear-agent";
    }

    // Crear un  agente con un rol
    @PostMapping("/crear-agent")
    public String crearAgent(@ModelAttribute @Valid Agent agent, RedirectAttributes redirectAttributes){
        try{
        agentService.guardarAgent(agent); // Guardar el nuevo agente
        redirectAttributes.addFlashAttribute("success", "Agent creat correctament!");
        }catch(DuplicateResourceException e){
            redirectAttributes.addFlashAttribute("error", "Error al crear l'agent: " + e.getMessage());
        }
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
    
    //Modificar agent
    @GetMapping("/{dni}/modificar")
    public String showFormulariModificarAgent(@PathVariable String dni, Model model){
        Optional<Agent> agentExisteix = agentService.findAgentByDni(dni);
        if(agentExisteix.isPresent()){
            Agent agent = agentExisteix.get();
            logger.info("Fecha de nacimiento del agente: {}", agent.getDataNaixement());
            model.addAttribute("agent", agent);
            model.addAttribute("localitzacions", localitzacioService.getallLocalitzacio());
            model.addAttribute("modificar", true);
            
            return "crear-agent";
        }else{
            throw new AgentNotFoundException("Agent amb DNI " + dni + " no trobat");
        }
    }
    
    @PostMapping("{dni}/modificar")
    public String modificarAgent(@ModelAttribute("agent") Agent agent, RedirectAttributes redirectAttributes){
        try{
            agentService.modificarAgent(agent);
            redirectAttributes.addFlashAttribute("success", "Agent modificat correctament.");
        }catch(AgentNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/agents";
    }
    
    @GetMapping("/{dni}")
    public String detallAgent (@PathVariable String dni, Model model){
        try{
            Agent agent = agentService.getAgentPerDni(dni);
            model.addAttribute("agent",agent);
            
            return "detall-agent";
        }catch(AgentNotFoundException e){
            model.addAttribute("errorMissatge", e.getMessage());
            return "error";
        }
    }
}
