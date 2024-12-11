/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Agent;
import cat.copernic.ranare.enums.Rol;
import cat.copernic.ranare.exceptions.AgentNotFoundException;
import cat.copernic.ranare.exceptions.DuplicateResourceException;
import cat.copernic.ranare.service.mysql.AgentService;
import cat.copernic.ranare.service.mysql.ClientService;
import cat.copernic.ranare.service.mysql.LocalitzacioService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    
    @Autowired
    private ClientService clientService;
    
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
    public String crearAgent(@ModelAttribute @Valid Agent agent,BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        try{
            //validar que el username està o no en ús
            if(agentService.existeixUsername(agent.getUsername())){
                bindingResult.rejectValue("username", "duplicate.username");
            }
            //validar que el email està o no en ús
            if(agentService.existeixEmail(agent.getEmail())){
                bindingResult.rejectValue("email", "duplicate.email");
            }
                        
            if (clientService.existeixUsername(agent.getUsername())) {
                bindingResult.rejectValue("username", "duplicate.username");
            }
            
            if (clientService.existeixEmail(agent.getEmail())) {
                bindingResult.rejectValue("email", "duplicate.email");
            }
            //si hi ha un error de validació et retorna a crear agent
            if(bindingResult.hasErrors()){
                model.addAttribute("agent", agent);
                model.addAttribute("modificar", false);
                return "crear-agent";
            }
            agentService.crearAgent(agent, agent.getRol()); // Guardar el nuevo agente
            redirectAttributes.addFlashAttribute("success", "Agent creat correctament!");
        }catch(DuplicateResourceException e){
            model.addAttribute("agent", agent);
            model.addAttribute("modificar", false);
            model.addAttribute("error", "Error al crear l'agent: " + e.getMessage());
            return "crear-agent";
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
    public String modificarAgent(@ModelAttribute("agent") Agent agent,BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        try{
            Agent agentExistent = agentService.getAgentPerDni(agent.getDni());
            //validar que el username està o no en ús tan sols si ha canviat el valor
            if(!agent.getUsername().equals(agentExistent.getUsername()) && agentService.existeixUsername(agent.getUsername())){
                bindingResult.rejectValue("username", "duplicate.username");
            }
            //validar que el email està o no en ús tan sols si ha canviat el valor
            if(!agent.getEmail().equals(agentExistent.getEmail()) && agentService.existeixEmail(agent.getEmail())){
                bindingResult.rejectValue("email", "duplicate.email");
            }
            
            if (!agent.getUsername().equals(agentExistent.getUsername()) && clientService.existeixUsername(agent.getUsername())) {
                bindingResult.rejectValue("username", "duplicate.username");
            }
            
            if (!agent.getEmail().equals(agentExistent.getEmail()) && clientService.existeixEmail(agent.getEmail())) {
                bindingResult.rejectValue("email", "duplicate.email");
            }
            //si hi ha un error de validació et retorna a crear agent
            if(bindingResult.hasErrors()){
                model.addAttribute("agent", agent);
                model.addAttribute("modificar", true);
                return "crear-agent";
            }
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
    
    @GetMapping("/buscar-agent")
    public String trobarAgent(@RequestParam("dni") String dni, Model model){
        Optional<Agent> agent = agentService.findAgentByDni(dni);
        
        if(agent.isPresent()){
            model.addAttribute("agents", List.of(agent.get()));
        }else{
            model.addAttribute("error", "Agent no trobat amb el DNI " + dni);
            model.addAttribute("agents", List.of());
        }
        
        return "llista-agents";
    }
}
