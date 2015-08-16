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
@Table(name = "shelter")
public class Shelter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long pk_shelter;

    @Column(nullable = false, unique = false)
    private String name;
    private int telephone;
    private String address;
    private int seat;
    private int free_seat;
    private String site;
    private String email;
    private String description;
    private String image;

    public Shelter() {
    }

    public Shelter(String name, int telephone, String address, int seat, int free_seat, String site, String email, String description, String image) {
        this.name = name;
        this.telephone = telephone;
        this.address = address;
        this.seat = seat;
        this.free_seat = free_seat;
        this.site = site;
        this.email = email;
        this.description = description;
        this.image = image;
    }

    public Long getPk_shelter() {
        return pk_shelter;
    }

    public void setPk_shelter(Long pk_shelter) {
        this.pk_shelter = pk_shelter;
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

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getFree_seat() {
        return free_seat;
    }

    public void setFree_seat(int free_seat) {
        this.free_seat = free_seat;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void updateShelter(String name, int telephone, String address, int seat, int free_seat, String site, String email, String description, String image) {
        this.name = name;
        this.telephone = telephone;
        this.address = address;
        this.seat = seat;
        this.free_seat = free_seat;
        this.site = site;
        this.email = email;
        this.description = description;
        this.image = image;
    }

}
