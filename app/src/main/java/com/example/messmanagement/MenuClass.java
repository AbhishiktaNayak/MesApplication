package com.example.messmanagement;

public class MenuClass {
    public String hostel,day,breakfast,lunch,snacks,dinner;

    public MenuClass(){

    }

    public MenuClass(String hostel, String day, String breakfast, String lunch, String snacks, String dinner) {
        this.hostel = hostel;
        this.day = day;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.snacks = snacks;
        this.dinner = dinner;
    }

    public MenuClass(String breakfast, String lunch, String snacks, String dinner) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.snacks = snacks;
        this.dinner = dinner;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getSnacks() {
        return snacks;
    }

    public void setSnacks(String snacks) {
        this.snacks = snacks;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }
}
