package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private byte languageId;

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
}
