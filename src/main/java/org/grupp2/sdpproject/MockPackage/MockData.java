package org.grupp2.sdpproject.MockPackage;

import org.grupp2.sdpproject.Utils.ConfigManager;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.Utils.SessionManager;
import org.grupp2.sdpproject.entities.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MockData {


    public void run(){


        HibernateUtil.initializeDatabase("root", "jesper123", "localhost", "3306");

        DAOManager daoManager = new DAOManager();

        Address address = daoManager.findById(Address.class, 1);

        City city = daoManager.findById(City.class, 2);

        Customer customer = daoManager.findById(Customer.class, 1);

        Inventory inventory = daoManager.findById(Inventory.class, 1);

        Payment payment = daoManager.findById(Payment.class, 33);

        Rental rental = daoManager.findById(Rental.class, 2);

        Staff staff = daoManager.findById(Staff.class, 1);

        Film film = daoManager.findById(Film.class, 1);

        Actor actor = daoManager.findById(Actor.class, 2);

        daoManager.update(actor);
        daoManager.delete(actor);

        //daoManager.update(film);
        //daoManager.delete(film);

        //address.setAddress("");

        //daoManager.update(address);
        //daoManager.delete(address);

        //daoManager.update(payment);
        //daoManager.delete(payment);

        //daoManager.update(staff);
        //daoManager.delete(staff);

        //these once kinda work

        //System.out.println("inventory film: " + inventory.getFilm());

        //daoManager.update(inventory);
        //daoManager.delete(inventory);

        //daoManager.update(city);
        //daoManager.delete(city);

        //daoManager.update(customer);
        //daoManager.delete(customer);

        //daoManager.update(rental);
        //daoManager.delete(rental);

    }


}
