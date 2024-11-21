/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mysql;

import cat.copernic.ranare.enums.EstatIncidencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ngall
 * @version 21/11/2024.1
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
    
    @Transient // Evita que este campo sea persistido en la base de datos SQL
    private String documentsIncidenciaId; 

}
