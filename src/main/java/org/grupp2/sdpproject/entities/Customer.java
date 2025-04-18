package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private short customerId;

    @ManyToOne()
    @JoinColumn(name = "store_id", nullable=false)
    private Store store;

    @Size(max = 45)
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Size(max = 45)
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @ManyToOne()
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Rental> rentals = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @Column(name = "last_update")
    private LocalDateTime lastUpdated;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String email, Address address, boolean active, Date createDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.active = active;
        this.createDate = createDate;
    }

    @PreUpdate
    @PrePersist
    public void updateTimestamp(){
        lastUpdated = LocalDateTime.now();
    }

    public short getCustomerId() {
        return customerId;
    }

    public void setCustomerId(short customerId) {
        this.customerId = customerId;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
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

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
