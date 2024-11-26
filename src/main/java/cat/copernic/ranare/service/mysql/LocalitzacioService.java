/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Localitzacio;
import cat.copernic.ranare.exceptions.InvalidCodiPostalException;
import cat.copernic.ranare.repository.mysql.LocalitzacioRepository;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author reyes
 */

@Service
public class LocalitzacioService {
    
    @Autowired
    LocalitzacioRepository localitzacioRepository;
    
    public List<Localitzacio> getallLocalitzacio(){
        return localitzacioRepository.findAll();
    }
    

    
    public Localitzacio saveLocalitzacio(Localitzacio local){
        
        if(!validarCodiPostal(local.getCodiPostal()))
            throw new InvalidCodiPostalException("El codi postal ha de contenir només números.");
        return localitzacioRepository.save(local);
    }
    
    private boolean validarCodiPostal(String cp){
        return cp!= null && cp.matches("\\d+");
    }
}
