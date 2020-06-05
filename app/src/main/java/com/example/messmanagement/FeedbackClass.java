package com.example.messmanagement;

public class FeedbackClass {
    private String id, roll, hostel, foodItem, suggestion, rating;

    public FeedbackClass(String id, String roll, String hostel, String foodItem, String rating, String suggestion) {
        this.id = id;
        this.roll = roll;
        this.hostel = hostel;
        this.foodItem = foodItem;
        this.rating=rating;
        this.suggestion = suggestion;
    }

    public FeedbackClass(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getRatings() {
        return rating;
    }

    public void setRatings(String rating) {
        this.rating = rating;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }


    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }
}

