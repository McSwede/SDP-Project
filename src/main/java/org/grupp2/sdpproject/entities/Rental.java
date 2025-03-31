package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id", nullable = false)
    private int rentalId;

    @Column(name = "rental_date", nullable = false)
    private Date rentalDate;

    @Column(name = "inventory_id", nullable = false)
    private int inventoryId;

    @Column(name = "customer_id", nullable = false)
    private short customerId;

    @Column(name = "return_date")
    private Date returnDate;

    @Column(name = "staff_id", nullable = false)
    private byte staffId;

    public Rental() {
    }

    public Rental(Date rentalDate, int inventoryId, short customerId, Date returnDate, byte staffId) {
        this.rentalDate = rentalDate;
        this.inventoryId = inventoryId;
        this.customerId = customerId;
        this.returnDate = returnDate;
        this.staffId = staffId;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public short getCustomerId() {
        return customerId;
    }

    public void setCustomerId(short customerId) {
        this.customerId = customerId;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public byte getStaffId() {
        return staffId;
    }

    public void setStaffId(byte staffId) {
        this.staffId = staffId;
    }
}
