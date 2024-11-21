/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mysql;

import cat.copernic.ranare.enums.Rol;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa un agent dins del sistema, amb la seva informació personal i el rol assignat.
 * Un agent està relacionat amb una localització, i pot ser assignat a una sola localització a través de la relació un a un.
 * 
 * La classe està mapejada a una taula de la base de dades utilitzant JPA.
 * Utilitza les anotacions de Lombok per generar automàticament els mètodes getters, setters, constructors, 
 * i altres mètodes útils per la classe.
 * 
 * 
 * La relació amb la classe {@link Localitzacio} és de tipus un a un, i l'entitat {@link Localitzacio} 
 * té el camp `agent` que fa referència a aquesta classe.
 * 
 * @author reyes
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent extends Client{
    
    /**
     * Rol de l'agent, determinat per un valor de l'enumeració {@link Rol}.
     */
    @Enumerated(EnumType.STRING)
    private Rol rol;
    
    
    /**
     * Localització assignada a l'agent. Relació un a un amb la classe {@link Localitzacio}.
     * La propietat `mappedBy` indica que aquesta relació és gestionada per la classe {@link Localitzacio}.
     * 
     * {@link CascadeType.ALL} indica que les operacions de persistència en un agent també 
     * es propagaran a la localització associada.
     */
    @OneToOne(mappedBy = "agent",cascade = CascadeType.ALL)
    private Localitzacio localitzacio;
}
