/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.repository.mysql.ClientRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Raú
 */
@Service
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    // Crear o actualizar un cliente
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    // Leer un cliente por su DNI
    public Optional<Client> getClientById(String dni) {
        return clientRepository.findById(dni);
    }

    // Leer todos los clientes
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Eliminar un cliente por su DNI
    public void deleteClient(String dni) {
        clientRepository.deleteById(dni);
    }
    
}
