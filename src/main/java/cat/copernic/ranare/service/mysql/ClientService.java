/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.repository.mysql.ClientRepository;
import exceptions.DuplicateResourceException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
     * @return `true` si l'operació és correcta, `false` si falla per
     * duplicació.
     */
    public Client saveClient(Client client, boolean isUpdate) {
        // Si es una operación de actualización, aseguramos que no se modifique el DNI
        if (isUpdate) {
            Optional<Client> existingClient = clientRepository.findById(client.getDni());
            if (existingClient.isPresent() && !existingClient.get().getDni().equals(client.getDni())) {
                throw new DuplicateResourceException("El DNI ja està registrat.", "update", client.getDni());
            }
        } else {
            // Para creación, primero comprobamos si el DNI ya está registrado
            Optional<Client> existingClient = clientRepository.findById(client.getDni());
            if (existingClient.isPresent()) {
                throw new DuplicateResourceException("El DNI ja està registrat.", "create", client.getDni());
            }
        }

        try {
            // Guardamos el cliente en la base de datos
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            // Si hay un error de integridad, lo capturamos y lo gestionamos
            String errorMessage;
            if (e.getMessage().contains("dni")) {
                errorMessage = "El DNI ja està registrat.";
            } else if (e.getMessage().contains("email")) {
                errorMessage = "El correu electrònic ja està registrat.";
            } else {
                errorMessage = "Un camp únic ja està duplicat.";
            }
            throw new DuplicateResourceException(errorMessage, isUpdate ? "update" : "create", client.getDni());
        }
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
     * @param dni El DNI del client a eliminar.
     */
    public void deleteClient(String dni) {
        clientRepository.deleteById(dni);
    }
}
