package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "language_id")
    private byte langueId;

    @Column(length = 20, nullable = false)
    private String name;

    public Language(String name) {
        this.name = name;
    }

    public Language() {
    }

    public byte getLangueId() {
        return langueId;
    }

    public void setLangueId(byte langueId) {
        this.langueId = langueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
