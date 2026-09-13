package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityCorso;
import java.util.List;

public class CorsoDAO extends GenericDAO<EntityCorso, Integer> {

	public EntityCorso salvaCorso(EntityCorso corso) {
		return save(corso);
	}

	public EntityCorso trovaCorso(int id) {
		return findById(EntityCorso.class, id);
	}

	public List<EntityCorso> trovaCorsi() {
		return findAll(EntityCorso.class);
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

/*
public class CorsoDAO {

	public void salvaCorso() {
		// TODO - implement CorsoDAO.salvaCorso
		throw new UnsupportedOperationException();
	}

	public void trovaCorso() {
		// TODO - implement CorsoDAO.trovaCorso
		throw new UnsupportedOperationException();
	}

	public void trovaCorsi() {
		// TODO - implement CorsoDAO.trovaCorsi
		throw new UnsupportedOperationException();
	}

	public void aggiornaCorso() {
		// TODO - implement CorsoDAO.aggiornaCorso
		throw new UnsupportedOperationException();
	}

	public void eliminaCorso() {
		// TODO - implement CorsoDAO.eliminaCorso
		throw new UnsupportedOperationException();
	}

}
 */