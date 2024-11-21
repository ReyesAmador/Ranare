/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mysql;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author reyes
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Localitzacio {
    
    @Id
    @Column(name = "codi_postal")
    private Integer codiPostal;
    
    private String adreca;
    
    private String tipus;
    
    private LocalDateTime horari;
    
    @OneToOne
    @JoinColumn(name = "dni_agent", referencedColumnName = "dni")
    private Agent agent;
    
    @OneToMany(mappedBy = "localitzacio", cascade = CascadeType.ALL)
    private Set<Vehicle> vehicles;
}
