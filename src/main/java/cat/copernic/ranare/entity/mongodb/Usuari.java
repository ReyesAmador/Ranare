/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mongodb;

import cat.copernic.ranare.enums.Reputacio;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Representa un client registrat al sistema.
 * Aquesta classe es mapeja a la col·lecció "clients" de la base de dades no relacional.
 * Conté informació personal i altra informació rellevant del client.
 * 
 * @author Raú
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuaris") // Col·lecció a MongoDB
public class Usuari {

    /**
     * Identificador únic del document a la col·lecció.
     * Generat automàticament per MongoDB si no es proporciona.
     */
    @Id
    private String id;

    /**
     * Document Nacional d'Identitat (DNI) del client.
     */
    @NotNull
    private String dni;

    /**
     * Nom del client.
     */
    @NotNull
    private String nom;

    /**
     * Cognoms del client.
     */
    @NotNull
    private String cognoms;

    /**
     * Correu electrònic del client.
     */
    @NotNull
    private String email;

    /**
     * Data de caducitat del DNI.
     * Utilitza LocalDate per representar la data.
     */
    private LocalDate dataExpiracionDni;

    /**
     * Número de la targeta de crèdit del client.
     */
    private String numeroTarjetaCredit;

    /**
     * Adreça física del client.
     */
    private String adreca;

    /**
     * Reputació del client.
     * Representada com un enumerat per garantir valors consistents.
     */
    @Enumerated(EnumType.STRING)
    private Reputacio reputacio;

    /**
     * Llista de referències a documents relacionats amb el client.
     * Pot incloure URLs o identificadors d'altres documents.
     */
    @Field("documents")
    private List<String> documents;

}