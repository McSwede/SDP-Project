package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;
import org.grupp2.sdpproject.ENUM.Role;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    // For Customer reference (customer_id is SMALLINT)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "customer_id",
            columnDefinition = "SMALLINT"  // Matches Customer's short type
    )
    private Customer customer;

    // For Staff reference (staff_id is TINYINT)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "staff_id",
            referencedColumnName = "staff_id",
            columnDefinition = "TINYINT"  // Matches Staff's byte type
    )
    private Staff staff;

    // Default constructor
    public User() {}

    // Constructor including firstName and lastName
    public User( String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Staff getStaff() { return staff; }
    public void setStaff(Staff staff) { this.staff = staff; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", customer=" + customer +
                ", staff=" + staff +
                '}';
    }
}
