package com.letianpai.robot.wificonnet.view;

import android.content.Context;
import android.util.AttributeSet;

import com.letianpai.robot.wificonnet.R;

/**
 * 提交按钮
 * @author liujunbin
 */
public class CommitButton extends AbstractKeyButton{

    public CommitButton(Context context) {
        super(context);
    }

    public CommitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommitButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CommitButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initData() {
        keyButton.setText(mContext.getText(R.string.commit_value));
    }

    @Override
    public void setButtonUnPressed() {
        keyButton.setTextColor(mContext.getColor(R.color.commit_background_color));
    }

    @Override
    public void setButtonPressed() {
        keyButton.setTextColor(mContext.getColor(R.color.white));
    }

}
