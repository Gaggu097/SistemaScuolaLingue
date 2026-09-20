package org.gagandeepsuman.dao;

import org.springframework.stereotype.Repository;

import org.gagandeepsuman.entity.EntityClasse;
import java.util.List;

@Repository
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