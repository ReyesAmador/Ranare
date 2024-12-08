/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.entity.mysql.Reserva;
import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.enums.EstatReserva;
import cat.copernic.ranare.enums.Reputacio;
import cat.copernic.ranare.repository.mysql.ReservaRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Raú
 */
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    /**
     * Crea una nova reserva.
     *
     * @param reserva La reserva a guardar.
     * @return La reserva guardada.
     */
    public Reserva crearReserva(Reserva reserva) {
        reserva.setEstat(EstatReserva.ACTIVA);

        return reservaRepository.save(reserva);
    }

    /**
     * Obté una reserva pel seu identificador.
     *
     * @param id L'identificador de la reserva.
     * @return La reserva, si existeix.
     */
    public Optional<Reserva> obtenirReservaPerId(Long id) {
        return reservaRepository.findById(id);
    }

    /**
     * Obté totes les reserves d'un client.
     *
     * @param client El client.
     * @return Llista de reserves del client.
     */
    public List<Reserva> obtenirReservesPerClient(Client client) {
        return reservaRepository.findByClient(client);
    }

    /**
     * Anul·la una reserva canviant el seu estat.
     *
     * @param id L'identificador de la reserva.
     */
    public void anularReserva(Long id) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            reserva.setEstat(EstatReserva.ANULADA);
            reservaRepository.save(reserva);
        }
    }

    /**
     * Finalitza una reserva canviant el seu estat.
     *
     * @param id L'identificador de la reserva.
     */
    public void finalitzarReserva(Long id) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            reserva.setEstat(EstatReserva.FINALITZADA);
            reservaRepository.save(reserva);
        }
    }

    public List<Reserva> getAllReserves() {
        return reservaRepository.findAll();
    }

 

    public double calcularFianca(Client client, Vehicle vehicle) {
        double fiancaStandard = vehicle.getFiancaStandard();
        return client.getReputacio() == Reputacio.PREMIUM ? fiancaStandard * 0.75 : fiancaStandard;
    }

   public double calcularCostReserva(LocalDateTime dataInici, LocalDateTime dataFin, double preuPerHoraLloguer, double fianca) {
    long hores = ChronoUnit.HOURS.between(dataInici, dataFin);
    if (hores <= 0) {
        throw new IllegalArgumentException("La data de finalització ha de ser com a mínim 1 hora posterior a la data d'inici.");
    }
    return (hores * preuPerHoraLloguer) + fianca;
}

    


}
