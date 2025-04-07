package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.entities.Rental;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.grupp2.sdpproject.Utils.HibernateUtil;
import java.util.List;

public class RentalDAO extends GenericDAO<Rental> {

    public RentalDAO(SessionFactory sessionFactory) {
        super(Rental.class, sessionFactory);
    }

    // Method to find rentals by customer ID
    public List<Rental> findRentalsByCustomerId(int customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Create a query to fetch rentals for the given customer ID
            Query<Rental> query = session.createQuery("FROM Rental WHERE customer.customerId = :customerId", Rental.class);
            query.setParameter("customerId", customerId);

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
