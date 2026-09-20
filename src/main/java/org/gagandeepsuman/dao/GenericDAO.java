package org.gagandeepsuman.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class GenericDAO<T, ID> implements IDAO<T, ID> {

    protected static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Errore durante l'inizializzazione della SessionFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // restituisce ID elemento appena creato
    @Override
    public T save(T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T findById(Class<T> clazz, ID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(clazz, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Esempio con Hibernate / JPA
    @Override
    public List<T> findAll(Class<T> clazz){
    //public <T> List<T> findAll(Class<T> clazz) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM " + clazz.getSimpleName();
            return session.createQuery(hql, clazz).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T update(T entity) {
        Transaction tx = null;
        T updatedEntity = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            updatedEntity = session.merge(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return updatedEntity;
    }

    @Override
    public void delete(T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            T managedEntity = session.contains(entity) ? entity : session.merge(entity);
            session.remove(managedEntity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // cancella cercando per ID dell'elemento
    @Override
    public boolean deleteById(Class<T> clazz, ID id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            T entity = session.find(clazz, id);
            if (entity != null) {
                session.remove(entity);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}