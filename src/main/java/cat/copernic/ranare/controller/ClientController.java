/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.service.mysql.ClientService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public String showForm(Model model) {
        model.addAttribute("client", new Client());
        return "crear_client"; // Plantilla Thymeleaf para el formulario de creación
    }

    @PostMapping("/crear_client")
    public String createClient(@ModelAttribute @Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "crear_client"; // Si hay errores, vuelve al formulario
        }
        clientService.saveClient(client);  // Guardar el cliente en la base de datos
      return "redirect:/clients";   // Redirigir a la lista de clientes
    }
}
