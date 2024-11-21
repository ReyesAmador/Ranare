/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mongodb;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa la informació addicional i no estructurada d'un usuari.
 * Aquesta informació és comuna per a tots els tipus d'usuaris (ADMIN, CLIENT, etc.).
 * Es mapeja a la col·lecció "documentacio_usuaris" de la base de dades no relacional.
 * 
 * @author Raú
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "documentacio_usuaris") // Col·lecció a MongoDB
public class DocumentacioUsuari {

    /**
     * Identificador únic del document a la col·lecció.
     * Generat automàticament per MongoDB si no es proporciona.
     */
    @Id
    private String id;

    /**
     * Llista de referències a documents (URLs o identificadors) relacionats amb l'usuari.
     * Poden ser imatges, PDFs, contractes, etc.
     */
    @NotNull
    private List<Binary> documents;


    /**
     * ID de referència a l'usuari relacionat amb aquest document.
     * Aquest camp és opcional.
     */
    private String userId;
}