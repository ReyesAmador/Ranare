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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/api")
    public ResponseEntity<Client> createOrUpdateClient(@RequestBody Client client) {
        Client savedClient = clientService.saveClient(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    @GetMapping("/api/{dni}")
    public ResponseEntity<Client> getClientById(@PathVariable String dni) {
        Optional<Client> client = clientService.getClientById(dni);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/api")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @DeleteMapping("/{dni}")
    public String deleteClient(@PathVariable String dni, RedirectAttributes redirectAttributes) {
        clientService.deleteClient(dni);
        redirectAttributes.addFlashAttribute("successMessage", "El client s'ha eliminat correctament.");
        return "redirect:/clients";
    }

    @GetMapping
    public String showClientList(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "llista_de_clients";
    }

    @GetMapping("/crear_client")
    public String showForm(Model model) {
        model.addAttribute("client", new Client());
        return "crear_client";
    }

    @PostMapping("/crear_client")
    public String createClient(@ModelAttribute @Valid Client client, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "crear_client";
        }
        clientService.saveClient(client);
        redirectAttributes.addFlashAttribute("successMessage", "El client s'ha creat correctament.");
        return "redirect:/clients";
    }

    @GetMapping("/modificar/{dni}")
    public String showEditForm(@PathVariable String dni, Model model) {
        Optional<Client> clientOpt = clientService.getClientById(dni);
        if (clientOpt.isPresent()) {
            model.addAttribute("client", clientOpt.get());
            return "modificar_client";
        }
        return "redirect:/clients";
    }

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
