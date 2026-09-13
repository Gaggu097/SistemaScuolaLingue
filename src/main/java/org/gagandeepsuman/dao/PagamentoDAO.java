package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityPagamento;

public class PagamentoDAO extends GenericDAO<EntityPagamento, Integer> {

	public EntityPagamento salvaPagamento(EntityPagamento pagamento) {
		return save(pagamento);
	}

	public EntityPagamento trovaPagamento(int id) {
		return findById(EntityPagamento.class, id);
	}

	public EntityPagamento aggiornaPagamento(EntityPagamento pagamento) {
		return update(pagamento);
	}

	public void eliminaPagamento(EntityPagamento pagamento) {
		delete(pagamento);
	}
	public boolean eliminaPagamentoPerId(int id) {
		return deleteById(EntityPagamento.class, id);
	}
}

/*
public class PagamentoDAO {

	public void salvaPagamento() {
		// TODO - implement PagamentoDAO.salvaPagamento
		throw new UnsupportedOperationException();
	}

	public void trovaPagamento() {
		// TODO - implement PagamentoDAO.trovaPagamento
		throw new UnsupportedOperationException();
	}

	public void aggiornaPagamento() {
		// TODO - implement PagamentoDAO.aggiornaPagamento
		throw new UnsupportedOperationException();
	}

	public void eliminaPagamento() {
		// TODO - implement PagamentoDAO.eliminaPagamento
		throw new UnsupportedOperationException();
	}

}
 */