package org.grupp2.sdpproject.Utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static synchronized boolean initializeDatabase(String username, String password, String ip, String port) {
        if (sessionFactory != null) {
            System.out.println("SessionFactory is already initialized.");
            return true;
        }

        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            String url = "jdbc:mysql://" + ip + ":" + port + "/sakila";

            // Replace placeholders in hibernate.cfg.xml
            configuration.setProperty("hibernate.connection.url", url);
            configuration.setProperty("hibernate.connection.username", username);
            configuration.setProperty("hibernate.connection.password", password);


            sessionFactory = configuration.buildSessionFactory();
            sessionFactory.openSession().close();

            // We call the DAOManager just to run its constructor to do the setup in the background
            DAOManager.getInstance();

            return true;
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace(); // For debugging
            return false;
        }
    }

    public static synchronized SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static synchronized void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}