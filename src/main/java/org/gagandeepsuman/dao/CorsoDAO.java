package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityCorso;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CorsoDAO extends GenericDAO<EntityCorso, Integer> {

	public EntityCorso salvaCorso(EntityCorso corso) {
		return save(corso);
	}

	public EntityCorso trovaCorso(int id) {
		return findById(EntityCorso.class, id);
	}

	public List<EntityCorso> trovaTuttiCorsi() {
		return findAll(EntityCorso.class);
	}

	public EntityCorso trovaPerLinguaELivello(String lingua, String livello) {
		try (Session session = sessionFactory.openSession()) {
			String hql = "FROM EntityCorso c WHERE c.linguaCorso = :lingua AND c.livelloCorso = :livello";
			Query<EntityCorso> query = session.createQuery(hql, EntityCorso.class);
			query.setParameter("lingua", lingua);
			query.setParameter("livello", livello);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public EntityCorso aggiornaCorso(EntityCorso corso) {
		return update(corso);
	}

	public void eliminaCorso(EntityCorso corso) {
		delete(corso);
	}

	public boolean eliminaCorsoPerId(int id) {
		return deleteById(EntityCorso.class, id);
	}
}