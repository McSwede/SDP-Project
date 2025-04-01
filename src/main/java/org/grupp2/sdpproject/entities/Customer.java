package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    //@ManyToOne // TODO: Uncomment this once the Store class has been created
    //@JoinColumn(name = "store_id", nullable = false)
    //private Store store;

    @Size(max = 45)
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Size(max = 45)
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @OneToMany(mappedBy = "customer_id")
    private List<Rental> rentals;

    // I'm not completely sure here but this needs to relate to Rental and Payment so we should probably do two OneToMany relations to them right?

    public Customer() {
    }

    public Customer(String firstName, String lastName, String email, Address address, boolean active,
                    LocalDate createDate, List<Rental> rentals) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.active = active;
        this.createDate = createDate;
        this.rentals = rentals;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }
}
