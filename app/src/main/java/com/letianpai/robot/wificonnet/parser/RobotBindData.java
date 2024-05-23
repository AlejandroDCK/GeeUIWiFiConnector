package com.letianpai.robot.wificonnet.parser;

public class RobotBindData {
    private String device_id;
    private String model;
    private String mac;
    private String device_name;
    private int is_online;
    private String country;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public int getIs_online() {
        return is_online;
    }

    public void setIs_online(int is_online) {
        this.is_online = is_online;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "RobotBindData{" +
                "device_id='" + device_id + '\'' +
                ", model='" + model + '\'' +
                ", mac='" + mac + '\'' +
                ", device_name='" + device_name + '\'' +
                ", is_online=" + is_online +
                ", country='" + country + '\'' +
                '}';
    }
}
