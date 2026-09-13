package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityClasse;
import java.util.List;

public class ClasseDAO extends GenericDAO<EntityClasse, Integer> {

	public EntityClasse salvaClasse(EntityClasse classe) {
		return save(classe);
	}

	public EntityClasse trovaClasse(int id) {
		return findById(EntityClasse.class, id);
	}

	public List<EntityClasse> trovaClassi() {
		return findAll(EntityClasse.class);
	}

	public EntityClasse aggiornaClasse(EntityClasse classe) {
		return update(classe);
	}

	public void eliminaClasse(EntityClasse classe) {
		delete(classe);
	}
	public boolean eliminaClassePerId(int id) { return deleteById(EntityClasse.class, id);
	}
}

/*
public class ClasseDAO {

	public void salvaClasse() {
		// TODO - implement ClasseDAO.salvaClasse
		throw new UnsupportedOperationException();
	}

	public void trovaClasse() {
		// TODO - implement ClasseDAO.trovaClasse
		throw new UnsupportedOperationException();
	}

	public void trovaClassi() {
		// TODO - implement ClasseDAO.trovaClassi
		throw new UnsupportedOperationException();
	}

	public void aggiornaClasse() {
		// TODO - implement ClasseDAO.aggiornaClasse
		throw new UnsupportedOperationException();
	}

	public void eliminaClasse() {
		// TODO - implement ClasseDAO.eliminaClasse
		throw new UnsupportedOperationException();
	}

}
*/