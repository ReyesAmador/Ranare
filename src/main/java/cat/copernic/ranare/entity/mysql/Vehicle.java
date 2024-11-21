/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mysql;

import cat.copernic.ranare.enums.TipusCombustio;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ngall
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    
    @Id
    private String matricula;
    
    @Enumerated(EnumType.STRING)
    private TipusCombustio combustio;
    
    @Column(name = "limit_quilometratge")
    private double limitQuilometratge;

    @Column(name = "preu_per_hora_lloguer")
    private double preuPerHoraLloguer;

    @Column(name = "minim_hores_lloguer")
    private int minimHoresLloguer;

    @Column(name = "maxim_hores_lloguer")
    private int maximHoresLloguer;

    @Column(name = "fianca_standard")
    private double fiancaStandard;

    // Dades reservades (no accessibles per als clients)
    @ElementCollection
    @CollectionTable(name = "vehicle_documents", joinColumns = @JoinColumn(name = "vehicle_id"))
    @Column(name = "document")
    private List<String> documentsPDF;

    @ElementCollection
    @CollectionTable(name = "vehicle_imatges", joinColumns = @JoinColumn(name = "vehicle_id"))
    @Column(name = "imatge")
    private List<String> imatges;

    @Column(name = "comentaris_agent", length = 2000)
    private String comentarisAgent;
}
