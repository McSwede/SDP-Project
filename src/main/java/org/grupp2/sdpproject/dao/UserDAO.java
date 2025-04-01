package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UserDAO {
    //private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void saveUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession(); // Ensure session is opened
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(user);
            System.out.println("userr: " + user);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    public User findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }
}
