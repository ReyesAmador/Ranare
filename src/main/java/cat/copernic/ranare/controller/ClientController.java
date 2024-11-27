/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.enums.Rol;
import cat.copernic.ranare.service.mysql.AgentService;
import cat.copernic.ranare.service.mysql.ClientService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador per gestionar les operacions relacionades amb els clients.
 * 
 * Aquest controlador permet la creació, modificació, eliminació i consulta dels clients.
 *
 * @author Raú
 */
@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;
    
    @Autowired
    private AgentService agentService;

    /**
     * Crear o actualitzar un client a través d'una API REST.
     *
     * @param client El client a crear o actualitzar.
     * @return La resposta amb el client creat o actualitzat.
     */
    @PostMapping("/api")
    public ResponseEntity<Client> createOrUpdateClient(@RequestBody Client client) {
        Client savedClient = clientService.saveClient(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    /**
     * Obtenir un client pel seu DNI a través d'una API REST.
     *
     * @param dni El DNI del client que volem obtenir.
     * @return La resposta amb el client si existeix, o un error 404 si no.
     */
    @GetMapping("/api/{dni}")
    public ResponseEntity<Client> getClientById(@PathVariable String dni) {
        Optional<Client> client = clientService.getClientById(dni);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtenir tots els clients a través d'una API REST.
     *
     * @return La llista de tots els clients.
     */
    @GetMapping("/api")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    /**
     * Eliminar un client pel seu DNI.
     *
     * @param dni El DNI del client que volem eliminar.
     * @return Una redirecció a la llista de clients després d'eliminar el client.
     */
    @DeleteMapping("/{dni}")
    public String deleteClient(@PathVariable String dni, RedirectAttributes redirectAttributes) {
        clientService.deleteClient(dni);
        redirectAttributes.addFlashAttribute("successMessage", "El client s'ha eliminat correctament.");
        return "redirect:/clients";
    }

    /**
     * Mostrar la llista de clients en una vista HTML.
     *
     * @param model El model per passar la llista de clients a la vista.
     * @return El nom de la plantilla Thymeleaf per mostrar la llista de clients.
     */
    @GetMapping
    public String showClientList(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "llista_de_clients"; // Plantilla Thymeleaf per mostrar la llista de clients
    }

    /**
     * Mostrar el formulari per crear un client.
     *
     * @param model El model per passar un objecte de client buit a la vista.
     * @return El nom de la plantilla Thymeleaf per mostrar el formulari de creació.
     */
    @GetMapping("/crear_client")
    public String showForm(Model model, @AuthenticationPrincipal User loggedUser) {
        
        //verificar si es admin
        boolean isAdmin = loggedUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        
        model.addAttribute("client", new Client());
        model.addAttribute("rols", Rol.values()); // Añadir roles disponibles (AGENT, ADMIN)
        return "crear_client"; // Plantilla Thymeleaf para el formulario de creación
    }

    // Crear un cliente o agente con un rol (POST - Vistas HTML)
    @PostMapping("/crear_client")
    public String createClient(@ModelAttribute @Valid Client client, @RequestParam(required = false) Rol rol, BindingResult bindingResult
            ,RedirectAttributes redirectAttributes, @AuthenticationPrincipal User loggedUser) {
        if (bindingResult.hasErrors()) {
            return "crear_client";
        }
        
        if(loggedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")) && rol != null){
            // Si el rol es especificado (admin está creando un agente), creamos un agente
            agentService.crearAgent(client, rol); // Crear un agente
        redirectAttributes.addFlashAttribute("missatge", "Agent creat correctament.");
        }
        else{
            // Si no se especifica rol, se crea un cliente normal
            clientService.saveClient(client);  // Guardar el cliente normal en la base de datos
            redirectAttributes.addFlashAttribute("missatge", "Client creat correctament.");
        }
      return "redirect:/clients";   // Redirigir a la lista de clientes
    }

    /**
     * Mostrar el formulari per modificar un client.
     *
     * @param dni El DNI del client que es vol modificar.
     * @param model El model per passar les dades del client a la vista.
     * @return El nom de la plantilla Thymeleaf per editar el client, o la redirecció a la llista de clients si no es troba.
     */
    @GetMapping("/modificar/{dni}")
    public String showEditForm(@PathVariable String dni, Model model) {
        Optional<Client> clientOpt = clientService.getClientById(dni);
        if (clientOpt.isPresent()) {
            model.addAttribute("client", clientOpt.get());
            return "modificar_client"; // Plantilla Thymeleaf per editar un client
        }
        return "redirect:/clients"; // Redirigeix a la llista si no es troba el client
    }

    /**
     * Processar la modificació d'un client.
     *
     * @param dni El DNI del client que es vol modificar.
     * @param client Els nous valors del client a partir del formulari.
     * @param result Resultat de la validació del formulari.
     * @return La vista de llistat de clients si la validació és correcta, o el formulari d'edició en cas d'error.
     */
    @PostMapping("/modificar/{dni}")
    public String updateClient(@PathVariable String dni, @ModelAttribute @Valid Client client, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "modificar_client";
        }
        client.setDni(dni);
        clientService.saveClient(client);
        redirectAttributes.addFlashAttribute("successMessage", "El client s'ha modificat correctament.");
        return "redirect:/clients";
    }
}
