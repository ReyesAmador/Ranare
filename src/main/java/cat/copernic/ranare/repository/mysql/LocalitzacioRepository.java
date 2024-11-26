/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.repository.mysql;

import cat.copernic.ranare.entity.mysql.Localitzacio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Una interface que hereda de JpaRepository amb els seus propis mètodes per poder implementar-los en la 
 * capa service. També es poden definir mètodes personalitzats sense implementar-los
 * @author reyes
 */
@Repository
public interface LocalitzacioRepository extends JpaRepository <Localitzacio,String>{
    
}
