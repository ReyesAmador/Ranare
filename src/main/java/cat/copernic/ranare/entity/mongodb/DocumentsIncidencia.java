/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mongodb;

import jakarta.persistence.Id;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author ngall
 * @version 21/11/2024.1
 */
@Document(collection = "documents_incidencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentsIncidencia {
    
    @Id
    private String id;
    
    private List<String> documents; //Aquí guardarem tant els PDF's com les imatges
    
}
