package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityPagamento;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
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

	public EntityPagamento trovaPagamentoPerIscrizione(int idIscrizione) {
		try (Session session = sessionFactory.openSession()) {
			String hql = "FROM EntityPagamento p WHERE p.fkIdIscrizione = :idIscrizione";
			return session.createQuery(hql, EntityPagamento.class)
					.setParameter("idIscrizione", idIscrizione)
					.getSingleResult();
		}
	}

//	public EntityPagamento trovaPagamentoPerIscrizione(int idIscrizione) {
//		String hql = "FROM EntityPagamento p WHERE p.fkIdIscrizione = :idIscrizione";
//		try(Session session = sessionFactory.openSession()) {
//
//			return session.createQuery(hql, EntityPagamento.class)
//					.setParameter("idIscrizione", idIscrizione)
//					.uniqueResult();
//		}
//		catch (Exception e) {
//			if (tx != null) tx.rollback();
//			e.printStackTrace();
//			return null;
//		}
//	}

}