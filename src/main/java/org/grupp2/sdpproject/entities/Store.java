package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private byte storeId;

    @Column(name = "manager_staff_id", nullable = false, unique = true)
    private byte managerStaffId;

    @Column(name = "address_id", nullable = false)
    private short addressId;

    public Store() {
    }

    public Store(byte managerStaffId, short addressId) {
        this.managerStaffId = managerStaffId;
        this.addressId = addressId;
    }

    public byte getManagerStaffId() {
        return managerStaffId;
    }

    public void setManagerStaffId(byte managerStaffId) {
        this.managerStaffId = managerStaffId;
    }

    public short getAddressId() {
        return addressId;
    }

    public void setAddressId(short addressId) {
        this.addressId = addressId;
    }
}
