/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.VehicleDto2;
import cat.copernic.ranare.service.mysql.VehicleService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 * @author reyes
 */
@Controller
public class VehicleClientController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @GetMapping("/public/vehicles/disponibles")
    public String filtrarVehiclesDisponibilitat(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInici,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal, Model model){
        List<VehicleDto2> vehiclesDisponibles = (List<VehicleDto2>)vehicleService.filtrarVehiculosDisponiblesDTO(dataInici, dataFinal, true);
        
        model.addAttribute("content", "llista-vehicles-disponibles :: vehiclesClientContent");
        model.addAttribute("vehiclesDisponibles", vehiclesDisponibles);
        
        return "base-public";
    }
}
