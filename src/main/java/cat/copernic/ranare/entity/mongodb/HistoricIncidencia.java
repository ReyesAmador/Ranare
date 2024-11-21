/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.entity.mongodb;

import jakarta.persistence.Id;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author ngall
 * @version 21/11/2024.1
 */
@Document(collection = "historical_incidents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricIncidencia {
    
    @Id
    private String id;
    
    @Field("vehicle_id")
    private String vehicleId;
    
    @Field("incidents")
    private List<Incident> incidents;
    
    //Classe interna per definir una incidència
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Incident{
        
        @Field("estat")
        private String estat;
        
        @Field("motiu")
        private String motiu;
        
        @Field("cost")
        private double cost;
        
        @Field("data_inici")
        private Date dataInici;
        
        @Field("data_final")
        private Date dataFinal;
        
        @Field("documents")
        private List<String> documents;
    }
    
    
}
