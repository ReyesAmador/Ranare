/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Reserva;
import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.entity.mysql.VehicleDTO;
import cat.copernic.ranare.enums.EstatReserva;
import cat.copernic.ranare.repository.mysql.ReservaRepository;

import cat.copernic.ranare.repository.mysql.VehicleRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servei per a la gestió de vehicles, incloent funcionalitats de validació,
 * obtenció i filtratge segons disponibilitat i restriccions.
 *
 * @author ngall
 */
@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ReservaService reservaRepository;

    /**
     * Guarda un vehicle després de validar-lo.
     *
     * @param vehicle El vehicle a guardar.
     * @return El vehicle guardat.
     * @throws IllegalArgumentException Si alguna validació no es compleix.
     */
    public Vehicle saveVehicle(Vehicle vehicle) {
        if (vehicle.getMinimHoresLloguer() > vehicle.getMaximHoresLloguer()) {
            throw new IllegalArgumentException("El mínim d'hores de lloguer no pot ser superior al màxim.");
        }
        if (vehicle.getPreuPerHoraLloguer() <= 0) {
            throw new IllegalArgumentException("El preu per hora de lloguer ha de ser un valor positiu.");
        }
        if (vehicle.getTransmissio() == null) {
            throw new IllegalArgumentException("El tipus de transmissió ha de ser especificat.");
        }

        return vehicleRepository.save(vehicle);
    }

    /**
     * Obté un vehicle per la seva matrícula.
     *
     * @param matricula La matrícula del vehicle.
     * @return El vehicle, o null si no es troba.
     */
    public Vehicle getVehicleByMatricula(String matricula) {
        return vehicleRepository.findById(matricula).orElse(null);
    }

    /**
     * Obté tots els vehicles del sistema.
     *
     * @return Una llista de tots els vehicles.
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /**
     * Elimina un vehicle per la seva matrícula.
     *
     * @param matricula La matrícula del vehicle a eliminar.
     * @throws IllegalArgumentException Si el vehicle no existeix.
     */
    public void deleteVehicle(String matricula) {
        if (vehicleRepository.existsById(matricula)) {
            vehicleRepository.deleteById(matricula);
        } else {
            throw new IllegalArgumentException("No es pot eliminar un vehicle que no existeix.");
        }
    }

    /**
     * Filtra els vehicles disponibles segons un període de temps i atributs de
     * vehicle.
     *
     * @param dataInici Data d'inici de la reserva.
     * @param dataFin Data de finalització de la reserva.
     * @return Una llista de vehicles disponibles en el període.
     */
    public List<VehicleDTO> filtrarVehiculosDisponiblesDTO(LocalDateTime dataInici, LocalDateTime dataFin) {
        // Obtenir reserves solapades només amb estat ACTIVA
        List<Reserva> overlappingReservations = reservaRepository.findOverlappingReservations(dataInici, dataFin);

        // Obtenir vehicles reservats
        List<Vehicle> reservedVehicles = overlappingReservations.stream()
                .map(Reserva::getVehicle)
                .distinct()
                .collect(Collectors.toList());

        // Obtenir tots els vehicles i filtrar segons disponibilitat, duració i no reservats
        List<Vehicle> allVehicles = getAllVehicles();
        List<Vehicle> availableVehicles = allVehicles.stream()
                .filter(vehicle -> !reservedVehicles.contains(vehicle)) // No reservats
                .filter(Vehicle::isDisponibilitat) // Només disponibles
                .filter(vehicle -> validarDuracio(dataInici, dataFin, vehicle)) // Validar duració
                .collect(Collectors.toList());

        // Convertir a DTO
        return availableVehicles.stream()
                .map(VehicleDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Valida si la durada de la reserva compleix amb els requisits del vehicle.
     *
     * @param dataInici Data d'inici de la reserva.
     * @param dataFin Data de finalització de la reserva.
     * @param vehicle Vehicle a validar.
     * @return Cert si la duració és vàlida; fals altrament.
     */
    private boolean validarDuracio(LocalDateTime dataInici, LocalDateTime dataFin, Vehicle vehicle) {
        long horesSolicitades = ChronoUnit.HOURS.between(dataInici, dataFin);
        return horesSolicitades >= vehicle.getMinimHoresLloguer() && horesSolicitades <= vehicle.getMaximHoresLloguer();
    }
    
    public List<Vehicle> getRandomVehicles(){
        return vehicleRepository.findRandomVehicles();
    }

}
