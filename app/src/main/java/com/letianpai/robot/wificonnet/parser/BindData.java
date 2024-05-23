package com.letianpai.robot.wificonnet.parser;

public class BindData {
    private boolean is_bind;
    private String country;

    public boolean isIs_bind() {
        return is_bind;
    }

    public void setIs_bind(boolean is_bind) {
        this.is_bind = is_bind;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "{" +
                "is_bind:" + is_bind +
                ", country:'" + country + '\'' +
                '}';
    }
}
