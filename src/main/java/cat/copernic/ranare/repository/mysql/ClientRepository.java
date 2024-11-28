/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.repository.mysql;

import cat.copernic.ranare.entity.mysql.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Raú
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    @Override
    Optional<Client> findById(String dni); // Para buscar por DNI
    
    Optional<Client> findByEmail(String email); // Para buscar por email
    
}
