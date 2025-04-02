package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private byte languageId;

    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String name;

    @OneToMany(mappedBy = "language")
    private List<Film> films;

    @OneToMany(mappedBy = "originalLanguage")
    private List<Film> originalFilms;

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
}
