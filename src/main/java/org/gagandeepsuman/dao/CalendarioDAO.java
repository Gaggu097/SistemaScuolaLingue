package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityCalendarioLezioni;

public class CalendarioDAO extends GenericDAO<EntityCalendarioLezioni, Integer> {

	public EntityCalendarioLezioni salvaCalendario(EntityCalendarioLezioni calendario) {
		return save(calendario);
	}

	public EntityCalendarioLezioni trovaCalendario(int id) {
		return findById(EntityCalendarioLezioni.class, id);
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

/*
public class CalendarioDAO {

	public void salvaCalendario() {
		// TODO - implement CalendarioDAO.salvaCalendario
		throw new UnsupportedOperationException();
	}

	public void trovaCalendario() {
		// TODO - implement CalendarioDAO.trovaCalendario
		throw new UnsupportedOperationException();
	}

	public void aggiornaCalendario() {
		// TODO - implement CalendarioDAO.aggiornaCalendario
		throw new UnsupportedOperationException();
	}

	public void eliminaCalendario() {
		// TODO - implement CalendarioDAO.eliminaCalendario
		throw new UnsupportedOperationException();
	}

}*/