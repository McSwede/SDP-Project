package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.entities.Film;
import org.hibernate.SessionFactory;

public class FilmDAO extends GenericDAO<Film> {
    public FilmDAO(SessionFactory sessionFactory) {
        super(Film.class, sessionFactory);
    }

    // This is where we would add overrides to the generic DAO methods
}