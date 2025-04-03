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
        // Automatically get sessionFactory
        this.sessionFactory = HibernateUtil.getSessionFactory();

        // Register non-generic entities here
        daoMap.put(Film.class, new FilmDAO(sessionFactory));
        //...
    }

    /**
     * Retrieves a DAO for the specified entity class. If the class is not
     * already mapped to a DAO, a new GenericDAO is created and stored.
     *
     * @param entityClass The class of the entity
     * @return A DAO instance for the given entity class
     */
    @SuppressWarnings("unchecked")
    private <T> GenericDAO<T> getDao(Class<T> entityClass) {
        // If we don't have a specific DAO generate a generic one
        return (GenericDAO<T>) daoMap.computeIfAbsent(
                entityClass,
                c -> new GenericDAO<>(c, sessionFactory)
        );
    }

    /**
     * Saves an entity to the database using the appropriate DAO.
     *
     * @param entity The entity instance to be saved
     */
    public <T> void save(T entity) {
        getDao(entity.getClass()).save(entity);
    }

    /**
     * Finds an entity by its ID using the appropriate DAO.
     *
     * @param entityClass The class of the entity to find
     * @param id          The primary key of the entity
     * @return The found entity or null if no matching entity exists
     */
    public <T> T findById(Class<T> entityClass, Object id) {
        return getDao(entityClass).findById(id);
    }

    /**
     * Retrieves all entities of the specified class using the appropriate DAO.
     *
     * @param entityClass The class of the entities to retrieve
     * @return A list of all entities of the specified class
     */
    public <T> List<T> findAll(Class<T> entityClass) {
        return getDao(entityClass).findAll();
    }

    /**
     * Updates an existing entity in the database using the appropriate DAO.
     *
     * @param entity The entity to be updated
     */
    public <T> void update(T entity) {
        getDao(entity.getClass()).update(entity);
    }

    /**
     * Deletes an existing entity in the database using the appropriate DAO.
     *
     * @param entity The entity to be deleted
     */
    public <T> void delete(T entity) {
        getDao(entity.getClass()).delete(entity);
    }
}
