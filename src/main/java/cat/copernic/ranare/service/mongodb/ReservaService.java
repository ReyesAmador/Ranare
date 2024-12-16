/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mongodb;

import cat.copernic.ranare.entity.mongodb.HistoricReserva;
import cat.copernic.ranare.entity.mysql.Reserva;
import cat.copernic.ranare.repository.mongodb.HistoricReservaRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Raú
 */
@Service
public class ReservaService {
    
    @Autowired
    private HistoricReservaRepository historicReservaRepository;

    /**
     * Registra un cambio en el histórico de reservas.
     *
     * @param reserva La reserva afectada.
     * @param accio La acción realizada (CREAR, ACTUALIZAR, ELIMINAR, etc.).
     */
    public void registrarEnHistoric(Reserva reserva, String accio) {
        HistoricReserva historic = new HistoricReserva();
        historic.setIdReserva(reserva.getId());
        historic.setAccio(accio);
        historic.setEstat(reserva.getEstat());
        historic.setDataInici(reserva.getDataInici());
        historic.setDataFin(reserva.getDataFin());
        historic.setCostReserva(reserva.getCostReserva());
        historic.setFianca(reserva.getFianca());
        historic.setTimestamp(LocalDateTime.now());

        // Agregar información relacionada
        historic.setClient(reserva.getClient());
        historic.setAgent(reserva.getAgent());
        historic.setVehiculo(reserva.getVehicle());
        historic.setLocalizacion(reserva.getVehicle().getLocalitzacio());

        historicReservaRepository.save(historic);
    }
}
