/*
 Proyecto Java EE, DAGSS-2016
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

//Imports nuevos
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import java.util.Date;
import java.util.List;

@Stateless
@LocalBean
public class PrescripcionDAO extends GenericoDAO<Prescripcion> {

    public Prescripcion buscarPorIdConRecetas(Long id) {
        TypedQuery<Prescripcion> q = em.createQuery("SELECT p FROM Prescripcion AS p JOIN FETCH p.recetas"
                                                  + "  WHERE p.id = :id", Prescripcion.class);
        q.setParameter("id", id);
        
        return q.getSingleResult();
    }
    
    // Completar aqui  
    
    public List<Prescripcion> buscarPorPaciente(String paciente){
        
        TypedQuery<Prescripcion> q = em.createQuery("SELECT p FROM Prescripcion AS p WHERE p.paciente.numeroTarjetaSanitaria = :paciente", Prescripcion.class);      
        q.setParameter("paciente", paciente);
        
        return q.getResultList();
    }
    
    public List<Prescripcion> buscarPorPaciente(String paciente, Date fecha){
        
        TypedQuery<Prescripcion> q = em.createQuery("SELECT p FROM Prescripcion AS p WHERE p.paciente.numeroTarjetaSanitaria = :paciente AND p.fechaFin >= :fecha", Prescripcion.class);      
        q.setParameter("paciente", paciente);
        q.setParameter("fecha", fecha);
        
        return q.getResultList();
    }
}
