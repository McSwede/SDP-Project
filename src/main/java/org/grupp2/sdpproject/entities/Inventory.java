package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inventory_id", nullable = false)
    private int inventoryId;

    @Column(name = "film_id", nullable = false)
    private short filmId;

    @Column(name = "store_id", nullable = false)
    private byte storeId;

}
