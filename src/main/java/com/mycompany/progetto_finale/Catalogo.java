package com.mycompany.progetto_finale;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Entity
@Table(name = "Categories")
public class Catalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID", nullable = false)
    private int categoryID;

    @Column(name = "CategoryName", nullable = false)
    private String categoryName;

    @Column(name = "Description")
    private String description;

    @Lob
    @Column(name = "Picture", columnDefinition = "BLOB")
    @JdbcTypeCode(Types.BINARY)
    private byte[] picture;

    public Catalogo() {
    }

    public Catalogo(String categoryName,
                     String description,
                     byte[] picture) {

        this.categoryName = categoryName;
        this.description = description;
        this.picture = picture;
    }

    public Catalogo(int categoryID,
                     String categoryName,
                     String description,
                     byte[] picture) {

        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.description = description;
        this.picture = picture;
    }

 

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}