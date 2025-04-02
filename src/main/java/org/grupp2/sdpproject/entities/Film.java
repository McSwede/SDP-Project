package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;
import org.grupp2.sdpproject.ENUM.Rating;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "film")
public class Film {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "film_id", nullable = false)
    private short filmId;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(name = "release_Year")
    private short releaseYear;

    @Column(name = "language_id", nullable = false)
    private byte languageId;

    @Column(name = "original_Language_id")
    private byte originalLanguageId;

    @Column(name = "rental_duration", nullable = false)
    private byte rentalDuration;

    @Column(name = "rental_rate", precision = 4, scale = 2, nullable = false)
    private BigDecimal rentalRate;

    @Column
    private short length;

    @Column(name = "replacementCost", precision = 5, scale = 2, nullable = false)
    private BigDecimal replacementCost;

    @Enumerated(EnumType.STRING)
    @Column
    private Rating rating;

    @Column(name = "special_features")
    private Set<String> specialFeatures; //Skulle kunna ändra till en enum av special features

    public Film(){

    }

    public Film(Set<String> specialFeatures, Rating rating, BigDecimal replacementCost, short length, BigDecimal rentalRate,
                byte rentalDuration, byte originalLanguageId, byte languageId, short releaseYear, String description, String title) {
        this.specialFeatures = specialFeatures;
        this.rating = rating;
        this.replacementCost = replacementCost;
        this.length = length;
        this.rentalRate = rentalRate;
        this.rentalDuration = rentalDuration;
        this.originalLanguageId = originalLanguageId;
        this.languageId = languageId;
        this.releaseYear = releaseYear;
        this.description = description;
        this.title = title;
    }

    public Set<String> getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(Set<String> specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public BigDecimal getReplacementCost() {
        return replacementCost;
    }

    public void setReplacementCost(BigDecimal replacementCost) {
        this.replacementCost = replacementCost;
    }

    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public byte getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(byte rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public byte getOriginalLanguageId() {
        return originalLanguageId;
    }

    public void setOriginalLanguageId(byte originalLanguageId) {
        this.originalLanguageId = originalLanguageId;
    }

    public byte getLanguageId() {
        return languageId;
    }

    public void setLanguageId(byte languageId) {
        this.languageId = languageId;
    }

    public short getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(short releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public short getFilmId() {
        return filmId;
    }

    public void setFilmId(short filmId) {
        this.filmId = filmId;
    }

    @Override
    public String toString() {
        return title;
    }
}
