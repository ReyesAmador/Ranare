/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Incidencia;
import cat.copernic.ranare.service.mongodb.DocumentService;
import cat.copernic.ranare.service.mysql.IncidenciaService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ngall
 */
@Controller
@RequestMapping("/vehicles/incidencies")
public class IncidenciaController {
    
    @Autowired
    private IncidenciaService incidenciaService;
    
    @Autowired
    private DocumentService documentService;
    
    @GetMapping
    public String listIncidencies(Model model){
        List<Incidencia> incidencies = incidenciaService.findAll();
        model.addAttribute("incidencies", incidencies);
        return "incidencies/llista-incidencies";
    }
    
    @GetMapping("/nou")
    public String novaIncidencia(Model model){
        model.addAttribute("incidencia", new Incidencia());
        return "incidencies/crear-incidencia";
    }
    
    @PostMapping
    public String guardarIncidencia(@ModelAttribute Incidencia incidencia, 
            @RequestParam("documents") MultipartFile file) throws IOException{
        
        if(!file.isEmpty()){
            String documentId = documentService.saveDocument(file);
            incidencia.setDocumentsIncidenciaId(documentId);
        }
        incidenciaService.save(incidencia);
        return "redirect:/incidencies";
    }
    
    @GetMapping("/modificar/{id}")
    public String modificarIncidencia(@PathVariable Long id, Model model){
        model.addAttribute("incidencia", incidenciaService.findById(id));
        return "incidencies/crear-incidencia";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarIncidencia(@PathVariable Long id){
        incidenciaService.deleteById(id);
        return "redirect:/incidencies";
    }
}
