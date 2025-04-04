package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.entities.Film;
import org.grupp2.sdpproject.entities.FilmText;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class FilmDAO extends GenericDAO<Film> {

    public FilmDAO(SessionFactory sessionFactory) {
        super(Film.class, sessionFactory);
    }

    // Example of overridden method
    @Override
    public List<Film> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Film", Film.class).list();
        }
    }

    @Override
    public void delete(Object entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.createMutationQuery("DELETE FROM FilmText ft WHERE ft.filmId = :filmId")
                    .setParameter("filmId", ((Film) entity).getFilmId())
                    .executeUpdate();
            session.remove(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}