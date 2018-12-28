/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Cita;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

//Imports nuevos
import java.util.List;
import java.util.Date;

@Stateless
@LocalBean
public class CitaDAO  extends GenericoDAO<Cita>{    

    // Completar aqui
    
    public Cita buscarPorId(Long id) {
        
        TypedQuery<Cita> q = em.createQuery("SELECT c FROM Cita AS c"
                + " WHERE c.id = :id", Cita.class);
        
        q.setParameter("id", id);
        
        return q.getSingleResult();
    }
    
    public List<Cita> buscarCitasPorMedicoDia(Long medicoId, Date fecha) {
       
        TypedQuery<Cita> q = em.createQuery("SELECT c FROM Cita AS c WHERE c.fecha = :fecha AND c.medico.id = :medicoId", Cita.class);
        q.setParameter("fecha", fecha); 
        q.setParameter("medicoId", medicoId);
        
        return q.getResultList();
    }
    //Metodo para pruebas, usar el anterior
    public List<Cita> buscarCitasPorMedico(Long medicoId) {
       
        TypedQuery<Cita> q = em.createQuery("SELECT c FROM Cita AS c WHERE c.medico.id = :medicoId", Cita.class);      
        q.setParameter("medicoId", medicoId);
        
        return q.getResultList();
    }    
    
    public void actualizarEstadoCita(Cita cita){
        em.merge(cita);  
}
}
