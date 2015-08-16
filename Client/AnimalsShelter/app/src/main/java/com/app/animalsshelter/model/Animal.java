package com.app.animalsshelter.model;

import java.io.Serializable;

public class Animal implements Serializable {
    private static final long serialVersionUID = 1L;

    String imageString;
    String pk, species, name, breed, gender, age, weight, sterilize, description, last_date_seen, last_location, latitude, longitude;

    String imageURLthumbnail, imageURL;

    public Animal(String pk, String species, String date, String location, String description, String latitude, String longitude) {
        this.pk = pk;
        this.species = species;
        this.last_date_seen = date;
        this.last_location = location;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageURLthumbnail = "http://10.0.3.2:4445/img/animals/thumbnails/" + pk + ".jpg";
        this.imageURL = "http://10.0.3.2:4445/img/animals/" + pk + ".jpg";
    }

    public String getLast_date_seen() {
        return last_date_seen;
    }

    public void setLast_date_seen(String last_date_seen) {
        this.last_date_seen = last_date_seen;
    }

    public String getLast_location() {
        return last_location;
    }

    public void setLast_location(String last_location) {
        this.last_location = last_location;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURLthumbnail() {
        return imageURLthumbnail;
    }

    public void setImageURLthumbnail(String imageURLthumbnail) {
        this.imageURLthumbnail = imageURLthumbnail;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Animal(String pk, String species, String name, String breed, String gender, String age, String weight, String sterilize, String description) {

        this.pk = pk;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.sterilize = sterilize;
        this.description = description;
        //this.imageString = imageString;
        this.imageURLthumbnail = "http://10.0.3.2:4445/img/animals/thumbnails/" + pk + ".jpg";
        this.imageURL = "http://10.0.3.2:4445/img/animals/" + pk + ".jpg";
    }

    public Animal() {
    }


    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSterilize() {
        return sterilize;
    }

    public void setSterilize(String sterilize) {
        this.sterilize = sterilize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

