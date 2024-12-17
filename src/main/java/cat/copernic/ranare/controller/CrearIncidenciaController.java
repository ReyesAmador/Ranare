/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Incidencia;
import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.repository.mongodb.ImatgesIncidenciaRepository;
import cat.copernic.ranare.service.mysql.IncidenciaService;
import cat.copernic.ranare.service.mysql.VehicleService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ngall
 */
@Controller
@RequestMapping("/admin/vehicles")
public class CrearIncidenciaController {
    
    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private ImatgesIncidenciaRepository imatgesIncidenciaRepository;

    // Método para mostrar el formulario de crear incidencia
    @GetMapping("/crear-incidencia")
    public String mostrarFormulari(@RequestParam(name = "matricula", required = false)String matricula,
                                   @RequestParam(name = "idIncidencia", required = false) Long idIncidencia,
                                   Model model) {
        Incidencia incidencia = new Incidencia();
        
        if(idIncidencia != null){
            incidencia = incidenciaService.findById(idIncidencia);
        }else if(matricula != null && !matricula.isEmpty()){
            Vehicle vehicle = vehicleService.getVehicleByMatricula(matricula);
            incidencia.setVehicle(vehicle);
        }
        
        model.addAttribute("incidencia", incidencia); // Crear un objeto vacío de incidencia para el formulario
        model.addAttribute("title", "Crear indicència");
        model.addAttribute("content", "crear-incidencia :: crearIncidenciaContent");
        
        return "admin"; // Retorna la plantilla HTML
    }
    
    // Método para manejar el formulario de creación de incidencia
    @PostMapping("/crear-incidencia")
    public String createOrUpdateIncidencia(@Valid Incidencia incidencia, 
                                           BindingResult result,
                                           @RequestParam("imatges") MultipartFile[] imatges,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        
        if(result.hasErrors()) {
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            model.addAttribute("title", "Crear indicència");
            model.addAttribute("content", "crear-incidencia :: crearIncidenciaContent");
            return "admin";
        }
        
        List<String> imatgesIds = new ArrayList<>();
        if(imatges != null && imatges.length > 0){
            try{
                for(MultipartFile imatge : imatges){
                    String imatgeId = imatgesIncidenciaRepository.storeImage(imatge);
                    imatgesIds.add(imatgeId);
                }
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Error al guardar las imágenes: " + e.getMessage());
                return "redirect:/admin/vehicles/crear-incidencia";
            }
        }
        
        if(incidencia.getIdIncidencia() != null){
            Incidencia existingIncidencia = incidenciaService.findById(incidencia.getIdIncidencia());
            if(existingIncidencia != null){
                existingIncidencia.setCulpabilitat(incidencia.isCulpabilitat());
                existingIncidencia.setEstat(incidencia.getEstat());
                existingIncidencia.setMotiu(incidencia.getMotiu());
                existingIncidencia.setDataInici(incidencia.getDataInici());
                existingIncidencia.setDataFinal(incidencia.getDataFinal());
                existingIncidencia.setCost(incidencia.getCost());
                existingIncidencia.setDocumentsIncidenciaId(incidencia.getDocumentsIncidenciaId());
                existingIncidencia.setVehicle(incidencia.getVehicle());
                existingIncidencia.setImatgesIncidenciesIDs(imatgesIds);
                
                incidenciaService.save(existingIncidencia);
                redirectAttributes.addFlashAttribute("message","La incidència s'ha actualitzat correctament.");          
            }else{
                redirectAttributes.addFlashAttribute("error","Error: La incidència no existeix.");
            }
        }else{
            incidencia.setImatgesIncidenciesIDs(imatgesIds);
            incidenciaService.save(incidencia); 
            redirectAttributes.addFlashAttribute("message", "La incidència s'ha creat correctament.");
        }
        return "redirect:/admin/vehicles/crear-incidencia";  
    }
}