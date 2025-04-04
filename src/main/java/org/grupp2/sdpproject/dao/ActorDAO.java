package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.entities.Actor;
import org.grupp2.sdpproject.entities.Film;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;

public class ActorDAO extends GenericDAO<Actor> {

    public ActorDAO(SessionFactory sessionFactory) {
        super(Actor.class, sessionFactory);
    }

    @Override
    public void delete(Object entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            // Get managed instance in current session
            Actor actor = session.get(Actor.class, ((Actor)entity).getActorId());

            if (actor != null) {
                // 1. Clear all film associations
                for (Film film : new ArrayList<>(actor.getFilmList())) {
                    film.getActorList().remove(actor);
                    session.merge(film); // Update the film
                }

                // 2. Delete from junction table
                session.createNativeQuery("DELETE FROM film_actor WHERE actor_id = :actorId")
                        .setParameter("actorId", actor.getActorId())
                        .executeUpdate();

                // 3. Now safe to delete the actor
                session.remove(actor);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}
