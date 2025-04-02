package org.grupp2.sdpproject.Utils;

import org.grupp2.sdpproject.dao.FilmDAO;
import org.grupp2.sdpproject.dao.GenericDAO;
import org.grupp2.sdpproject.entities.Film;
import org.hibernate.SessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAOManager {

    private final SessionFactory sessionFactory;
    private final Map<Class<?>, GenericDAO<?>> daoMap = new HashMap<>();

    public DAOManager() {
        this.sessionFactory = HibernateUtil.getSessionFactory();

        // Register non-generic entities here
        daoMap.put(Film.class, new FilmDAO(sessionFactory));
        //...
    }

    // Universal method to get a DAO class for the given entity class
    @SuppressWarnings("unchecked")
    private <T> GenericDAO<T> getDao(Class<T> entityClass) {
        // If we don't have a specific DAO generate a generic one
        return (GenericDAO<T>) daoMap.computeIfAbsent(
                entityClass,
                c -> new GenericDAO<>(c, sessionFactory)
        );
    }

    public <T> void save(T entity) {
        getDao(entity.getClass()).save(entity);
    }

    public <T> T findById(Class<T> entityClass, Object id) {
        return getDao(entityClass).findById(id);
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        return getDao(entityClass).findAll();
    }

    public <T> void update(T entity) {
        getDao(entity.getClass()).update(entity);
    }

    public <T> void delete(T entity) {
        getDao(entity.getClass()).delete(entity);
    }
}
