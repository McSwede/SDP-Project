package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rental_id", nullable = false)
    private int rentalId;

    @Column(name = "rental_date", nullable = false)
    private Date rentalDate;

    @Column(name = "inventory_id", nullable = false)
    private int inventoryId;

    @Column(name = "customer_id", nullable = false)
    private short customerId;

    @Column(name = "return_date")
    private Date returnDate;

    @Column(name = "staff_id", nullable = false)
    private byte staffId;

}
