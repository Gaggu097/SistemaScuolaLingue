package org.gagandeepsuman.dao;

import org.gagandeepsuman.entity.EntityIscrizione;
import org.hibernate.Session;

import java.util.List;

public class IscrizioneDAO extends GenericDAO<EntityIscrizione, Integer> {

	public EntityIscrizione creaIscrizione(EntityIscrizione iscrizione) {
		return save(iscrizione);
	}

	public boolean creaIscrizioneTransazionale(EntityIscrizione iscrizione, org.gagandeepsuman.entity.EntityPagamento pagamento, org.gagandeepsuman.entity.EntityCorso corso) {
		org.hibernate.Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			
			// 1. Salva Iscrizione
			session.persist(iscrizione);
			
			// 2. Imposta l'ID dell'iscrizione salvata nel Pagamento e salvalo
			pagamento.setFkIdIscrizione(iscrizione.getID());
			session.persist(pagamento);
			
			// 3. Aggiorna il numero iscritti del Corso
			corso.setNumIscritti(corso.getNumIscritti() + 1);
			session.merge(corso);
			
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			e.printStackTrace();
			return false;
		}
	}
	public EntityIscrizione trovaIscrizione(int id) {
		return findById(EntityIscrizione.class, id);
	}

	public EntityIscrizione findByIds(int idCliente, int idCorso) {
		try (Session session = sessionFactory.openSession()) {
			// HQL usa il nome della proprietà Java (es. deletedAt)
			String hql = "FROM EntityIscrizione i WHERE i.FKidCorso = :idCorso AND i.FKidCliente = :idCliente AND i.deletedAt IS NULL";

			return session.createQuery(hql, EntityIscrizione.class)
					.setParameter("idCorso", idCorso)
					.setParameter("idCliente", idCliente)
					.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<EntityIscrizione> trovaTutti() {
		try (Session session = sessionFactory.openSession()) {
			// HQL usa il nome della proprietà Java (es. deletedAt)
			String hql = "FROM EntityIscrizione i WHERE i.deletedAt IS NULL";

			return session.createQuery(hql, EntityIscrizione.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Restituisce tutte le iscrizioni non annullate di un determinato anno accademico.
	 * Il formato atteso è "AAAA-AAAA" es. "2025-2026".
	 */
	public List<EntityIscrizione> trovaPerAnnoAccademico(String annoAccademico) {
		try (Session session = sessionFactory.openSession()) {
			String hql = "FROM EntityIscrizione i WHERE i.annoAccademico = :anno AND i.deletedAt IS NULL";
			return session.createQuery(hql, EntityIscrizione.class)
					.setParameter("anno", annoAccademico)
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public EntityIscrizione aggiornaIscrizione(EntityIscrizione iscrizione) {
		return update(iscrizione);
	}

	public void eliminaIscrizione(EntityIscrizione iscrizione) { delete(iscrizione); }
	public boolean eliminaIscrizionePerId(int id) { return deleteById(EntityIscrizione.class, id); }

	/**
	 * Restituisce tutte le iscrizioni non annullate di un determinato corso.
	 */
	public List<EntityIscrizione> trovaPerCorso(int idCorso) {
		try (Session session = sessionFactory.openSession()) {
			String hql = "FROM EntityIscrizione i WHERE i.FKidCorso = :idCorso AND i.deletedAt IS NULL";
			return session.createQuery(hql, EntityIscrizione.class)
					.setParameter("idCorso", idCorso)
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}