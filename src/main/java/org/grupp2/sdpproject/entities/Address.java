package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private short addressId;

    @Size(max = 50)
    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Size(max = 50)
    @Column(name = "address2", length = 50)
    private String address2;

    @Size(max = 20)
    @Column(name = "district", nullable = false, length = 20)
    private String district;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Size(max = 10)
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Size(max = 20)
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "location", columnDefinition = "GEOMETRY", nullable = false)
    private Point location;

    @OneToMany(mappedBy = "address")
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy = "address")
    private List<Staff> staff = new ArrayList<>();

    @OneToMany(mappedBy = "address")
    private List<Store> stores = new ArrayList<>();

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdated;

    public Address() {
    }

    public Address(String address, String address2, String district, City city, String postalCode, String phone) {
        this.address = address;
        this.address2 = address2;
        this.district = district;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    @PreUpdate
    @PrePersist
    public void updateTimestamp(){
        lastUpdated = LocalDateTime.now();
    }

    public short getAddressId() {
        return addressId;
    }

    public void setAddressId(short addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Can be used like:
     * location.getX() = longitude
     * location.getY() = latitude
     *
     * @return location as a Point
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Create a Point by using:
     * GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 0);
     * gf.createPoint(new Coordinate(longitude, latitude))
     *
     * @param location
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public List<Staff> getStaff() {
        return staff;
    }

    public void setStaff(List<Staff> staff) {
        this.staff = staff;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
