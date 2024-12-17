/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mongodb;

import cat.copernic.ranare.entity.mongodb.DocumentacioUsuari;
import cat.copernic.ranare.enums.DocumentState;
import cat.copernic.ranare.enums.DocumentType;
import cat.copernic.ranare.repository.mongodb.DocumentacioUsuariRepository;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Raú
 */
@Service
public class DocumentacioUsuariService {

    @Autowired
    DocumentacioUsuariRepository documentacioUsuariRepository;

    public List<DocumentacioUsuari> obtenirDocumentsActiusPerUsuari(String userId) {
        return documentacioUsuariRepository.findByUserIdAndDocumentState(userId, DocumentState.ACTIVA);
    }

    public List<DocumentacioUsuari> obtenirHistoricDocuments(String userId) {
        return documentacioUsuariRepository.findByUserIdAndDocumentState(userId, DocumentState.CADUCADA);
    }

    public List<DocumentacioUsuari> obtenirDocumentsPerEstat(String userId, DocumentState estat) {
        return documentacioUsuariRepository.findByUserIdAndDocumentState(userId, estat);
    }

    public void afegirDocument(String userId, String documentType, MultipartFile frontFile, MultipartFile backFile) {
        DocumentType docType = DocumentType.valueOf(documentType.toUpperCase()); // Convertir a Enum

        // Marcar documentos previos del mismo tipo como caducados
        List<DocumentacioUsuari> documentsActius = documentacioUsuariRepository.findByUserIdAndDocumentState(userId, DocumentState.ACTIVA);
        for (DocumentacioUsuari doc : documentsActius) {
            if (doc.getDocumentType() == docType) {
                doc.setDocumentState(DocumentState.CADUCADA);
                documentacioUsuariRepository.save(doc);
            }
        }

        // Crear un nuevo documento activo
        DocumentacioUsuari nouDocument = new DocumentacioUsuari();
        nouDocument.setUserId(userId);
        nouDocument.setDocumentType(docType);
        nouDocument.setDocumentState(DocumentState.ACTIVA);
        nouDocument.setFrontUrl(guardarFitxer(frontFile));
        nouDocument.setBackUrl(guardarFitxer(backFile));
        nouDocument.setCreationDate(LocalDateTime.now());
        documentacioUsuariRepository.save(nouDocument);
    }

    public String guardarFitxer(MultipartFile fitxer) {
        try {
            String uploadDir = "uploads/documents";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Crear archivo y guardar
            String nomFitxer = System.currentTimeMillis() + "_" + fitxer.getOriginalFilename();
            File destinationFile = new File(dir, nomFitxer);
            fitxer.transferTo(destinationFile);

            // Retornar la ruta del archivo
            return destinationFile.getPath();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el fitxer: " + fitxer.getOriginalFilename(), e);
        }
    }
}
