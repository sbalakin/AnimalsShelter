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
@Table(name = "temporary_owner")
public class TempOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long pk_temp_owner;

    @Column(nullable = false, unique = false)
    private String name;
    private int telephone;
    private String address;
    private int amount_of_animal;
    private String image;

    public TempOwner() {
    }

    public TempOwner(String name, int telephone, String address, int amount_of_animal, String image) {

        this.name = name;
        this.telephone = telephone;
        this.address = address;
        this.amount_of_animal = amount_of_animal;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAmount_of_animal() {
        return amount_of_animal;
    }

    public void setAmount_of_animal(int amount_of_animal) {
        this.amount_of_animal = amount_of_animal;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPk_temp_owner() {
        return pk_temp_owner;
    }

    public void setPk_temp_owner(Long pk_temp_owner) {
        this.pk_temp_owner = pk_temp_owner;
    }

    public void updateTempOwner(String name, int telephone, String address, int amount_of_animal, String image) {
        this.name = name;
        this.telephone = telephone;
        this.address = address;
        this.amount_of_animal = amount_of_animal;
        this.image = image;
    }

}
