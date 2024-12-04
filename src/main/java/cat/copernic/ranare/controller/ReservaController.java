/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.controller;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.entity.mysql.Reserva;
import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.service.mysql.ClientService;
import cat.copernic.ranare.service.mysql.ReservaService;
import cat.copernic.ranare.service.mysql.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
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
     * @param model
     * @return
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
     * @param reserva
     * @param result
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/crear")
    public String crearReserva(@ModelAttribute @Valid Reserva reserva, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "formulari_reserva";
        }

        // Recuperar cliente y vehículo seleccionados
        Client client = clientService.getClientById(reserva.getClient().getDni())
                .orElseThrow(() -> new IllegalArgumentException("Client no trobat amb DNI: " + reserva.getClient().getDni()));

        Vehicle vehicle = vehicleService.getVehicleByMatricula(reserva.getVehicle().getMatricula());

        // Calcular fiança
        double fianca = reservaService.calcularFianca(client, vehicle);
        reserva.setFianca(fianca);

        // Calcular costReserva
        double costReserva = reservaService.calcularCostReserva(reserva.getDataInici(), reserva.getDataFin(), vehicle.getPreuPerHoraLloguer());
        reserva.setCostReserva(costReserva);

        // Guardar la reserva
        reservaService.crearReserva(reserva);
        redirectAttributes.addFlashAttribute("missatge", "Reserva creada correctament.");
        return "redirect:/reserves";
    }

    /**
     * Mostra la llista de reserves.
     *
     * @param model
     * @return
     */
    @GetMapping
    public String llistarReserves(Model model) {
        model.addAttribute("reserves", reservaService.getAllReserves());
        return "llista_reserves";
    }

    /**
     * Gestiona la sol·licitud per anul·lar una reserva.
     *
     * @param id
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/anular")
    public String anularReserva(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        reservaService.anularReserva(id);
        redirectAttributes.addFlashAttribute("missatge", "Reserva anul·lada correctament.");
        return "redirect:/reserves";
    }
}
