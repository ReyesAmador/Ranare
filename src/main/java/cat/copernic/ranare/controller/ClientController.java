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
 * Controlador per gestionar les operacions relacionades amb els clients.
 *
 * Aquest controlador inclou operacions per crear, modificar, eliminar i 
 * consultar clients tant en vistes HTML com en API REST.
 *
 * @author Raú
 */
@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * Crea o actualitza un client a través d'una API REST.
     *
     * @param client El client que es vol crear o actualitzar.
     * @return La resposta amb el client creat o actualitzat.
     */
    @PostMapping("/api")
    public ResponseEntity<Client> createOrUpdateClient(@RequestBody Client client) {
        Client savedClient = clientService.saveClient(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    /**
     * Obté un client pel seu DNI a través d'una API REST.
     *
     * @param dni El DNI del client que es vol obtenir.
     * @return La resposta amb el client si existeix, o un error 404 si no es troba.
     */
    @GetMapping("/api/{dni}")
    public ResponseEntity<Client> getClientById(@PathVariable String dni) {
        Optional<Client> client = clientService.getClientById(dni);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obté tots els clients a través d'una API REST.
     *
     * @return La llista de tots els clients disponibles.
     */
    @GetMapping("/api")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    /**
     * Elimina un client pel seu DNI.
     *
     * @param dni El DNI del client que es vol eliminar.
     * @param redirectAttributes Atributs de redirecció per passar missatges d'èxit.
     * @return La redirecció a la pàgina de llista de clients.
     */
    @DeleteMapping("/{dni}")
    public String deleteClient(@PathVariable String dni, RedirectAttributes redirectAttributes) {
        clientService.deleteClient(dni);
        redirectAttributes.addFlashAttribute("successMessage", "El client s'ha eliminat correctament.");
        return "redirect:/clients";
    }

    /**
     * Mostra la llista de clients en una vista HTML.
     *
     * @param model El model per passar dades a la vista.
     * @return El nom de la plantilla Thymeleaf per mostrar la llista de clients.
     */
    @GetMapping
    public String showClientList(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "llista_de_clients";
    }

    /**
     * Mostra el formulari per crear un nou client.
     *
     * @param model El model per passar un client buit a la vista.
     * @return El nom de la plantilla Thymeleaf per crear un client.
     */
    @GetMapping("/crear_client")
    public String showForm(Model model) {
        model.addAttribute("client", new Client());
        return "crear_client";
    }

    /**
     * Processa el formulari de creació de clients.
     *
     * @param client El client creat a partir del formulari.
     * @param bindingResult El resultat de la validació del formulari.
     * @param redirectAttributes Atributs de redirecció per passar missatges d'èxit.
     * @return La redirecció a la pàgina de llista de clients si és correcte, o el formulari si hi ha errors.
     */
    @PostMapping("/crear_client")
    public String createClient(@ModelAttribute @Valid Client client, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "crear_client";
        }
        clientService.saveClient(client);
        redirectAttributes.addFlashAttribute("successMessage", "El client s'ha creat correctament.");
        return "redirect:/clients";
    }

    /**
     * Mostra el formulari per modificar un client existent.
     *
     * @param dni El DNI del client que es vol modificar.
     * @param model El model per passar les dades del client a la vista.
     * @return El nom de la plantilla Thymeleaf per modificar el client, o la redirecció a la llista de clients si no es troba.
     */
    @GetMapping("/modificar/{dni}")
    public String showEditForm(@PathVariable String dni, Model model) {
        Optional<Client> clientOpt = clientService.getClientById(dni);
        if (clientOpt.isPresent()) {
            model.addAttribute("client", clientOpt.get());
            return "modificar_client";
        }
        return "redirect:/clients";
    }

    /**
     * Processa la modificació d'un client.
     *
     * @param dni El DNI del client que es vol modificar.
     * @param client Les noves dades del client.
     * @param result El resultat de la validació del formulari.
     * @param redirectAttributes Atributs de redirecció per passar missatges d'èxit.
     * @return La redirecció a la pàgina de llista de clients si és correcte, o el formulari si hi ha errors.
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
