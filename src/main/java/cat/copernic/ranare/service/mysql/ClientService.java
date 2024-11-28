/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mysql;

import cat.copernic.ranare.entity.mysql.Client;
import cat.copernic.ranare.repository.mysql.ClientRepository;
import cat.copernic.ranare.exceptions.DuplicateResourceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

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

    /*
    public Client saveClient(Client client, boolean isUpdate, BindingResult bindingResult) {
    // Verificación de duplicados sin interrumpir el flujo
    List<String> errorMessages = new ArrayList<>();

    // Verificación de duplicado para DNI
    if (isUpdate) {
        Optional<Client> existingClientByDni = clientRepository.findById(client.getDni());
        if (existingClientByDni.isPresent() && !existingClientByDni.get().getDni().equals(client.getDni())) {
            errorMessages.add("El DNI ja està registrat.");
        }
    } else {
        Optional<Client> existingClientByDni = clientRepository.findById(client.getDni());
        if (existingClientByDni.isPresent()) {
            errorMessages.add("El DNI ja està registrat.");
        }
    }

    // Verificación de duplicado para email
    Optional<Client> existingClientByEmail = clientRepository.findByEmail(client.getEmail());
    if (existingClientByEmail.isPresent()) {
        errorMessages.add("El correu electrònic ja està registrat.");
    }

    // Si se encontraron errores, los agregamos al bindingResult
    if (!errorMessages.isEmpty()) {
        for (String errorMessage : errorMessages) {
            bindingResult.rejectValue("email", "duplicate.email", errorMessage);  // Para email
            bindingResult.rejectValue("dni", "duplicate.dni", errorMessage);  // Para DNI
        }
        return null; // Para evitar que el cliente se guarde si hay errores
    }

    // Si no hay duplicados, guardamos el cliente
    return clientRepository.save(client);
} */
 /*
    public Client saveClient(Client client, boolean isUpdate, BindingResult bindingResult) {
    // Verificación de duplicados sin interrumpir el flujo
    List<String> errorMessages = new ArrayList<>();

    // Verificación de duplicado para DNI
    if (isUpdate) {
        Optional<Client> existingClientByDni = clientRepository.findById(client.getDni());
        if (existingClientByDni.isPresent() && !existingClientByDni.get().getDni().equals(client.getDni())) {
            bindingResult.rejectValue("dni", "duplicate.dni", "El DNI ja està registrat.");
        }
    } else {
        Optional<Client> existingClientByDni = clientRepository.findById(client.getDni());
        if (existingClientByDni.isPresent()) {
            bindingResult.rejectValue("dni", "duplicate.dni", "El DNI ja està registrat.");
        }
    }

    // Verificación de duplicado para email
    Optional<Client> existingClientByEmail = clientRepository.findByEmail(client.getEmail());
    if (existingClientByEmail.isPresent()) {
        bindingResult.rejectValue("email", "duplicate.email", "El correu electrònic ja està registrat.");
    }

    // Si hay errores, devolvemos el formulario con los errores
    if (bindingResult.hasErrors()) {
        return null; // Para evitar que se guarde el cliente si hay errores
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
        clientRepository.deleteById(dni);
    }
}
