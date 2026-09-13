package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityCredenziali;

public class CredenzialiDAO extends GenericDAO<EntityCredenziali, Integer> {

	public EntityCredenziali salvaCredenziali(EntityCredenziali credenziali) {
		return save(credenziali);
	}

	public EntityCredenziali trovaCredenziali(int id) {
		return findById(EntityCredenziali.class, id);
	}

	public EntityCredenziali aggiornaCredenziali(EntityCredenziali credenziali) {
		return update(credenziali);
	}

	public void eliminaCredenziali(EntityCredenziali credenziali) {
		delete(credenziali);
	}

	public boolean eliminaCredenzialiPerId(int id) {
		return deleteById(EntityCredenziali.class, id);
	}
}

/*
public class CredenzialiDAO {

	public void salvaPagamento() {
		// TODO - implement CredenzialiDAO.salvaPagamento
		throw new UnsupportedOperationException();
	}

	public void trovaPagamento() {
		// TODO - implement CredenzialiDAO.trovaPagamento
		throw new UnsupportedOperationException();
	}

	public void aggiornaPagamento() {
		// TODO - implement CredenzialiDAO.aggiornaPagamento
		throw new UnsupportedOperationException();
	}

	public void eliminaPagamento() {
		// TODO - implement CredenzialiDAO.eliminaPagamento
		throw new UnsupportedOperationException();
	}

}
 */