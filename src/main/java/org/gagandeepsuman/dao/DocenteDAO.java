package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityDocente;

public class DocenteDAO extends GenericDAO<EntityDocente, Integer> {

	public EntityDocente salvaDocente(EntityDocente docente) {
		return save(docente);
	}

	public EntityDocente trovaDocente(int id) {
		return findById(EntityDocente.class, id);
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

/*
public class DocenteDAO {

	public void salvaDocente() {
		// TODO - implement DocenteDAO.salvaDocente
		throw new UnsupportedOperationException();
	}

	public void trovaDocente() {
		// TODO - implement DocenteDAO.trovaDocente
		throw new UnsupportedOperationException();
	}

	public void aggiornaDocente() {
		// TODO - implement DocenteDAO.aggiornaDocente
		throw new UnsupportedOperationException();
	}

	public void eliminaDocente() {
		// TODO - implement DocenteDAO.eliminaDocente
		throw new UnsupportedOperationException();
	}

}
 */