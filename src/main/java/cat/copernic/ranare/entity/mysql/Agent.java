/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mysql;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class Agent {
    
    @Id
    private String dni;
    
    private String nom;
    
    private String cognom;
    
    private String email;
    
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "localitzacio")
    private Localitzacio localitzacio;
}
