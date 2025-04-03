package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private byte languageId;

    @Size(max = 20)
    @Column(length = 20, nullable = false, columnDefinition = "CHAR(20)")
    private String name;

    @OneToMany(mappedBy = "language")
    private List<Film> films;

    @OneToMany(mappedBy = "originalLanguage")
    private List<Film> originalFilms = new ArrayList<>();

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdated;

    public Language(String name) {
        this.name = name;
    }

    public Language() {
    }

    public Language(String name, List<Film> films, List<Film> originalFilms) {
        this.name = name;
        this.films = films;
        this.originalFilms = originalFilms;
    }

    @PreUpdate
    @PrePersist
    public void updateTimestamp(){
        lastUpdated = LocalDateTime.now();
    }

    public byte getLanguageId() {
        return languageId;
    }

    public void setLanguageId(byte languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }

    public List<Film> getOriginalFilms() {
        return originalFilms;
    }

    public void setOriginalFilms(List<Film> originalFilms) {
        this.originalFilms = originalFilms;
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
