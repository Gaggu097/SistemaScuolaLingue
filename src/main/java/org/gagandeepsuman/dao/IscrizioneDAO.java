package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityIscrizione;

public class IscrizioneDAO extends GenericDAO<EntityIscrizione, Integer> {

	public EntityIscrizione creaIscrizione(EntityIscrizione iscrizione) {
		return save(iscrizione);
	}

	public EntityIscrizione trovaIscrizione(int id) {
		return findById(EntityIscrizione.class, id);
	}

	public EntityIscrizione aggiornaIscrizione(EntityIscrizione iscrizione) {
		return update(iscrizione);
	}

	public void eliminaIscrizione(EntityIscrizione iscrizione) { delete(iscrizione); }
	public boolean eliminaIscrizionePerId(int id) { return deleteById(EntityIscrizione.class, id); }
}

/*
public class IscrizioneDAO {

	public void creaIscrizione() {
		// TODO - implement IscrizioneDAO.creaIscrizione
		throw new UnsupportedOperationException();
	}

	public void trovaIscrizione() {
		// TODO - implement IscrizioneDAO.trovaIscrizione
		throw new UnsupportedOperationException();
	}

	public void aggiornaIscrizione() {
		// TODO - implement IscrizioneDAO.aggiornaIscrizione
		throw new UnsupportedOperationException();
	}

	public void eliminaIscrizione() {
		// TODO - implement IscrizioneDAO.eliminaIscrizione
		throw new UnsupportedOperationException();
	}

}
*/