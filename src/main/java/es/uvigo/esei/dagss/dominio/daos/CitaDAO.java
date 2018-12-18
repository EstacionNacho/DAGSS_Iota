/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Cita;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;


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
}
