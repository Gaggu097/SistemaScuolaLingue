package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityLezione;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
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