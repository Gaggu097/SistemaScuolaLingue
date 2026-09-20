package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityCliente;
import org.springframework.stereotype.Repository;

@Repository
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
