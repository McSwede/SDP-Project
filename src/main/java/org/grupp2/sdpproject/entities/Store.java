package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private byte storeId;

    @ManyToOne
    @JoinColumn(name = "manager_staff_id", nullable = false, unique = true)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "store")
    private List<Inventory> inventories;

    @OneToMany(mappedBy = "store")
    private List<Customer> customers;

    @OneToMany(mappedBy = "store")
    private List<Staff> staffList;


    public Store() {
    }

    public Store(Staff staff, Address address, List<Inventory> inventories,
                 List<Customer> customers, List<Staff> staffList) {
        this.staff = staff;
        this.address = address;
        this.inventories = inventories;
        this.customers = customers;
        this.staffList = staffList;
    }

    public byte getStoreId() {
        return storeId;
    }

    public void setStoreId(byte storeId) {
        this.storeId = storeId;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }
}
