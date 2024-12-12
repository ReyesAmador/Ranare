/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mysql;

import cat.copernic.ranare.enums.TipusCombustio;
import cat.copernic.ranare.enums.TipusVehicle;
import lombok.Data;

/**
 *
 * @author Raú
 */
@Data
public class VehicleDTO {

    // Atributos que vamos a transferir al frontend
    private String matricula;         // Matricula del vehículo
    private String nomVehicle;        // Nombre del vehículo
    private TipusCombustio combustio;        // Tipo de combustible
    private double preuPerHoraLloguer; // Precio por hora de alquiler
    private double fiancaStandard;     // Fianza estándar
    private double limitQuilometratge; // Límite de kilometraje
    private int minimHoresLloguer;    // Mínimo de horas de alquiler
    private int maximHoresLloguer;    // Máximo de horas de alquiler
    private TipusVehicle tipus;
    private int passatgers;
    private boolean disponibilitat;
    private int potencia;
    private Localitzacio localitzacio;
    
    

    public VehicleDTO() {
    }

    public VehicleDTO(Vehicle vehicle) {
        this.matricula = matricula;
        this.nomVehicle = nomVehicle;
        this.combustio = combustio;
        this.preuPerHoraLloguer = preuPerHoraLloguer;
        this.fiancaStandard = fiancaStandard;
        this.limitQuilometratge = limitQuilometratge;
        this.minimHoresLloguer = minimHoresLloguer;
        this.maximHoresLloguer = maximHoresLloguer;
        this.tipus = tipus;
        this.passatgers = passatgers;
        this.disponibilitat = disponibilitat;
        this.potencia = potencia;
        this.localitzacio = localitzacio;
    }



}
