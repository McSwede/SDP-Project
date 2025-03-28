package org.grupp2.sdpproject.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private short countryId;

    @Size(max = 50)
    @Column(name = "country", nullable = false)
    private String country;

    // I believe this should have a OneToMany relationship to cities

    public Country() {
    }

    public Country(String country) {
        this.country = country;
    }

    public short getCountryId() {
        return countryId;
    }

    public void setCountryId(short countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
