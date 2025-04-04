package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private byte staffId;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] picture;

    @Column(length = 50)
    private String email;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private boolean active; //ska vara true by default så kanske måste göra något här

    @Size(max = 16)
    @Column(nullable = false, length = 16)
    private String username;

    @Size(max = 40)
    @Column(nullable = false, length = 40) // binary?
    private String password;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<Rental> rentals = new ArrayList<>();

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdated;

    public Staff() {
    }

    @PreUpdate
    @PrePersist
    public void updateTimestamp(){
        lastUpdated = LocalDateTime.now();
    }

    public Staff(String firstName, String lastName, Address address, byte[] picture,
                 String email, Store store, boolean active, String username, String password, List<Rental> rentals,
                 List<Payment> payments) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.picture = picture;
        this.email = email;
        this.store = store;
        this.active = active;
        this.username = username;
        this.password = password;
        this.rentals = rentals;
        this.payments = payments;
    }


    public void setStaffId(byte staffId) {
        this.staffId = staffId;
    }

    public byte getStaffId() {return staffId;}

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
