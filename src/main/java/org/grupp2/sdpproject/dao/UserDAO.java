package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class UserDAO extends GenericDAO<User> {

    public UserDAO(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    public User findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }
}
