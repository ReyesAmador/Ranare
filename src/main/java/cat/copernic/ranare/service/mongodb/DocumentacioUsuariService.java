/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mongodb;

import cat.copernic.ranare.entity.mongodb.DocumentacioUsuari;
import cat.copernic.ranare.enums.DocumentState;
import cat.copernic.ranare.repository.mongodb.DocumentacioUsuariRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Raú
 */
public class DocumentacioUsuariService {

    @Autowired
    DocumentacioUsuariRepository documentacioUsuariRepository;

    public List<DocumentacioUsuari> obtenirDocumentsActiusPerUsuari(String userId) {
        return documentacioUsuariRepository.findByUserIdAndDocumentState(userId, DocumentState.ACTIVA);
    }

    public void afegirDocument(String userId, String documentType, MultipartFile frontFile, MultipartFile backFile) {
        // Marcar el documento previo como caducado si existe
        List<DocumentacioUsuari> documentsActius = documentacioUsuariRepository.findByUserIdAndDocumentState(userId, DocumentState.ACTIVA);
        for (DocumentacioUsuari doc : documentsActius) {
            if (doc.getDocumentType().equals(documentType)) {
                doc.setDocumentState(DocumentState.CADUCADA);
                repository.save(doc);
            }
        }

        // Crear nuevo documento activo
        DocumentacioUsuari nouDocument = new DocumentacioUsuari();
        nouDocument.setUserId(userId);
        nouDocument.setDocumentType(documentType);
        nouDocument.setDocumentState(DocumentState.ACTIVA);
        nouDocument.setFrontUrl(guardarFitxer(frontFile));
        nouDocument.setBackUrl(guardarFitxer(backFile));
        nouDocument.setCreationDate(LocalDate.now());
        repository.save(nouDocument);
    }
}
