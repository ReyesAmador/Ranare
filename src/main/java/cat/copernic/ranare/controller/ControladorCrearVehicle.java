/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.service.mysql.VehicleService;
import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.validation.Valid;
import java.io.IOException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author ngall
 */
@Controller
public class ControladorCrearVehicle {
    
    @Autowired
    VehicleService vehicleService;
    
    @Autowired
    GridFsTemplate gridFsTemplate;
    
    @GetMapping("/crear-vehicle")
    public String mostrarFormulari(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        return "crear-vehicle";  
    }
    
    @PostMapping("/crear-vehicle")
    public String crearVehicle(@Valid Vehicle vehicle, BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "crear-vehicle";
        }
        
        vehicleService.saveVehicle(vehicle);
        
        redirectAttributes.addFlashAttribute("message", "El vehicle s'ha creat correctament.");
        return "redirect:/crear-vehicle";
    }
    
    @PostMapping("/pujarImatge")
    public String pujarImatge(@RequestParam("image") MultipartFile file, Model model) throws IOException{
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
       
        ObjectId fileId = gridFsTemplate.store(file.getInputStream(), fileName, file.getContentType());
        
        model.addAttribute("message", "Imatge penjada correctament!");
        return "crear-vehicle";
    }
}
