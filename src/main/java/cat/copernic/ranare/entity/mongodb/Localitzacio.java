/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mongodb;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Representa una localització per a vehicles dins del sistema.
 * Es mapeja a la col·lecció "localitzacions" a la base de dades no relacional.
 * Aquesta classe emmagatzema informació rellevant sobre la localització, com ara el codi postal,
 * el tipus de localització i els horaris d'obertura i tancament.
 * 
 * @author Raú
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "localitzacions")  // Col·lecció a MongoDB
public class Localitzacio {

    /**
     * Identificador únic de la localització.
     * MongoDB genera automàticament aquest camp si no s'especifica.
     */
    @Id
    private String id;

    /**
     * Codi postal de la localització.
     */
    @NotNull
    private int codiPostal;

    /**
     * Tipus de localització (per exemple: "magatzem", "punt de recollida", etc.).
     */
    @NotNull
    private String tipus;

    /**
     * Horari d'obertura de la localització.
     * Representat com un LocalTime per gestionar només hores.
     */
    @NotNull
    private LocalTime horariApertura;

    /**
     * Horari de tancament de la localització.
     * Representat com un LocalTime per gestionar només hores.
     */
    @NotNull
    private LocalTime horariTancament;
}