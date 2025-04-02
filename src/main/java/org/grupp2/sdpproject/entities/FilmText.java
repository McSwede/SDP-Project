package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "film_text")
public class FilmText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id", nullable = false)
    private short filmId;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @OneToOne
    @MapsId
    @JoinColumn(name = "film_id")
    private Film film;

    public FilmText() {
    }

    public FilmText(String title, String description) {
        this.title = title;
        this.description = description;
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

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

}
