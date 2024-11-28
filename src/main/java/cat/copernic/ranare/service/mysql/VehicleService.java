/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.repository.mysql.VehicleRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngall
 */
@Service
public class VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    public Vehicle saveVehicle(Vehicle vehicle){
        if(vehicle.getMinimHoresLloguer() > vehicle.getMaximHoresLloguer()){
            throw new IllegalArgumentException("El mínim d'hores de lloguer no pot ser superior al màxim.");
        }
        if(vehicle.getPreuPerHoraLloguer() <= 0){
            throw new IllegalArgumentException("El preu per hora de lloguer ha de ser un valor positiu.");
        }
        if(vehicle.getTransmissio() == null){
            throw new IllegalArgumentException("El tipus de transmissió ha de ser especificat.");
        }
        
        return vehicleRepository.save(vehicle);
    }
    
    public Vehicle getVehicleByMatricula(String matricula){
        Optional<Vehicle> vehicle = vehicleRepository.findById(matricula);
        return vehicle.orElse(null);
    }
    
    public List<Vehicle> getAllVehicles(){
        return vehicleRepository.findAll();
    }
    
    public void deleteVehicle(String matricula){
        Optional<Vehicle> vehicle = vehicleRepository.findById(matricula);
        
        if(vehicle.isPresent()){
            vehicleRepository.deleteById(matricula);
        }else{
            throw new IllegalArgumentException("No es pot eliminar un vehicle que no existeix.");
        }
    }
}
