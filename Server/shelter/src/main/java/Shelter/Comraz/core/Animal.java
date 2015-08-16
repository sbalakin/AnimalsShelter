/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shelter.Comraz.core;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author DAT
 */
@Entity
@Table(name = "animal")

public class Animal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long pk_animal;

    @Column(nullable = false, unique = false)
    private String name;

    private String type;

    private String color;
    private String health_status;
    private float weight;
    private String breed;
    private String relationship_with_human;
    private int age;
    private String image;
    private String description;
    private Boolean is_were_owner;

    private int gender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pk_type_animal")
    private Type_Animal type_animal;

    private String showGender;

    public String getShowGender() {
        return showGender;
    }

    public void setShowGender(String showGender) {
        this.showGender = showGender;
    }

    public Animal() {
    }

    public Animal(Type_Animal type_animal, String name, String type, int gender, String color, String health_status, float weight, String breed, String relationship_with_human, int age, String image, String description) {
        this.type_animal = type_animal;
        this.name = name;
        this.type = type;
        this.gender = gender;
        this.color = color;
        this.health_status = health_status;
        this.weight = weight;
        this.breed = breed;
        this.relationship_with_human = relationship_with_human;
        this.age = age;
        this.image = image;
        this.description = description;
        // this.is_were_owner = is_were_owner;
    }

    public Long getId() {
        return pk_animal;
    }

    public Long getPk_animal() {
        return pk_animal;
    }

    public String getName() {
        return name;
    }

    public Type_Animal getType_animal() {
        return this.type_animal;
    }

    public int getGender() {
        return gender;
    }

    public String getColor() {
        return color;
    }

    public String getHealth_status() {
        return health_status;
    }

    public float getWeight() {
        return weight;
    }

    public String getBreed() {
        return breed;
    }

    public String getRelationship_with_human() {
        return relationship_with_human;
    }

    public int getAge() {
        return age;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIs_were_owner() {
        return is_were_owner;
    }

    public String getType() {
        return type;
    }

    
    public void updateAnimal(Type_Animal type_animal, String name, String type, int gender, String color, String health_status, float weight, String breed, String relationship_with_human, int age, String image, String description) {
        this.type_animal = type_animal;
        this.name = name;
        this.type = type;
        this.gender = gender;
        this.color = color;
        this.health_status = health_status;
        this.weight = weight;
        this.breed = breed;
        this.relationship_with_human = relationship_with_human;
        this.age = age;
        this.image = image;
        this.description = description;

    }
}
