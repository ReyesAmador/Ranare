/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mysql;

import cat.copernic.ranare.enums.TipusCombustio;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa un vehicle dins del sistema de gestió.
 * Aquesta entitat conté la informació del vehicle, com la matrícula, tipus de combustió, límits de quilometratge,
 * preus de lloguer, fiances i les relacions amb altres entitats com les incidències i la localització.
 * @author ngall
 * @version 21/11/2024.1
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    
    /**
     * Matrícula del vehicle, que actua com a identificador únic dins del sistema.
     */
    @Id
    private String matricula;
    
    /**
     * Tipus de combustible que utilitza el vehicle.
     * Es tracta d'un tipus enum que pot tenir valors com 'Elèctric', 'Híbrid' o 'Combustió'.
     */
    @Enumerated(EnumType.STRING)
    private TipusCombustio combustio;
    
    /**
     * Límit de quilometratge permès per al vehicle.
     * Aquesta propietat especifica el nombre màxim de quilòmetres que es poden recórrer amb el vehicle abans d'arribar al límit.
     */
    @Column(name = "limit_quilometratge")
    private double limitQuilometratge;

    /**
     * Preu per hora de lloguer del vehicle.
     * Aquesta propietat defineix el cost per cada hora de lloguer d'aquest vehicle.
     */
    @Column(name = "preu_per_hora_lloguer")
    private double preuPerHoraLloguer;

    /**
     * Nombre mínim d'hores per al lloguer del vehicle.
     * Aquesta propietat indica el nombre mínim d'hores que es pot llogar el vehicle.
     */
    @Column(name = "minim_hores_lloguer")
    private int minimHoresLloguer;

    /**
     * Nombre màxim d'hores per al lloguer del vehicle.
     * Aquesta propietat especifica el nombre màxim d'hores per al lloguer del vehicle.
     */
    @Column(name = "maxim_hores_lloguer")
    private int maximHoresLloguer;

    /**
     * Fiança estàndard que es requereix per al lloguer del vehicle.
     * Aquesta propietat especifica la quantitat de diners que s'ha de dipositar com a garantia en el moment del lloguer.
     */
    @Column(name = "fianca_standard")
    private double fiancaStandard;

    /**
     * Comentaris addicionals que l'agent pot afegir sobre el vehicle.
     * Aquesta propietat emmagatzema una cadena de text que permet afegir observacions específiques del vehicle.
     */
    @Column(name = "comentaris_agent", length = 2000)
    private String comentarisAgent;
    
    /**
     * Relació entre Vehicle i Incidència.
     * Aquesta relació es defineix com un "OneToMany" ja que un vehicle pot tenir diverses incidències associades.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id")
    private List<Incidencia> incidencies;
    
    /**
     * Relació entre Vehicle i Localització.
     * Un vehicle està associat a una localització mitjançant el codi postal.
     * La relació es defineix com a "ManyToOne", ja que diversos vehicles poden compartir la mateixa localització.
     */
    @ManyToOne
    @JoinColumn(name = "localitzacio", referencedColumnName = "codi_postal")
    private Localitzacio localitzacio;
}
