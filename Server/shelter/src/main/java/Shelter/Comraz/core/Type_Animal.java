/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shelter.Comraz.core;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author DAT
 */
@Entity
@Table(name = "type_animal")
public class Type_Animal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long pk_type_animal;

    @Column(nullable = false, unique = false)
    private String title;

    public Type_Animal() {
    }

    public Type_Animal(Long pk_type_animal, String title) {
        this.pk_type_animal = pk_type_animal;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Long getPk_type_animal() {
        return pk_type_animal;
    }

}
