package com.letianpai.robot.wificonnet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.letianpai.robot.wificonnet.R;

/**
 * @author liujunbin
 */
public class KeyButton extends Button {
    private KeyButton keyButton;
    private Context mContext;
    public KeyButton(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        keyButton = KeyButton.this;
        setButtonUnPressed();
        addKeyButtonListeners();
    }

    public KeyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public KeyButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void addKeyButtonListeners() {

        keyButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    setButtonUnPressed();

                }else if (event.getAction() == MotionEvent.ACTION_CANCEL){
                    setButtonUnPressed();

                }else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    setButtonPressed();
                }
                return false;
            }
        });
    }

    private void setButtonUnPressed() {
        keyButton.setBackgroundColor(mContext.getResources().getColor(R.color.background));
        keyButton.setTextColor(mContext.getColor(R.color.keyboard_text_color));
    }

    private void setButtonPressed() {
        keyButton.setBackgroundColor(mContext.getResources().getColor(R.color.keyboard_highlight));
        keyButton.setTextColor(mContext.getColor(R.color.white));
        
    }


}
