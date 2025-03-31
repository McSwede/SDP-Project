package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.entities.Customer;
import org.grupp2.sdpproject.entities.Staff;
import org.grupp2.sdpproject.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class UserDAO {
    private SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveUser(User user, String role) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        if ("customer".equals(role)) {
            Customer customer = session.get(Customer.class, user.getCustomer().getCustomerId());
            user.setCustomer(customer);
        } else if ("staff".equals(role)) {
            Staff staff = session.get(Staff.class, user.getStaff().getStaffId());
            user.setStaff(staff);
        }

        session.save(user);
        transaction.commit();
        session.close();
    }

    public User findByUsername(String username) {
        Session session = sessionFactory.openSession();
        User user = session.get(User.class, username);
        session.close();
        return user;
    }
}
