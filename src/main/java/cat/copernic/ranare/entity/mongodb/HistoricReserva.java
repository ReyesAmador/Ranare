/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mongodb;

import cat.copernic.ranare.enums.Estat;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
     * Referències lògiques a les col·leccions relacionades.
     * Aquests camps vinculen l'històric amb altres entitats a MongoDB.
     */
    @NotNull
    private String clientDNI;      // Referència al DNI del client

    @NotNull
    private String clientEmail;
    
    @NotNull
    private String agentDNI;       // Referència al DNI de l'agent
 
    @NotNull
    private String vehicleMatricula;     // Referència a la matrícula del vehicle

    @NotNull
    private Date dataInici;
    
    @NotNull
    private Date dataFinal;
    

}