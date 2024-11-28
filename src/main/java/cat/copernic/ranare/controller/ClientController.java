/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.enums.Rol;
import cat.copernic.ranare.service.mysql.AgentService;
import cat.copernic.ranare.service.mysql.ClientService;
import cat.copernic.ranare.exceptions.DuplicateResourceException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import cat.copernic.ranare.response.ErrorResponse;
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
     * @return La redirecció a la pàgina de llista de clients si és correcte, o
     * el formulari si hi ha errors.
     */
    @PostMapping("/api")
    public ResponseEntity<Object> createOrUpdateClient(@RequestBody @Valid Client client, BindingResult bindingResult) {
        // Si hay errores de validación en el formulario
        if (bindingResult.hasErrors()) {
            // Creamos una respuesta de error con los errores de validación
            ErrorResponse errorResponse = new ErrorResponse(bindingResult.getAllErrors());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            // Guardar o actualizar el cliente
            Client savedClient = clientService.saveClient(client, true, bindingResult);

            // Si no hay errores y el cliente se guarda correctamente
            return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
        } catch (DuplicateResourceException e) {
            // Si se lanza una excepción por duplicado, devolver un error con el mensaje adecuado
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
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
     * @param redirectAttributes
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

    /*
    /**
     * Procesa el formulario de creación de clientes o agentes. Gestiona errores
     * de duplicación.
     *
     * @param client El cliente creado a partir del formulario.
     * @param rol
     * @param bindingResult El resultado de la validación del formulario.
     * @param redirectAttributes Atributos de redirección para pasar mensajes de
     * éxito.
     * @param loggedUser
     * @return La redirección a la página de lista de clientes si es correcto, o
     * el formulario si hay errores.
     */

    @PostMapping("/crear_client")
    public String createClient(@ModelAttribute @Valid Client client,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal User loggedUser) {
        // Si hay errores de validación, retorna al formulario
        if (client.getDni() != null) {
        client.setDni(client.getDni().toUpperCase());
    }
        
        if (bindingResult.hasErrors()) {
            return "crear_client";  // Si hay errores, vuelve al formulario con los mensajes
        }

        // Si no hay errores, intenta guardar el cliente
        try {
            Client savedClient = clientService.saveClient(client, false, bindingResult);
            if (savedClient == null) {
                return "crear_client";  // Si hay errores de duplicado, vuelve al formulario
            }

            redirectAttributes.addFlashAttribute("missatge", "Client creat correctament.");
        } catch (DuplicateResourceException e) {
            // Manejo de duplicados
            return "crear_client";
        }

        return "redirect:/clients";  // Si todo está bien, redirige a la lista de clientes
    }

    /*
    @PostMapping("/crear_client")
    public String createClient(@ModelAttribute @Valid Client client,
            @RequestParam(required = false) Rol rol,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal User loggedUser) {

        // Si hay errores de validación, retornamos al formulario
        if (bindingResult.hasErrors()) {
            return "crear_client";
        }

        // Si no hay errores, intentamos guardar el cliente
        try {
            if (loggedUser.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")) && rol != null) {
                // Si es admin y se especifica un rol, crear un agente
                agentService.crearAgent(client, rol);
                redirectAttributes.addFlashAttribute("missatge", "Agent creat correctament.");
            } else {
                // Si no es admin, simplemente se crea el cliente
                Client savedClient = clientService.saveClient(client, false, bindingResult);
                if (savedClient == null) {
                    return "crear_client"; // Si hubo errores de duplicado, vuelve al formulario
                }
                redirectAttributes.addFlashAttribute("missatge", "Client creat correctament.");
            }
        } catch (DuplicateResourceException e) {
            // Rechazamos el valor del DNI o email en el caso de que sea duplicado
            if (e.getMessage().contains("DNI")) {
                bindingResult.rejectValue("dni", "duplicate.dni", e.getMessage());
            } else if (e.getMessage().contains("email")) {
                bindingResult.rejectValue("email", "duplicate.email", e.getMessage());
            }

            // Retornamos al formulario con los errores
            return "crear_client";
        }

        // Si no hay errores, redirigimos a la lista de clientes
        return "redirect:/clients";
    }  
     */
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
    /**
     * Procesa la modificación de un cliente. Gestiona errores de duplicación.
     *
     * @param dni El DNI del cliente que se quiere modificar.
     * @param client Los nuevos datos del cliente.
     * @param result El resultado de la validación del formulario.
     * @param redirectAttributes Atributos de redirección para pasar mensajes de
     * éxito.
     * @return La redirección a la página de lista de clientes si es correcto, o
     * el formulario si hay errores.
     */
    @PostMapping("/modificar/{dni}")
    public String updateClient(@PathVariable String dni,
            @ModelAttribute @Valid Client client,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "modificar_client"; // Si hay errores de validación, vuelve al formulario
        }

        try {
            // Asegúrate de que el DNI no cambia durante la actualización
            client.setDni(dni);
            Client updatedClient = clientService.saveClient(client, true, result);
            if (updatedClient == null) {
                return "modificar_client"; // Si hubo errores de duplicado, vuelve al formulario
            }

            redirectAttributes.addFlashAttribute("successMessage", "El client s'ha modificat correctament.");
        } catch (DuplicateResourceException e) {
            // Manejo de la excepción por duplicado de DNI o email
            if (e.getMessage().contains("DNI")) {
                result.rejectValue("dni", "duplicate.dni", e.getMessage());
            }
            if (e.getMessage().contains("correu electrònic")) {
                result.rejectValue("email", "duplicate.email", e.getMessage());
            }

            // Retorna al formulario con los errores
            return "modificar_client"; // Vuelve al formulario con los errores
        }

        return "redirect:/clients"; // Redirige a la lista de clientes si no hay errores
    }

}
