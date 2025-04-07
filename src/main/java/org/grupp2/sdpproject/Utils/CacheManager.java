package org.grupp2.sdpproject.Utils;

import org.grupp2.sdpproject.dao.GenericDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheManager {
    private final Map<Class<?>, CacheEntry<?>> cache = new HashMap<>();
    private final ExecutorService executor;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final DAOManager daoManager;

    private static class CacheEntry<T> {
        private List<T> data;
        private boolean isValid;

        public CacheEntry(List<T> data) {
            this.data = data;
            this.isValid = true;
        }
    }

    public CacheManager(DAOManager daoManager) {
        this.daoManager = daoManager;
        this.executor = Executors.newCachedThreadPool();
    }

    public void initializeCachesFor(Class<?>... entityClasses) {
        for (Class<?> entityClass : entityClasses) {
            executor.submit(() -> {
                initializeCacheFor(entityClass);
            });
        }
    }

    public <T> void initializeCacheFor(Class<T> entityClass) {
        refreshCacheDirect(entityClass);
    }

    public <T> List<T> getCachedData(Class<T> entityClass) {
        lock.readLock().lock();
        try {
            CacheEntry<T> entry = (CacheEntry<T>) cache.get(entityClass);

            if (entry != null && entry.isValid) {
                return entry.data;
            }
        } finally {
            lock.readLock().unlock();
        }

        return refreshCache(entityClass);
    }

    public <T> void invalidateCache(Class<T> entityClass) {
        lock.writeLock().lock();
        try {
            CacheEntry<T> entry = (CacheEntry<T>) cache.get(entityClass);
            if (entry != null) {
                entry.isValid = false;
            }
        } finally {
            lock.writeLock().unlock();
        }

        executor.submit(() -> refreshCacheDirect(entityClass));
    }

    private <T> List<T> refreshCache(Class<T> entityClass) {
        lock.writeLock().lock();
        try {
            CacheEntry<T> entry = (CacheEntry<T>) cache.get(entityClass);
            if (entry != null && entry.isValid) {
                return entry.data;
            }

            return refreshCacheDirect(entityClass);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private <T> List<T> refreshCacheDirect(Class<T> entityClass) {
        GenericDAO<T> dao = daoManager.getDao(entityClass);
        List<T> freshData = dao.findAll();

        lock.writeLock().lock();
        try {
            cache.put(entityClass, new CacheEntry<>(freshData));
            return freshData;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}