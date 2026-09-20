package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityCalendarioLezioni;

public class CalendarioDAO extends GenericDAO<EntityCalendarioLezioni, Integer> {

	public EntityCalendarioLezioni salvaCalendario(EntityCalendarioLezioni calendario) {
		return save(calendario);
	}

	public EntityCalendarioLezioni trovaCalendario(int id) {
		return findById(EntityCalendarioLezioni.class, id);
	}

	public EntityCalendarioLezioni trovaCalendarioPerCorso(int idCorso) {
		try (org.hibernate.Session session = sessionFactory.openSession()) {
			String hql = "FROM EntityCalendarioLezioni c WHERE c.corsoIdCorso = :idCorso";
			java.util.List<EntityCalendarioLezioni> lista = session.createQuery(hql, EntityCalendarioLezioni.class)
					.setParameter("idCorso", idCorso)
					.getResultList();
			if (!lista.isEmpty()) {
				return lista.get(0);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public EntityCalendarioLezioni aggiornaCalendario(EntityCalendarioLezioni calendario) {
		return update(calendario);
	}

	public void eliminaCalendario(EntityCalendarioLezioni calendario) {
		delete(calendario);
	}
	public boolean eliminaCalendarioPerId(int id) { return deleteById(EntityCalendarioLezioni.class, id);
	}
}