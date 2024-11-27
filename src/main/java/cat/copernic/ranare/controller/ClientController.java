/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.enums.Rol;
import cat.copernic.ranare.service.mysql.ClientService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
 * Aquest controlador inclou operacions per crear, modificar, eliminar i
 * consultar clients tant en vistes HTML com en API REST.
 *
 * També afegeix suport per a la creació d'agents per part d'usuaris administradors.
 *
 * @author Raú
 */
@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AgentService agentService; // **Nou: Suport per gestionar agents.**

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
        redirectAttributes.addFlashAttribute("missatge", "El client s'ha eliminat correctament.");
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
     * Inclou comprovació del rol de l'usuari autenticat per determinar si pot crear agents.
     *
     * @param model El model per passar un client buit a la vista.
     * @param loggedUser L'usuari autenticat per comprovar permisos.
     * @return El nom de la plantilla Thymeleaf per crear un client.
     */
    @GetMapping("/crear_client")
    public String showForm(Model model, @AuthenticationPrincipal User loggedUser) {
        boolean isAdmin = loggedUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")); // **Nou: Comprovació de rol.**
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("client", new Client());
        model.addAttribute("rols", Rol.values()); // **Nou: Passa els rols disponibles (AGENT, ADMIN).**
        return "crear_client";
    }

    /**
     * Processa el formulari de creació de clients o agents.
     *
     * Admins poden especificar un rol per crear agents, mentre que usuaris sense permisos només poden crear clients.
     *
     * @param client El client o agent creat a partir del formulari.
     * @param rol El rol seleccionat si es tracta d'un agent.
     * @param bindingResult El resultat de la validació del formulari.
     * @param redirectAttributes Atributs de redirecció per passar missatges d'èxit.
     * @param loggedUser L'usuari autenticat per comprovar permisos.
     * @return La redirecció a la pàgina de llista de clients si és correcte, o el formulari si hi ha errors.
     */
    @PostMapping("/crear_client")
    public String createClient(@ModelAttribute @Valid Client client, @RequestParam(required = false) Rol rol, 
                               BindingResult bindingResult, RedirectAttributes redirectAttributes, 
                               @AuthenticationPrincipal User loggedUser) {
        if (bindingResult.hasErrors()) {
            return "crear_client";
        }
        if (loggedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")) && rol != null) {
            // **Nou: Creació d'un agent si l'usuari és administrador.**
            agentService.crearAgent(client, rol);
            redirectAttributes.addFlashAttribute("missatge", "Agent creat correctament.");
        } else {
            // Creació d'un client normal.
            clientService.saveClient(client);
            redirectAttributes.addFlashAttribute("missatge", "Client creat correctament.");
        }
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
    public String updateClient(@PathVariable String dni, @ModelAttribute @Valid Client client, BindingResult result, 
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "modificar_client";
        }
        client.setDni(dni);
        clientService.saveClient(client);
        redirectAttributes.addFlashAttribute("missatge", "El client s'ha modificat correctament.");
        return "redirect:/clients";
    }
}