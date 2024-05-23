package com.letianpai.robot.wificonnet.parser;

import android.graphics.Rect;

/**
 * @author liujunbin
 */
public class RobotKeyboardLabel {

    private String text;
    private int bgColor;
    private int bgPressedColor;
    private int textColor;
    private int align;
    private int origTextSize;
    private Rect origRect;
    private Rect dispRect;
    private Rect origTouchRect;
    private Rect dispTouchRect;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getBgPressedColor() {
        return bgPressedColor;
    }

    public void setBgPressedColor(int bgPressedColor) {
        this.bgPressedColor = bgPressedColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public int getOrigTextSize() {
        return origTextSize;
    }

    public void setOrigTextSize(int origTextSize) {
        this.origTextSize = origTextSize;
    }

    public Rect getOrigRect() {
        return origRect;
    }

    public void setOrigRect(Rect origRect) {
        this.origRect = origRect;
    }

    public Rect getDispRect() {
        return dispRect;
    }

    public void setDispRect(Rect dispRect) {
        this.dispRect = dispRect;
    }

    public Rect getOrigTouchRect() {
        return origTouchRect;
    }

    public void setOrigTouchRect(Rect origTouchRect) {
        this.origTouchRect = origTouchRect;
    }

    public Rect getDispTouchRect() {
        return dispTouchRect;
    }

    public void setDispTouchRect(Rect dispTouchRect) {
        this.dispTouchRect = dispTouchRect;
    }



}
