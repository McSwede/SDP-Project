package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.entities.Film;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


public class FilmDAO extends GenericDAO<Film> {

    public FilmDAO(SessionFactory sessionFactory) {
        super(Film.class, sessionFactory);
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

    public Film findByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Film> query = session.createQuery(
                    "FROM Film WHERE title = :title", Film.class);
            query.setParameter("title", title);
            return query.uniqueResult();
        }
    }
}