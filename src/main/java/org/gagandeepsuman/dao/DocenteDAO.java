package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityCorso;
import org.gagandeepsuman.entity.EntityDocente;

import java.util.List;

public class DocenteDAO extends GenericDAO<EntityDocente, Integer> {

	public EntityDocente salvaDocente(EntityDocente docente) {
		return save(docente);
	}

	public EntityDocente trovaDocente(int id) {
		return findById(EntityDocente.class, id);
	}

	public List<EntityDocente> trovaTuttiDocenti() {
		return findAll(EntityDocente.class);
	}

	public EntityDocente aggiornaDocente(EntityDocente docente) {
		return update(docente);
	}

	public void eliminaDocente(EntityDocente docente) {
		delete(docente);
	}
	public boolean eliminaDocentePerId(int id) {
		return deleteById(EntityDocente.class, id);
	}
}