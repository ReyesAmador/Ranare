/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Agent;
import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.enums.Rol;
import cat.copernic.ranare.repository.mysql.AgentRepository;
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
    
    @Autowired
    private AgentRepository agentRepository;
    
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
    
    public Agent crearAgent(Client client, Rol rol){
        Agent agent = Agent.builder().dni(client.getDni())
                .nom(client.getNom())
                .cognoms(client.getCognoms())
                .dataNaixement(client.getDataNaixement())
                .email(client.getEmail())
                .numeroTarjetaCredit(client.getNumeroTarjetaCredit())
                .adreca(client.getAdreca())
                .pais(client.getPais())
                .ciutat(client.getCiutat())
                .codiPostal(client.getCodiPostal())
                .reputacio(client.getReputacio())
                .rol(rol) // Establecer el rol (AGENT o ADMIN)
                .build();
        
        return agentRepository.save(agent);
    }
}
