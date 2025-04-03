package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id", nullable = false)
    private int rentalId;

    @Column(name = "rental_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date rentalDate;

    @Column(name = "return_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    @ManyToOne()
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @OneToMany(mappedBy = "rental")
    private List<Payment> payments = new ArrayList<>();

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdated;

    public Rental() {
    }

    public Rental(Date rentalDate, Date returnDate, Inventory inventory, Customer customer, Staff staff, List<Payment> payments) {
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.inventory = inventory;
        this.customer = customer;
        this.staff = staff;
        this.payments = payments;
    }

    @PreUpdate
    @PrePersist
    public void updateTimestamp(){
        lastUpdated = LocalDateTime.now();
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }



}
