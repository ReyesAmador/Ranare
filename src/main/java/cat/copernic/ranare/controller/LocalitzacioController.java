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
    LocalitzacioService localitzacioService;
    
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
        model.addAttribute("localitzacio", new Localitzacio());
        
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
        localitzacioService.saveLocalitzacio(local);
        
        // Afegeix un missatge de confirmació utilitzant flash attributes.
        redirectAttributes.addFlashAttribute("success", "Localització afegida correctament!");
        
        return "redirect:/localitzacio";
        
        }catch(InvalidCodiPostalException e){
            // Afegeix un missatge d'error al model en cas d'excepció.
            model.addAttribute("error", e.getMessage());
            return "crear-localitzacio";
        }
    }
}
