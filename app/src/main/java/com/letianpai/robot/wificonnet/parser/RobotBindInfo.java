package com.letianpai.robot.wificonnet.parser;

public class RobotBindInfo {

    private int code;
    private String msg;
    private RobotBindData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RobotBindData getData() {
        return data;
    }

    public void setData(RobotBindData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
