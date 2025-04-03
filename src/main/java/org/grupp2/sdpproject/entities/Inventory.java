package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id", nullable = false)
    private int inventoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    private List<Rental> rentalList;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdated;

    public Inventory() {
    }

    public Inventory(Film film, Store store, List<Rental> rentalList) {
        this.film = film;
        this.store = store;
        this.rentalList = rentalList;
    }

    @PreUpdate
    @PrePersist
    public void updateTimestamp(){
        lastUpdated = LocalDateTime.now();
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<Rental> getRentalList() {
        return rentalList;
    }

    public void setRentalList(List<Rental> rentalList) {
        this.rentalList = rentalList;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
