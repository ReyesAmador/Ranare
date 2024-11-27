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

/**
 * Controlador para gestionar las operaciones relacionadas con los clientes.
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

    // Crear o actualizar un cliente (API REST) -> Mantén esta ruta para las peticiones API
    @PostMapping("/api")
    public ResponseEntity<Client> createOrUpdateClient(@RequestBody Client client) {
        Client savedClient = clientService.saveClient(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    // Leer un cliente por su DNI (API REST)
    @GetMapping("/api/{dni}")
    public ResponseEntity<Client> getClientById(@PathVariable String dni) {
        Optional<Client> client = clientService.getClientById(dni);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Leer todos los clientes (API REST)
    @GetMapping("/api")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    // Eliminar un cliente por su DNI (API REST)
    @DeleteMapping("/api/{dni}")
    public ResponseEntity<Void> deleteClient(@PathVariable String dni) {
        clientService.deleteClient(dni);
        return ResponseEntity.noContent().build();
    }

    // Mostrar lista de clientes (Vistas HTML)
    @GetMapping
    public String showClientList(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "llista_de_clients"; // Plantilla Thymeleaf para listar clientes
    }

    // Mostrar el formulario para crear cliente (Vistas HTML)
    @GetMapping("/crear_client")
    public String showForm(Model model, Authentication auth) {
        
        //verificar si es admin
        boolean isAdmin = auth.getAuthorities().stream()
                                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        
        model.addAttribute("client", new Client());
        model.addAttribute("rols", Rol.values()); // Añadir roles disponibles (AGENT, ADMIN)
        model.addAttribute("isAdmin", isAdmin); // Pasamos si el usuario es admin al modelo
        return "crear_client"; // Plantilla Thymeleaf para el formulario de creación
    }

    // Crear un cliente o agente con un rol (POST - Vistas HTML)
    @PostMapping("/crear_client")
    public String createClient(@ModelAttribute @Valid Client client, @RequestParam(required = false) Rol rol, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rols", Rol.values()); // Re-incluir los roles disponibles
            return "crear_client"; // Si hay errores, vuelve al formulario
        }
        
        if(rol != null)
            // Si el rol es especificado (admin está creando un agente), creamos un agente
            agentService.crearAgent(client, rol); // Crear un agente
        else
            // Si no se especifica rol, se crea un cliente normal
            clientService.saveClient(client);  // Guardar el cliente normal en la base de datos
      return "redirect:/clients";   // Redirigir a la lista de clientes
    }
}
