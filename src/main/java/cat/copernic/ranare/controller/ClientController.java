/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.enums.Rol;
import cat.copernic.ranare.service.mysql.AgentService;
import cat.copernic.ranare.service.mysql.ClientService;
import exceptions.DuplicateResourceException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
 * Aquest controlador inclou operacions per crear, modificar, eliminar i
 * consultar clients tant en vistes HTML com en API REST.
 *
 * També afegeix suport per a la creació d'agents per part d'usuaris
 * administradors.
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
     * Processa el formulari de creació de clients o agents. Gestiona errors de
     * duplicació.
     *
     * @param client El client creat a partir del formulari.
     * @param bindingResult El resultat de la validació del formulari.
     * @param redirectAttributes Atributs de redirecció per passar missatges
     * d'èxit.
     * @return La redirecció a la pàgina de llista de clients si és correcte, o
     * el formulari si hi ha errors.
     */
    @PostMapping("/api")
    public ResponseEntity<Client> createOrUpdateClient(@RequestBody Client client) {
        Client savedClient = null;

        try {
            savedClient = clientService.saveClient(client, true); // Llamada al servicio para guardar el cliente
        } catch (DuplicateResourceException e) {
            // Manejo de la excepción: retorna un error 400 (Bad Request) con el mensaje
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Puedes enviar el mensaje en la respuesta si lo deseas
        }

        // Si el cliente se guarda correctamente, retorna el cliente con un código 201 (CREATED)
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
     * @return Una redirecció a la llista de clients després d'eliminar el
     * client.
     */
    @DeleteMapping("/{dni}")
    public String deleteClient(@PathVariable String dni, RedirectAttributes redirectAttributes) {
        clientService.deleteClient(dni);
        redirectAttributes.addFlashAttribute("missatge", "El client s'ha eliminat correctament.");
        return "redirect:/clients";
    }

    /**
     * Mostrar la llista de clients en una vista HTML.
     *
     * @param model El model per passar la llista de clients a la vista.
     * @return El nom de la plantilla Thymeleaf per mostrar la llista de
     * clients.
     */
    @GetMapping
    public String showClientList(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "llista_de_clients"; // Plantilla Thymeleaf per mostrar la llista de clients
    }

    /**
     * Mostra el formulari per crear un nou client.
     *
     * Inclou comprovació del rol de l'usuari autenticat per determinar si pot
     * crear agents.
     *
     * @param model El model per passar un client buit a la vista.
     * @param loggedUser L'usuari autenticat per comprovar permisos.
     * @return El nom de la plantilla Thymeleaf per crear un client.
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

    /**
     * Processa el formulari de creació de clients o agents.
     *
     * Admins poden especificar un rol per crear agents, mentre que usuaris
     * sense permisos només poden crear clients.
     *
     * @param client El client o agent creat a partir del formulari.
     * @param rol El rol seleccionat si es tracta d'un agent.
     * @param bindingResult El resultat de la validació del formulari.
     * @param redirectAttributes Atributs de redirecció per passar missatges
     * d'èxit.
     * @param loggedUser L'usuari autenticat per comprovar permisos.
     * @return La redirecció a la pàgina de llista de clients si és correcte, o
     * el formulari si hi ha errors.
     */
    @PostMapping("/crear_client")
    public String createClient(@ModelAttribute @Valid Client client,
            @RequestParam(required = false) Rol rol,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal User loggedUser) {
        if (bindingResult.hasErrors()) {
            return "crear_client"; // Retorna al formulari si hi ha errors de validació
        }

        try {
            if (loggedUser.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")) && rol != null) {
                // Si és admin i es defineix un rol, crear un agent
                agentService.crearAgent(client, rol);
                redirectAttributes.addFlashAttribute("missatge", "Agent creat correctament.");
            } else {
                // Crear client normal
                clientService.saveClient(client, false);
                redirectAttributes.addFlashAttribute("missatge", "Client creat correctament.");
            }
        } catch (DuplicateResourceException e) {
            bindingResult.reject("dni", e.getMessage());
            return "crear_client"; // Retorna al formulari amb els errors
        }

        return "redirect:/clients"; // Redirigeix si no hi ha errors
    }

    /**
     * Mostrar el formulari per modificar un client.
     *
     * @param dni El DNI del client que es vol modificar.
     * @param model El model per passar les dades del client a la vista.
     * @return El nom de la plantilla Thymeleaf per editar el client, o la
     * redirecció a la llista de clients si no es troba.
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
     * Processa la modificació d'un client. Gestiona errors de duplicació.
     *
     * @param dni El DNI del client que es vol modificar.
     * @param client Les noves dades del client.
     * @param result El resultat de la validació del formulari.
     * @param redirectAttributes Atributs de redirecció per passar missatges
     * d'èxit.
     * @return La redirecció a la pàgina de llista de clients si és correcte, o
     * el formulari si hi ha errors.
     */
    @PostMapping("/modificar/{dni}")
    public String updateClient(@PathVariable String dni,
            @ModelAttribute @Valid Client client,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "modificar_client"; // Si hi ha errors de validació, torna al formulari
        }

        try {
            client.setDni(dni); // Assegura que el DNI no es perdi durant l'actualització
            clientService.saveClient(client, true);
            redirectAttributes.addFlashAttribute("successMessage", "El client s'ha modificat correctament.");
        } catch (DuplicateResourceException e) {
            result.reject("dni", e.getMessage());
            return "modificar_client"; // Retorna al formulari amb els errors
        }

        return "redirect:/clients"; // Redirigeix si no hi ha errors
    }
}
