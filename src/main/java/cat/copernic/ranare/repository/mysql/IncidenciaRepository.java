/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.repository.mysql;

import cat.copernic.ranare.entity.mysql.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ngall
 */
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long>{
}
