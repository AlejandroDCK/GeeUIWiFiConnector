package com.letianpai.robot.wificonnet.parser;

public class CountryData {

    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "{" +
                "country:'" + country + '\'' +
                '}';
    }
}
