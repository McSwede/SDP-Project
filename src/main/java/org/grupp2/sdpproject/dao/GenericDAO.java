package org.grupp2.sdpproject.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

public class GenericDAO<T> {

    protected final Class<T> entityClass;
    protected final SessionFactory sessionFactory;

    public GenericDAO(Class<T> entityClass, SessionFactory sessionFactory) {
        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }

    public T findById(Object id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(entityClass, id);
        }
    }

    public List<T> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM " + entityClass.getName(), entityClass).list();
        }
    }

    public void save(Object entity) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                try {
                    tx.rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (session.isOpen()) session.close();
        }
    }

    public void update(Object entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void delete(Object entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public List<T> findByField(String fieldName, Object value) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "from " + entityClass.getSimpleName() + " where " + fieldName + " = :value";
            return session.createQuery(hql, entityClass)
                    .setParameter("value", value)
                    .list();
        }
    }

    public List<T> findByFields(Map<String, Object> fields) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("from " + entityClass.getSimpleName() + " where ");
            int index = 0;
            for (String key : fields.keySet()) {
                if (index++ > 0) hql.append(" and ");
                hql.append(key).append(" = :").append(key);
            }

            Query<T> query = session.createQuery(hql.toString(), entityClass);
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            return query.list();
        }
    }
}
