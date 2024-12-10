/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.entity.mysql.Reserva;
import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.entity.mysql.VehicleDTO;
import cat.copernic.ranare.service.mysql.ClientService;
import cat.copernic.ranare.service.mysql.ReservaService;
import cat.copernic.ranare.service.mysql.VehicleService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Classe que gestiona les funcionalitats relacionades amb les reserves del
 * sistema. Aquesta classe és un controlador que combina la gestió del frontend
 * amb Thymeleaf i els serveis REST necessaris per a comunicació asíncrona amb
 * el backend.
 *
 * Modificacions recents: - Implementació de càlcul de la fiança i desglòs de
 * costos. - Gestió de vehicles disponibles segons el període de reserva. -
 * Optimització del procés de reserva per evitar conflictes en les dates.
 *
 * @author Raú
 */
@Controller
@RequestMapping("/reserves")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private VehicleService vehicleService;

    /**
     * Mostra el formulari per crear una nova reserva.
     *
     * Aquest mètode inicialitza el formulari per la creació d'una reserva.
     * Carrega la llista de clients i vehicles disponibles per ser seleccionats.
     *
     * @param dataInici
     * @param dataFin
     * @param model Objecte del model utilitzat per passar dades a la vista.
     * @return El nom de la plantilla Thymeleaf "crear_reserva".
     */
    @GetMapping("/nova")
    public String mostrarFormulariNovaReserva(Model model) {
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "crear_reserva";
    }

    /**
     * Gestiona l'enviament del formulari per crear una nova reserva.
     *
     * Aquest mètode valida les dades del formulari, calcula la fiança i el cost
     * total de la reserva segons les dades seleccionades i guarda la reserva al
     * sistema.
     *
     * @param reserva L'objecte Reserva amb les dades del formulari.
     * @param result Resultat de la validació del formulari.
     * @param redirectAttributes Objecte per afegir missatges d'estat a les
     * redireccions.
     * @return Redirecció a la pàgina de llista de reserves.
     */
    @PostMapping("/crear")
    public String crearReserva(@ModelAttribute @Valid Reserva reserva, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "crear_reserva";
        }

        // Validar cliente y vehículo
        Client client = clientService.getClientById(reserva.getClient().getDni())
                .orElseThrow(() -> new IllegalArgumentException("Client no trobat."));
        Vehicle vehicle = vehicleService.getVehicleByMatricula(reserva.getVehicle().getMatricula());

        // Validar fechas
        if (reserva.getDataInici().isAfter(reserva.getDataFin()) || reserva.getDataInici().isEqual(reserva.getDataFin())) {
            redirectAttributes.addFlashAttribute("error", "La data de inici ha de ser abans que la data de finalització.");
            return "crear_reserva";
        }

        // Calcular fiança y coste total
        double fianca = reservaService.calcularFianca(client, vehicle);
        double costReserva = reservaService.calcularCostReserva(reserva.getDataInici(), reserva.getDataFin(), vehicle.getPreuPerHoraLloguer(), fianca);

        reserva.setFianca(fianca);
        reserva.setCostReserva(costReserva);

        // Guardar reserva
        reservaService.crearReserva(reserva);
        redirectAttributes.addFlashAttribute("missatge", "Reserva creada correctament.");
        return "redirect:/reserves";
    }

    /**
     * Mostra la llista de totes les reserves.
     *
     * Aquest mètode carrega totes les reserves del sistema i les passa a la
     * vista.
     *
     * @param model Objecte del model utilitzat per passar dades a la vista.
     * @return El nom de la plantilla Thymeleaf "llista_reserves".
     */
    @GetMapping
    public String llistarReserves(Model model) {
        model.addAttribute("reserves", reservaService.getAllReserves());
        return "llista_reserves";
    }

    /**
     * Gestiona la sol·licitud per anul·lar una reserva existent.
     *
     * Aquest mètode actualitza l'estat de la reserva a "anul·lada".
     *
     * @param id Identificador de la reserva que s'ha d'anul·lar.
     * @param redirectAttributes Objecte per afegir missatges d'estat a les
     * redireccions.
     * @return Redirecció a la pàgina de llista de reserves.
     */
    @PostMapping("/anular")
    public String anularReserva(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        reservaService.anularReserva(id);
        redirectAttributes.addFlashAttribute("missatge", "Reserva anul·lada correctament.");
        return "redirect:/reserves";
    }

    @GetMapping("/filtrar-vehiculos")
    @ResponseBody
    public List<VehicleDTO> filtrarVehiculosDisponibles(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInici,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFin) {
        // Filtrar vehículos disponibles en las fechas seleccionadas
        List<VehicleDTO> vehiclesDTO = vehicleService.filtrarVehiculosDisponiblesDTO(dataInici, dataFin);
        return vehiclesDTO;
    }

}
