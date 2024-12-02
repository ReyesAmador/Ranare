/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Localitzacio;
import cat.copernic.ranare.entity.mysql.Vehicle;
import cat.copernic.ranare.exceptions.InvalidCodiPostalException;
import cat.copernic.ranare.exceptions.InvalidHorariException;
import cat.copernic.ranare.repository.mysql.LocalitzacioRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servei per gestionar les operacions relacionades amb les localitzacions.
 * Proporciona funcionalitats per obtenir totes les localitzacions i guardar una nova localització.
 * @author reyes
 */

@Service
public class LocalitzacioService {
    
    /**
     * Repositori de localitzacions utilitzat per interactuar amb la base de dades.
     */
    @Autowired
    LocalitzacioRepository localitzacioRepository;
    
    /**
     * Obté totes les localitzacions emmagatzemades a la base de dades.
     *
     * @return Una llista amb totes les localitzacions.
     */
    public List<Localitzacio> getallLocalitzacio(){
        return localitzacioRepository.findAll();
    }
    
    /**
     * Guarda una nova localització a la base de dades.
     * Verifica que el codi postal sigui vàlid abans de guardar.
     *
     * @param local La localització que es vol guardar.
     * @return La localització que s'ha guardat.
     * @throws InvalidCodiPostalException Si el codi postal no conté només números.
     */
    public Localitzacio saveLocalitzacio(Localitzacio local){
        
        if(!validarCodiPostal(local.getCodiPostal()))
            throw new InvalidCodiPostalException("El codi postal ha de contenir només números.");
        return localitzacioRepository.save(local);
    }
    
    /**
     * Valida si el codi postal proporcionat és vàlid.
     * El codi postal es considera vàlid si no és null i només conté números.
     *
     * @param cp El codi postal a validar.
     * @return True si el codi postal és vàlid, false en cas contrari.
     */
    private boolean validarCodiPostal(String cp){
        return cp!= null && cp.matches("\\d+");
    }
    
    //comprovar si l'horari d'apertura es abans de l'horari de tancament
    public void validarHorari(LocalTime horariApertura, LocalTime horariTancament) throws InvalidHorariException{
        if(horariApertura.isAfter(horariTancament) || horariApertura.equals(horariTancament))
            throw new InvalidHorariException("L'horari de tancament ha de ser més tard que el d'apertura");
    }
    
    //retorna els vehicles associats a una localitzacio, es comproba també que el codi postal existeixi
    public Set<Vehicle> getVehiclePerLocalitzacio(String codiPostal){
        Localitzacio localitzacio = localitzacioRepository.trobarVehiclesPerCodiPostal(codiPostal)
                .orElseThrow(() -> new InvalidCodiPostalException("Localització amb el codi postal " + codiPostal +" no trobada"));
        
        return localitzacio.getVehicles();
    }
    
    /**
     * Retorna una localització pel seu codi postal.
     * 
     * @param codiPostal El codi postal de la localització.
     * @return La localització corresponent al codi postal.
     * @throws EntityNotFoundException Si no es troba cap localització amb el codi postal.
     */
    public Localitzacio getLocalitzacioPerCodiPostal(String codiPostal){
        return localitzacioRepository.findById(codiPostal)
                .orElseThrow(() -> new InvalidCodiPostalException("Localització amb el codi postal " + codiPostal +" no trobada"));
    }
}
