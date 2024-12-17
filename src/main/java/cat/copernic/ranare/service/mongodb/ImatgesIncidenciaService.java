/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mongodb;

import cat.copernic.ranare.repository.mongodb.ImatgesIncidenciaRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ngall
 */
@Service
public class ImatgesIncidenciaService {
    
    @Autowired
    private ImatgesIncidenciaRepository imatgesIncidenciaRepository;
    
    public List<String> storeImages(MultipartFile[] imatges) throws IOException {
        List<String> imatgesIds = new ArrayList<>();
        
        if (imatges != null && imatges.length > 0) {
            for (MultipartFile imatge : imatges) {
                String imatgeId = imatgesIncidenciaRepository.storeImage(imatge);
                imatgesIds.add(imatgeId);
            }
        }
        
        return imatgesIds;
    }
}
