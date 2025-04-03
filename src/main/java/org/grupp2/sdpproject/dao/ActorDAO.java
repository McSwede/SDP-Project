package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.entities.Actor;
import org.grupp2.sdpproject.entities.Film;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ActorDAO extends GenericDAO<Actor> {

    private final SessionFactory sessionFactory;

    public ActorDAO(SessionFactory sessionFactory) {

        super(Actor.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void delete(Object entity) {
        Transaction tx = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Actor actor = (Actor) entity;

            // Manually delete the actor's links in the linking table (film_actor) using native SQL
            String sqlDelete = "DELETE FROM film_actor WHERE actor_id = :actorId";
            session.createNativeQuery(sqlDelete)
                    .setParameter("actorId", actor.getActorId())
                    .executeUpdate();  // Execute the delete query to remove links in the linking table

            // Remove the actor from each film's actor list (this will update the films' actor lists)
            for (Film film : actor.getFilmList()) {
                film.getActorList().remove(actor);  // Remove the actor from the film's actor list
                session.merge(film);  // Update the film entity to reflect the removal
            }

            // Now, delete the actor from the actor table
            session.remove(actor);

            tx.commit();  // Commit the transaction to persist the changes
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();  // Rollback the transaction if something goes wrong
            }
            throw e;  // Rethrow the exception to handle it further up
        } finally {
            if (session != null && session.isOpen()) {
                session.close();  // Always close the session to avoid leaks
            }
        }
    }

}
