/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Agent;
import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.enums.Rol;
import cat.copernic.ranare.exceptions.AgentNotFoundException;
import cat.copernic.ranare.exceptions.DuplicateResourceException;
import cat.copernic.ranare.repository.mysql.AgentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author reyes
 */
@Service
public class AgentService {
    
    @Autowired
    private AgentRepository agentRepository;
    
    public List<Agent> getAllAgents() {
        return agentRepository.findAll(); // Devuelve la lista de agentes
    }
    
    @Transactional
    public Agent crearAgent(Client client, Rol rol) {
        // Verificar si ya existe un agente con el mismo DNI
        Optional<Agent> existingAgent = agentRepository.findById(client.getDni());
        if (existingAgent.isPresent()) {
            // Si ya existe un agente con el mismo DNI, se lanza una excepción
            throw new DuplicateResourceException("El DNI ya está asignado a otro agente.");
        }

        // Limpiar la sesión para evitar la duplicación del objeto en la sesión de Hibernate

        Agent agent = Agent.builder()
                .dni(client.getDni())
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
                .rol(rol)
                .build();

        // Guardar el nuevo agente
        return agentRepository.save(agent);
    }
    
    public void guardarAgent(Agent agent){
        agentRepository.save(agent);
    }
     public Optional<Agent> findAgentByDni(String dni) {
        return agentRepository.findById(dni); // Busca un agente por su DNI
    }
    
    public void eliminarAgent(String dni) {
        Optional<Agent> agentOpt = agentRepository.findById(dni);
        if(agentOpt.isPresent())
            agentRepository.delete(agentOpt.get());
        else
            throw new AgentNotFoundException("L'agent amb DNI " + dni + " no existeix");
    }
}
