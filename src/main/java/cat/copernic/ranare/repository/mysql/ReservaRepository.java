/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.repository.mysql;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.entity.mysql.Reserva;
import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.enums.EstatReserva;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Raú
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Troba totes les reserves associades a un client específic.
     *
     * @param client El client de les reserves.
     * @return Llista de reserves del client.
     */
    List<Reserva> findByClient(Client client);

    /**
     * Troba totes les reserves associades a un vehicle específic.
     *
     * @param vehicle El vehicle de les reserves.
     * @return Llista de reserves del vehicle.
     */
    List<Reserva> findByVehicle(Vehicle vehicle);

    /**
     * Troba totes les reserves amb un estat específic.
     *
     * @param estat L'estat de les reserves.
     * @return Llista de reserves amb aquest estat.
     */
    List<Reserva> findByEstat(EstatReserva estat);

    /**
     * Cerca totes les reserves entre dues dates.
     *
     * @param dataInici Data d'inici.
     * @param dataFin Data de finalització.
     * @return Llista de reserves dins del període.
     */
    List<Reserva> findByDataIniciBetween(LocalDateTime dataInici, LocalDateTime dataFin);
}