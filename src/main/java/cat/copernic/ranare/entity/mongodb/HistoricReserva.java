/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mongodb;

import cat.copernic.ranare.enums.Estat;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa l'històric d'una reserva al sistema.
 * Aquesta classe es mapeja a la col·lecció "historic_reserves" de la base de dades no relacional.
 * Conté informació sobre la reserva, així com referències a altres entitats.
 * 
 * @author Raú
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "historic_reserves") // Col·lecció a MongoDB
public class HistoricReserva {

    /**
     * Identificador únic de l'històric de la reserva.
     */
    @Id
    private String id;

    /**
     * Detalls específics de la reserva històrica.
     * Aquest és un subdocument que conté la informació de la reserva.
     */
    @NotNull
    private Reserva reserva;

    /**
     * Referències lògiques a les col·leccions relacionades.
     * Aquests camps vinculen l'històric amb altres entitats a MongoDB.
     */
    @NotNull
    private String clienteId;      // Referència al DNI del client

    @NotNull
    private String agenteId;       // Referència al DNI de l'agent

    @NotNull
    private String vehiculoId;     // Referència a la matrícula del vehicle

    private String localizacionId; // Referència a l'ID de la localització (opcional)

    /**
     * Classe interna que representa els detalls d'una reserva.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reserva {

        /**
         * Identificador únic de la reserva.
         */
        private String id;

        /**
         * DNI del client que ha realitzat la reserva.
         */
        private String clientDni;

        /**
         * DNI de l'agent que ha realitzat la reserva.
         */
        private String agentDni;

        /**
         * Identificador del vehicle reservat.
         */
        private String matriculaVehicle;

        /**
         * Data i hora d'inici de la reserva.
         */
        private LocalDateTime dataInicio;

        /**
         * Data i hora de finalització de la reserva.
         */
        private LocalDateTime dataFin;

        /**
         * Fiança associada a la reserva.
         */
        private Double fianca;

        /**
         * Cost total de la reserva.
         */
        private Double costReserva;

        /**
         * Estat actual de la reserva.
         * Canviat a un enumerat per garantir valors consistents.
         */
        private Estat estat;

    }
}