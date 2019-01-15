/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Receta;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

//Imports nuevos
import java.util.List;
import java.util.Date;

@Stateless
@LocalBean
public class RecetaDAO extends GenericoDAO<Receta>{
    
    public List<Receta> buscarPorIdPacienteConPrescripcion(Long id, Date fecha) {
        
        TypedQuery<Receta> q = em.createQuery("SELECT r FROM Receta AS r"
                + " WHERE r.prescripcion.paciente.id = :id AND r.prescripcion.fechaFin >= :fecha", Receta.class);
        
        q.setParameter("id", id);
        q.setParameter("fecha", fecha); 
        
        return q.getResultList();
    }
}
