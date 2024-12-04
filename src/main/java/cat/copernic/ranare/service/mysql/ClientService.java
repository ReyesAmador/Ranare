
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.exceptions.ClientNotFoundException;
import cat.copernic.ranare.repository.mysql.ClientRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

import java.util.List;

import cat.copernic.ranare.exceptions.ClientNotFoundException;

import cat.copernic.ranare.entity.mysql.Client;

/**
 *
 * @author Raú
 */
// Crear o actualizar un cliente
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Desa un client a la base de dades.
     *
     * Detecta si és una creació o una modificació en funció de l'existència del
     * client.
     *
     * @param client El client a desar.
     * @param isUpdate
     * @param bindingResult
     * @return `true` si l'operació és correcta, `false` si falla per
     * duplicació.
     */
    public Client saveClient(Client client, boolean isUpdate, BindingResult bindingResult) {
        // Verificación de duplicados sin interrumpir el flujo
        List<String> errorMessages = new ArrayList<>();

        if (client.getDni() != null) {
            client.setDni(client.getDni().toUpperCase());
        }

        // Verificación de duplicado para DNI
        if (isUpdate) {
            Optional<Client> existingClientByDni = clientRepository.findById(client.getDni());
            if (existingClientByDni.isPresent() && !existingClientByDni.get().getDni().equals(client.getDni())) {
                // Si ya existe un cliente con ese DNI, se agrega un error específico para el DNI
                bindingResult.rejectValue("dni", "duplicate.dni", "El DNI ja està registrat.");
            }
        } else {
            Optional<Client> existingClientByDni = clientRepository.findById(client.getDni());
            if (existingClientByDni.isPresent()) {
                // Si ya existe un cliente con ese DNI, se agrega un error específico para el DNI
                bindingResult.rejectValue("dni", "duplicate.dni", "El DNI ja està registrat.");
            }
        }

        // Verificación de duplicado para email
        Optional<Client> existingClientByEmail = clientRepository.findByEmail(client.getEmail());
        if (existingClientByEmail.isPresent()) {
            // Si ya existe un cliente con ese correo electrónico, se agrega un error específico para el correo
            bindingResult.rejectValue("email", "duplicate.email", "El correu electrònic ja està registrat.");
        }

        // Si se encontraron errores, no procedemos a guardar el cliente
        if (bindingResult.hasErrors()) {
            return null; // Si hay errores de validación, no guardamos el cliente
        }

        // Si no hay duplicados, guardamos el cliente
        return clientRepository.save(client);
    }

    /**
     * Obté un client pel seu DNI.
     *
     * @param dni El DNI del client.
     * @return Una instància `Optional` del client.
     */
    public Optional<Client> getClientById(String dni) {
        return clientRepository.findById(dni);
    }

    /**
     * Obté tots els clients de la base de dades.
     *
     * @return Una llista de clients.
     */
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    /**
     * Elimina un client pel seu DNI.
     *
     * @param dni El DNI del client a eliminar.s
     */
    public void deleteClient(String dni) {
        Optional<Client> clientOpt = clientRepository.findById(dni);
        if (clientOpt.isPresent()) {
            clientRepository.delete(clientOpt.get());
        } else {
            throw new ClientNotFoundException("L'agent amb DNI " + dni + " no existeix");
        }

    }

    /**
 * Retorna una llista de clients que exclou els agents.
 *
 * @return Una llista de clients (sense incloure els agents).
 */
public List<Client> getOnlyClients() {
    return clientRepository.findAllClientsExcludingAgents();
}

/**
 * Cerca clients en funció d'un filtre proporcionat. El filtre pot ser parcial i 
 * buscarà coincidències en els camps rellevants (com ara nom, cognoms, correu electrònic, etc.).
 *
 * @param query El filtre de cerca, es convertirà a minúscules per realitzar una cerca insensible a majúscules/minúscules.
 * @return Una llista de clients que coincideixen amb el filtre especificat.
 */
public List<Client> searchClients(String query) {
    return clientRepository.searchByFilters(query.toLowerCase());
}

   
}
