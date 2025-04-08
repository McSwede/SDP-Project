package org.grupp2.sdpproject.Utils;

import org.grupp2.sdpproject.dao.ActorDAO;
import org.grupp2.sdpproject.dao.FilmDAO;
import org.grupp2.sdpproject.dao.GenericDAO;
import org.grupp2.sdpproject.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.entity.EntityPersister;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAOManager {
    private static DAOManager instance;
    private final SessionFactory sessionFactory;
    private final Map<Class<?>, GenericDAO<?>> daoMap = new HashMap<>();
    private final CacheManager cacheManager;

    private DAOManager() {
        // Automatically get sessionFactory
        this.sessionFactory = HibernateUtil.getSessionFactory();

        // Register non-generic entities here
        daoMap.put(Film.class, new FilmDAO(sessionFactory));
        daoMap.put(Actor.class, new ActorDAO(sessionFactory));
        //...

        // Start caching immediately
        this.cacheManager = new CacheManager(this);
        initializeCaches();
    }

    // Cache these entities immediately on startup
    private void initializeCaches() {
        cacheManager.initializeCachesFor(
                User.class,
                Film.class,
                Actor.class,
                Customer.class,
                Address.class,
                Payment.class,
                Rental.class,
                Staff.class,
                Store.class,
                Inventory.class
        );
    }

    public static synchronized DAOManager getInstance() {
        if (instance == null) {
            instance = new DAOManager();
        }
        return instance;
    }

    /**
     * Retrieves a DAO for the specified entity class. If the class is not
     * already mapped to a DAO, a new GenericDAO is created and stored.
     *
     * @param entityClass The class of the entity
     * @return A DAO instance for the given entity class
     */
    @SuppressWarnings("unchecked")
    <T> GenericDAO<T> getDao(Class<T> entityClass) {
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
        cacheManager.invalidateCache(entity.getClass());
    }

    /**
     * Finds an entity by its ID using the appropriate DAO.
     *
     * @param entityClass The class of the entity to find
     * @param id          The primary key of the entity
     * @return The found entity or null if no matching entity exists
     */
    public <T> T findById(Class<T> entityClass, Object id) {
        if (id == null) {
            return null;
        }

        // 1. First try to get from cache using Hibernate's identifier
        try {
            List<T> cachedData = cacheManager.getCachedData(entityClass);
            if (cachedData != null && !cachedData.isEmpty()) {
                T cachedEntity = findInCacheById(entityClass, id, cachedData);
                if (cachedEntity != null) {
                    return cachedEntity;
                }
            }
        } catch (Exception e) {
            System.err.println("Cache lookup failed!");
        }

        // 2. Fall back to database query
        return getDao(entityClass).findById(id);
    }

    /**
     * Helper method to find entity by ID in cached data by using Hibernate's identifier
     */
    private <T> T findInCacheById(Class<T> entityClass, Object id, List<T> cachedData) {
        try (Session session = sessionFactory.openSession()) {
            SessionFactoryImplementor sfi = (SessionFactoryImplementor) sessionFactory;
            EntityPersister persister = sfi.getMetamodel().entityPersister(entityClass);

            for (T entity : cachedData) {
                try {
                    Object entityId = persister.getIdentifier(entity, (SharedSessionContractImplementor) session);
                    if (id.equals(entityId)) {
                        return entity;
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Retrieves all entities of the specified class using the appropriate DAO.
     *
     * @param entityClass The class of the entities to retrieve
     * @return A list of all entities of the specified class
     */
    public <T> List<T> findAll(Class<T> entityClass) {
        return cacheManager.getCachedData(entityClass);
    }

    /**
     * Updates an existing entity in the database using the appropriate DAO.
     *
     * @param entity The entity to be updated
     */
    public <T> void update(T entity) {
        getDao(entity.getClass()).update(entity);
        cacheManager.invalidateCache(entity.getClass());
    }

    /**
     * Deletes an existing entity in the database using the appropriate DAO.
     *
     * @param entity The entity to be deleted
     */
    public <T> void delete(T entity) {
        getDao(entity.getClass()).delete(entity);
        cacheManager.invalidateCache(entity.getClass());
    }

    public <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value) {
        // First try to find in cache
        List<T> cachedData = cacheManager.getCachedData(entityClass);
        if (cachedData != null && !cachedData.isEmpty()) {
            List<T> filteredResults = cachedData.stream()
                    .filter(item -> {
                        try {
                            Field field = entityClass.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            Object fieldValue = field.get(item);
                            return (fieldValue == null && value == null) ||
                                    (fieldValue != null && fieldValue.equals(value));
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .toList();

            if (!filteredResults.isEmpty()) {
                return filteredResults;
            }
        }

        // If not found in cache or cache is empty, query database
        return getDao(entityClass).findByField(fieldName, value);
    }

    public <T> List<T> findByFields(Class<T> entityClass, Map<String, Object> fields) {
        // First try to find in cache
        List<T> cachedData = cacheManager.getCachedData(entityClass);
        if (cachedData != null && !cachedData.isEmpty()) {
            List<T> filteredResults = cachedData.stream()
                    .filter(item -> {
                        try {
                            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                                Field field = entityClass.getDeclaredField(entry.getKey());
                                field.setAccessible(true);
                                Object fieldValue = field.get(item);
                                Object expectedValue = entry.getValue();

                                if (!((fieldValue == null && expectedValue == null) ||
                                        (fieldValue != null && fieldValue.equals(expectedValue)))) {
                                    return false;
                                }
                            }
                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .toList();

            if (!filteredResults.isEmpty()) {
                return filteredResults;
            }
        }

        // If not found in cache or cache is empty, query database
        return getDao(entityClass).findByFields(fields);
    }

    public static void shutdown() {
        if (instance != null) {
            instance.cacheManager.shutdown();
            instance = null;
        }
    }
}
