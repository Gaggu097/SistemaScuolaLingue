package org.gagandeepsuman.dao;
import org.gagandeepsuman.entity.EntityCliente;

public class ClienteDAO extends GenericDAO<EntityCliente, Integer> {

	public EntityCliente salvaCliente(EntityCliente cliente) {
		return save(cliente);
	}

	public EntityCliente trovaCliente(int id) {
		return findById(EntityCliente.class, id);
	}

	public EntityCliente aggiornaCliente(EntityCliente cliente) {
		return update(cliente);
	}

	public void eliminaCliente(EntityCliente cliente) {
		delete(cliente);
	}

	public boolean eliminaClientePerId(int id) {
		return deleteById(EntityCliente.class, id);
	}
}
/*
public class ClienteDAO {


	public void salvaCliente() {
		// TODO - implement ClienteDAO.salvaCliente
		throw new UnsupportedOperationException();
	}

	public EntityCliente trovaCliente() {
		// TODO - implement ClienteDAO.trovaCliente
		throw new UnsupportedOperationException();
	}

	public void aggiornaCliente() {
		// TODO - implement ClienteDAO.aggiornaCliente
		throw new UnsupportedOperationException();
	}

	public void eliminaCliente() {
		// TODO - implement ClienteDAO.eliminaCliente
		throw new UnsupportedOperationException();
	}

}
*/