/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Agent;
import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.enums.Rol;
import cat.copernic.ranare.exceptions.AgentNotFoundException;
import cat.copernic.ranare.repository.mysql.AgentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    public void guardarAgent(Agent agent){
        agentRepository.save(agent);
    }
    
    public void eliminarAgent(String dni) {
        Optional<Agent> agentOpt = agentRepository.findById(dni);
        if(agentOpt.isPresent())
            agentRepository.delete(agentOpt.get());
        else
            throw new AgentNotFoundException("L'agent amb DNI " + dni + " no existeix");
    }
}
