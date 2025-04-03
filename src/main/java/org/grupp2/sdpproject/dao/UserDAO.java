package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.entities.Film;
import org.grupp2.sdpproject.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UserDAO extends GenericDAO<User> {
    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public User findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }
}
