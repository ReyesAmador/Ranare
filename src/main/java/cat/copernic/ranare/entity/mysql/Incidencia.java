/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mysql;

import cat.copernic.ranare.enums.EstatIncidencia;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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
public class Incidencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incidencia")
    private Long idIncidencia;
    
    @Enumerated(EnumType.STRING)
    private EstatIncidencia estat;
    
    private String motiu;
    
    private double cost;
    
    @Column(name = "data_inici")
    private LocalDateTime dataInici; 
    
    @Column(name = "data_final")
    private LocalDateTime dataFinal;
    
    @ElementCollection
    @CollectionTable(name = "incidencia_documents", joinColumns = @JoinColumn(name = "incidencia_id"))
    @Column(name = "document")
    private List<String> documents; // Fotos, PDF, etc.

    // Relació amb Vehicle (una incidència està associada a un vehicle)
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
}
