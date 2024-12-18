/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author reyes
 */
@Data
@AllArgsConstructor
public class VehicleDto2 {
    private String matricula;
    private String nomVehicle;
    private String imgBase64;
    private String codiPostal;
    private Double preuPerHoraLloguer;
}
