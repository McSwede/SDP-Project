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

    public <T> T findByIdWithJoinFetch(Class<T> entityClass, Object id, List<String> joinFetchProperties) {
        try (Session session = sessionFactory.openSession()) {
            // First query to fetch the main entity
            StringBuilder queryBuilder = new StringBuilder("SELECT e FROM ");
            queryBuilder.append(entityClass.getSimpleName()).append(" e ");
            queryBuilder.append("WHERE e.id = :id");

            // Fetch the main entity first
            T entity = session.createQuery(queryBuilder.toString(), entityClass)
                    .setParameter("id", id)
                    .uniqueResult();

            // Fetch each property (collection) individually if needed
            for (String joinProp : joinFetchProperties) {
                String joinQuery = "SELECT e FROM " + entityClass.getSimpleName() + " e LEFT JOIN FETCH e." + joinProp + " WHERE e.id = :id";
                session.createQuery(joinQuery, entityClass)
                        .setParameter("id", id)
                        .uniqueResult();
            }

            return entity;
        }
    }

    /**
     * Retrieves a paginated list of entities.
     * @param offset The starting index for pagination
     * @param limit The number of entities to fetch
     * @return A list of entities
     */
    public List<T> findPaginated(int offset, int limit) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM " + entityClass.getName(), entityClass)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .list();
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
