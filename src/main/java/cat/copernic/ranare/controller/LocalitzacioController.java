/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Agent;
import cat.copernic.ranare.entity.mysql.Localitzacio;
import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.exceptions.InvalidCodiPostalException;
import cat.copernic.ranare.exceptions.InvalidHorariException;
import cat.copernic.ranare.service.mysql.AgentService;
import cat.copernic.ranare.service.mysql.LocalitzacioService;
import java.util.List;
import java.util.Set;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * Controlador per gestionar les operacions relacionades amb les localitzacions.
 * Aquest controlador permet visualitzar totes les localitzacions, accedir al formulari per crear-ne una nova
 * i processar el formulari per afegir una localització.
 *
 * @author reyes
 * @version 22/11/2024 v2
 */

@Controller
@RequestMapping("/localitzacio")
public class LocalitzacioController {
    
    /**
     * Servei de localitzacions utilitzat per realitzar operacions sobre les dades de localitzacions.
     */
    @Autowired  
    private LocalitzacioService localitzacioService;
    
    @Autowired
    private AgentService agentService;
    
    /**
     * Mostra una llista amb totes les localitzacions disponibles.
     *
     * @param model Model utilitzat per afegir dades que es passaran a la vista.
     * @return El nom de la vista que mostra les localitzacions.
     */
    @GetMapping
    public String mostrarLocalitzacions(Model model) {
        List<Localitzacio> localitzacions = localitzacioService.getallLocalitzacio();
        model.addAttribute("localitzacions", localitzacions);
        return "localitzacio"; 
    }
    
    /**
     * Mostra el formulari per crear una nova localització.
     *
     * @param model Model utilitzat per passar una instància buida de {@link Localitzacio} a la vista.
     * @return El nom de la vista del formulari per crear una localització.
     */
    @GetMapping("/crear-localitzacio")
    public String showCrearLocalitzacioPage(Model model){
        
        Logger logger = LoggerFactory.getLogger(ClientController.class);

        List<Agent> agents = agentService.getAllAgents();

        // Depuración: registra la lista de agentes en el log
        agents.forEach(agent -> logger.info("Agent: {}, DNI: {}", agent.getNom(), agent.getDni()));
        
        model.addAttribute("localitzacio", new Localitzacio());
        model.addAttribute("agents", agentService.getAllAgents());
        model.addAttribute("crear", true);//passem aquesta variable per indivar al HTML que es per crear i mostri "crear"
        
        return "crear-localitzacio";
    }
    
    /**
     * Processa el formulari per crear una nova localització.
     *
     * @param local L'objecte {@link Localitzacio} omplert amb les dades introduïdes al formulari.
     * @param redirectAttributes Objecte per passar missatges entre peticions HTTP amb redireccions.
     * @param model Model utilitzat per passar missatges d'error a la vista en cas d'excepció.
     * @return Una redirecció a la pàgina de llista de localitzacions si es crea correctament, 
     * o el nom de la vista del formulari si hi ha errors.
     */
    
    @PostMapping("/crear-localitzacio")
    public String createLocalitzacio(@ModelAttribute Localitzacio local, RedirectAttributes redirectAttributes, Model model){
        
        try{
            //validar horaris
            localitzacioService.validarHorari(local.getHorariApertura(), local.getHorariTancament());
            localitzacioService.saveLocalitzacio(local);

            // Afegeix un missatge de confirmació utilitzant flash attributes.
            redirectAttributes.addFlashAttribute("success", "Localització afegida correctament!");

            return "redirect:/localitzacio";
        
        }catch(InvalidCodiPostalException e){
            // Afegeix un missatge d'error al model en cas d'excepció.
            model.addAttribute("error_codi", e.getMessage());
            model.addAttribute("error", "Hi ha un error");
            return "crear-localitzacio";
        }catch(InvalidHorariException e){
            model.addAttribute("error_horari", e.getMessage());
            model.addAttribute("error", "Hi ha un error");
            return "crear-localitzacio";
        }
    }
    
    @GetMapping("/{codiPostal}/vehicles")
    public String mostrarVehicles(@PathVariable String codiPostal, Model model){
        try{
            Set<Vehicle> vehicles = localitzacioService.getVehiclePerLocalitzacio(codiPostal);
            model.addAttribute("vehicles",vehicles);

            return "admin-vehicles";
        }catch(InvalidCodiPostalException e){
            model.addAttribute("errorMissatge", e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/{codiPostal}")
    public String detallLocalitzacio(@PathVariable String codiPostal, Model model){
        try{
            Localitzacio localitzacio = localitzacioService.getLocalitzacioPerCodiPostal(codiPostal);
            model.addAttribute("localitzacio",localitzacio);

            return "detall-localitzacio";
        }catch(InvalidCodiPostalException e){
            model.addAttribute("errorMissatge", e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/{codiPostal}/modificar")
    public String mostrarModificarLocalitzacio(@PathVariable String codiPostal, Model model){
        Localitzacio localitzacio = localitzacioService.getLocalitzacioPerCodiPostal(codiPostal);
        model.addAttribute("localitzacio",localitzacio);
        model.addAttribute("agents", agentService.getAllAgents());
        model.addAttribute("crear", false);
        
        return "crear-localitzacio";
    }
    
    @PostMapping("/{codiPostal}/modificar")
    public String modificarLocalitzacio(@PathVariable String codiPostal,@ModelAttribute Localitzacio localitzacio, RedirectAttributes redirectAttributes, Model model){
        try{
            localitzacioService.validarHorari(localitzacio.getHorariApertura(), localitzacio.getHorariTancament());
            localitzacioService.updateLocalitzacio(localitzacio);
            redirectAttributes.addFlashAttribute("success", "Localització modificada correctament!");
            return "redirect:/localitzacio";
        }catch(InvalidCodiPostalException e){
           model.addAttribute("error_codi", e.getMessage());
            model.addAttribute("error", "Hi ha un error");
            return "crear-localitzacio";
        }catch(InvalidHorariException e){
            model.addAttribute("error_horari", e.getMessage());
            model.addAttribute("error", "Hi ha un error");
            return "crear-localitzacio";
        }
    }
    
}
