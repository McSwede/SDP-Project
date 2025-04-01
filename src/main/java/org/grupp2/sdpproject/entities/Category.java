package org.grupp2.sdpproject.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private byte categoryId;

    @Column(length = 25, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "categoryList")
    private List<Film> filmList;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
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
}
