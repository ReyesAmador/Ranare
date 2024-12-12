/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.service.mysql.AdminVehicleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ngall
 */

@Controller
public class ControladorAdminVehicle {
    
    @Autowired
    private AdminVehicleService adminVehicleService;
    
    @GetMapping("/admin/vehicles")
    public String mostrarVehicles(Model model){
        List<Vehicle> vehicles = adminVehicleService.getAllVehicles();
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("title", "Vehicles");
        model.addAttribute("content", "admin-vehicles :: vehicleContent");
        return "admin";
    }
    
    @GetMapping("/admin/seleccionarVehicle")
    public String seleccionarVehicle(@RequestParam(name = "matricula", required = false) 
            String matricula, RedirectAttributes redirectAttributes){
        if(matricula != null && !matricula.isEmpty()){
            return "redirect:/crear-vehicle?matricula=" + matricula;
        }else{
            return "redirect:/crear-vehicle";
        }
    }
   
    @PostMapping("/admin/eliminarVehicle")
    public String eliminarVehicle(@RequestParam(name = "matriculas", required = false)
            List<String> matriculas, RedirectAttributes redirectAttributes){
        if(matriculas != null && !matriculas.isEmpty()){
            adminVehicleService.deleteVehiclesByIds(matriculas);
            redirectAttributes.addFlashAttribute("message", "Vehicle eliminat correctament.");
        }else{
            redirectAttributes.addFlashAttribute("errorMessage","No s'ha seleccionat cap vehicle per eliminar.");
        }
        return "redirect:/admin/vehicles";
    } 
}