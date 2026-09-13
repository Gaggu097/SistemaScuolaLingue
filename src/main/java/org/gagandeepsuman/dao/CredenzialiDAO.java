package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityCredenziali;
import org.hibernate.Session;

public class CredenzialiDAO extends GenericDAO<EntityCredenziali, Integer> {

	public EntityCredenziali salvaCredenziali(EntityCredenziali credenziali) {
		return save(credenziali);
	}

	public EntityCredenziali trovaCredenziali(int id) {
		return findById(EntityCredenziali.class, id);
	}

	public EntityCredenziali aggiornaCredenziali(EntityCredenziali credenziali) {
		return update(credenziali);
	}

	public void eliminaCredenziali(EntityCredenziali credenziali) {
		delete(credenziali);
	}

	public boolean eliminaCredenzialiPerId(int id) {
		return deleteById(EntityCredenziali.class, id);
	}

	public static boolean esisteUsername(String username) {
		try (Session session = sessionFactory.openSession()) {
			String hql = "SELECT COUNT(c) FROM EntityCredenziali c WHERE c.username = :username";
			Long count = session.createQuery(hql, Long.class)
					.setParameter("username", username)
					.getSingleResult();
			return count > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
