package org.grupp2.sdpproject.dao;

import org.grupp2.sdpproject.entities.Film;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import org.grupp2.sdpproject.Utils.HibernateUtil;

public class FilmsDAO {

    public List<Film> getAllFilms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Film", Film.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
