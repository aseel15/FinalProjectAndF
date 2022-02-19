package com.example.finalproject;
public class PlaceReservation {

    int id;
    int placeID;
    String check_In;
    String check_Out;
    int numOfPeople;

    public PlaceReservation(int id, int placeID, String check_In, String check_Out, int numOfPeople) {
        this.id = id;
        this.placeID = placeID;
        this.check_In = check_In;
        this.check_Out = check_Out;
        this.numOfPeople = numOfPeople;
    }

    public PlaceReservation(int placeID, String check_In, String check_Out, int numOfPeople) {
        this.placeID = placeID;
        this.check_In = check_In;
        this.check_Out = check_Out;
        this.numOfPeople = numOfPeople;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    public String getCheck_In() {
        return check_In;
    }

    public void setCheck_In(String check_In) {
        this.check_In = check_In;
    }

    public String getCheck_Out() {
        return check_Out;
    }

    public void setCheck_Out(String check_Out) {
        this.check_Out = check_Out;
    }

    public int getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(int numOfPeople) {
        this.numOfPeople = numOfPeople;
    }
}
