/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mysql;

import cat.copernic.ranare.enums.Reputacio;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;

/**
 *
 * @author Raú
 */
/**
 * /**
 * Classe que representa un client del sistema com a entitat relacional. Aquesta
 * classe està mapejada a una base de dades relacional utilitzant JPA.
 *
 * Nota: Hereta atributs comuns de la classe Usuari (si s'aplica herència en un
 * futur) i afegeix atributs específics per als clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Client {

    /**
     * Document Nacional d'Identitat (DNI). Actua com a clau primària única.
     */
    @Id
    @Column(nullable = false, unique = true, length = 9)
    private String dni;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String cognoms;

    /**
     * Correu electrònic de l'usuari.
     */
    @Column(nullable = false)
    private String email;

    /**
     * Adreça física del client.
     */
    @Column(nullable = false)
    private String adreca;

    /**
     * País de residència del client.
     */
    @Column(nullable = false)
    private String pais;

    /**
     * Ciutat de residència del client.
     */
    @Column(nullable = false)
    private String ciutat;

    /**
     * Codi postal associat a l'adreça del client.
     */
    @Column(nullable = false, length = 10)
    private String codiPostal;

    @Transient
    private List<Binary> documents;

    /**
     * Número de la targeta de crèdit associada al client.
     */
    @Column(nullable = false, unique = true)
    private String numeroTarjetaCredit;

    /**
     * Reputació del client. Pot ser "NORMAL" o "PREMIUM".
     */
    @Column(nullable = false)
    private Reputacio reputacio;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Reserva> reserves;

    /**
     * Referència a la documentació en la base no relacional. Aquest camp pot
     * apuntar al document corresponent a MongoDB (pot ser un ID o una URL).
     */
    @Column(nullable = true)
    private String referenciaDocumentacio;

}
