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

    public Inventory() {
    }

    public Inventory(short filmId, byte storeId) {
        this.filmId = filmId;
        this.storeId = storeId;
    }

    public short getFilmId() {
        return filmId;
    }

    public void setFilmId(short filmId) {
        this.filmId = filmId;
    }

    public byte getStoreId() {
        return storeId;
    }

    public void setStoreId(byte storeId) {
        this.storeId = storeId;
    }
}
