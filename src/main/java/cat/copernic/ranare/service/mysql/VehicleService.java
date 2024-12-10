/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Reserva;
import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.entity.mysql.VehicleDTO;
import cat.copernic.ranare.repository.mysql.ReservaRepository;

import cat.copernic.ranare.repository.mysql.VehicleRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngall
 */
@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ReservaService reservaRepository;

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

    public Vehicle getVehicleByMatricula(String matricula) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(matricula);
        return vehicle.orElse(null);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public void deleteVehicle(String matricula) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(matricula);

        if (vehicle.isPresent()) {
            vehicleRepository.deleteById(matricula);
        } else {
            throw new IllegalArgumentException("No es pot eliminar un vehicle que no existeix.");
        }
    }

    public List<VehicleDTO> filtrarVehiculosDisponiblesDTO(LocalDateTime dataInici, LocalDateTime dataFin) {
        // Obtener reservas solapadas
        List<Reserva> overlappingReservations = reservaRepository.findOverlappingReservations(dataInici, dataFin);

        // Obtener vehículos reservados
        List<Vehicle> reservedVehicles = overlappingReservations.stream()
                .map(Reserva::getVehicle)
                .distinct()
                .collect(Collectors.toList());

        // Filtrar vehículos no reservados
        List<Vehicle> allVehicles = getAllVehicles();
        List<Vehicle> availableVehicles = allVehicles.stream()
                .filter(vehicle -> !reservedVehicles.contains(vehicle))
                .collect(Collectors.toList());

        // Convertir a DTO
        return availableVehicles.stream()
                .map(vehicle -> new VehicleDTO(vehicle))
                .collect(Collectors.toList());
    }

}
