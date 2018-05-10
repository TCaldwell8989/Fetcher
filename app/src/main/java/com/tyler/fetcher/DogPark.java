package com.tyler.fetcher;

import java.util.UUID;

//Model layer for Fetcher
public class DogPark {

    private UUID uuid;
    private String name;
    private String location;
    private String note;
    private int rating;

    public DogPark() {
        this(UUID.randomUUID());
    }

    public DogPark(UUID id) {
        uuid = id;
    }


    public UUID getId() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    //Gives each photo a individual name
    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
