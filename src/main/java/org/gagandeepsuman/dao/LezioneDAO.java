package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityLezione;
import java.util.List;

public class LezioneDAO extends GenericDAO<EntityLezione, Integer> {

	public EntityLezione salvaLezione(EntityLezione lezione) {
		return save(lezione);
	}

	public EntityLezione trovaLezione(int id) {
		return findById(EntityLezione.class, id);
	}

	public List<EntityLezione> trovaLezioni() {
		return findAll(EntityLezione.class);
	}

	public EntityLezione aggiornaLezione(EntityLezione lezione) {
		return update(lezione);
	}

	public void eliminaLezione(EntityLezione lezione) {
		delete(lezione);
	}
	public boolean eliminaLezionePerId(int id) { return deleteById(EntityLezione.class, id); }
}

/*
public class LezioneDAO {

	public void salvaLezione() {
		// TODO - implement LezioneDAO.salvaLezione
		throw new UnsupportedOperationException();
	}

	public void trovaLezione() {
		// TODO - implement LezioneDAO.trovaLezione
		throw new UnsupportedOperationException();
	}

	public void aggiornaLezione() {
		// TODO - implement LezioneDAO.aggiornaLezione
		throw new UnsupportedOperationException();
	}

	public void eliminaLezione() {
		// TODO - implement LezioneDAO.eliminaLezione
		throw new UnsupportedOperationException();
	}

	public void trovaLezioni() {
		// TODO - implement LezioneDAO.trovaLezioni
		throw new UnsupportedOperationException();
	}

}
*/