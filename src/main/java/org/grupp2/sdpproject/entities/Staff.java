package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_id")
    private byte storeId;

    @Column(name = "manager_staff_id", nullable = false, unique = true)
    private byte managerStaffId;

    @Column(name = "address_id", nullable = false)
    private short addressId;

}
