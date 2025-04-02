package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.entities.Film;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class FilmDAO extends GenericDAO<Film> {
    private final SessionFactory sessionFactory;

    public FilmDAO(SessionFactory sessionFactory) {
        super(Film.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    // Example of overridden method
    @Override
    public List<Film> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Film", Film.class).list();
        }
    }
}