package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private byte categoryId;

    @Size(max = 25)
    @Column(length = 25, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "categoryList")
    private List<Film> filmList;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdated;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    @PreUpdate
    @PrePersist
    public void updateTimestamp(){
        lastUpdated = LocalDateTime.now();
    }

    public byte getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(byte categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Film> getFilmList() {
        return filmList;
    }

    public void setFilmList(List<Film> filmList) {
        this.filmList = filmList;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return name;
    }
}
