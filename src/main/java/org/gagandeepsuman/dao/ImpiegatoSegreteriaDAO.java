package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityImpiegatoSegreteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImpiegatoSegreteriaDAO extends GenericDAO<EntityImpiegatoSegreteria, Integer> {

    public EntityImpiegatoSegreteria salvaImpiegatoSegreteria(EntityImpiegatoSegreteria impiegato) {
        return save(impiegato);
    }

    public EntityImpiegatoSegreteria trovaImpiegatoSegreteria(int id) {
        return findById(EntityImpiegatoSegreteria.class, id);
    }

    public EntityImpiegatoSegreteria aggiornaImpiegatoSegreteria(EntityImpiegatoSegreteria impiegato) {
        return update(impiegato);
    }

    public void eliminaImpiegatoSegreteria(EntityImpiegatoSegreteria impiegato) {
        delete(impiegato);
    }

    public boolean eliminaImpiegatoSegreteriaPerId(int id) {
        return deleteById(EntityImpiegatoSegreteria.class, id);
    }

    /**
     * Find an impiegato segreteria by username
     * @param username the username to search for
     * @return EntityImpiegatoSegreteria if found, null otherwise
     */
    public EntityImpiegatoSegreteria findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM EntityImpiegatoSegreteria i WHERE i.username = :username";
            return session.createQuery(hql, EntityImpiegatoSegreteria.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if username already exists
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    public boolean esisteUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(i) FROM EntityImpiegatoSegreteria i WHERE i.username = :username";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<EntityImpiegatoSegreteria> trovaTuttiImpiegatiSegreteria() {
        return findAll(EntityImpiegatoSegreteria.class);
    }
}