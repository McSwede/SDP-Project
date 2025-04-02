package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import org.grupp2.sdpproject.ENUM.Rating;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "film")
public class Film {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id", nullable = false)
    private short filmId;

    @Size(max = 128)
    @Column(length = 128, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "release_Year", columnDefinition = "YEAR")
    private short releaseYear;

    @ManyToOne(targetEntity = Language.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @ManyToOne(targetEntity = Language.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    @Column(name = "rental_duration", nullable = false)
    private byte rentalDuration;

    @Digits(integer = 2, fraction = 2)
    @Column(name = "rental_rate", precision = 4, scale = 2, nullable = false)
    private BigDecimal rentalRate;

    @Column
    private short length;

    @Digits(integer = 3, fraction = 2)
    @Column(name = "replacement_cost", precision = 5, scale = 2, nullable = false)
    private BigDecimal replacementCost;

    @Enumerated(EnumType.STRING)
    @Column
    private Rating rating;

    @Column(name = "special_features", columnDefinition = "SET('Trailers','Commentaries','Deleted Scenes','Behind the Scenes')")
    private String specialFeatures; //Skulle kunna ändra till en enum av special features

    @ManyToMany(fetch = FetchType.EAGER) //not sure about the cascade type
    @JoinTable(
            name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actorList;

    @ManyToMany(fetch = FetchType.EAGER) //not sure about the cascade type
    @JoinTable(
            name = "film_category",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categoryList;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventory> inventories;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdated;

    public Film() {
    }

    public Film(String title, String description, short releaseYear, Language language, Language originalLanguage,
                byte rentalDuration, BigDecimal rentalRate, short length, BigDecimal replacementCost, Rating rating,
                String specialFeatures, List<Actor> actorList, List<Category> categoryList) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.language = language;
        this.originalLanguage = originalLanguage;
        this.rentalDuration = rentalDuration;
        this.rentalRate = rentalRate;
        this.length = length;
        this.replacementCost = replacementCost;
        this.rating = rating;
        this.specialFeatures = specialFeatures;
        this.actorList = actorList;
        this.categoryList = categoryList;
    }

    @PreUpdate
    @PrePersist
    public void updateTimestamp(){
        lastUpdated = LocalDateTime.now();
    }

    public short getFilmId() {
        return filmId;
    }

    public void setFilmId(short filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(short releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Language getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(Language originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public byte getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(byte rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public BigDecimal getReplacementCost() {
        return replacementCost;
    }

    public void setReplacementCost(BigDecimal replacementCost) {
        this.replacementCost = replacementCost;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public List<Actor> getActorList() {
        return actorList;
    }

    public void setActorList(List<Actor> actorList) {
        this.actorList = actorList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
