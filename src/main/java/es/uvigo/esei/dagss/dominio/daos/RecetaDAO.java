/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Receta;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

//Imporst nuevo
import java.util.List;

@Stateless
@LocalBean
public class RecetaDAO extends GenericoDAO<Receta>{
 
    public List<Receta> buscarPorIdPacienteConPrescripcion(Long id) {
        
        TypedQuery<Receta> q = em.createQuery("SELECT r FROM Receta INNER JOIN Prescripcion ON Receta.prescripcion_id = prescripcion.id WHERE Prescripcion.paciente_id = :id", Receta.class);
        q.setParameter("id", id);
        
        return q.getResultList();
    }
}
