/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Incidencia;
import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.service.mysql.IncidenciaService;
import cat.copernic.ranare.service.mysql.VehicleService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ngall
 */
@Controller
public class CrearIncidenciaController {
    
    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private VehicleService vehicleService;

    // Método para mostrar el formulario de crear incidencia
    @GetMapping("/crear-incidencia")
    public String mostrarFormulari(Model model) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles(); // Obtener lista de vehículos disponibles
        
        model.addAttribute("vehicles", vehicles); // Añadir vehículos al modelo
        model.addAttribute("incidencia", new Incidencia()); // Crear un objeto vacío de incidencia para el formulario
        
        return "crear-incidencia"; // Retorna la plantilla HTML
    }
    
    // Método para manejar el formulario de creación de incidencia
    @PostMapping("/crear-incidencia")
    public String createIncidencia(@Valid Incidencia incidencia,BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        
        if(result.hasErrors()) {
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "crear-incidencia";
        }
        incidenciaService.save(incidencia);
        
        redirectAttributes.addFlashAttribute("message", "La incidència s'ha creat correctament.");
        return "redirect:/crear-incidencia";
        
    }
}