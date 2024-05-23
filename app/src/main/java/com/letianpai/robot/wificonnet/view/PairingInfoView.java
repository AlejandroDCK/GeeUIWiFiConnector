package com.letianpai.robot.wificonnet.view;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.letianpai.robot.wificonnet.MainActivity;
import com.letianpai.robot.wificonnet.R;

/**
 * @author liujunbin
 */
public class PairingInfoView extends LinearLayout {
    private Context mContext;
    private CommitButton commitButton;


    public PairingInfoView(Context context) {
        super(context);
        init(context);
    }

    public PairingInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PairingInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PairingInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.keyboard_pair_info, this);
        commitButton = findViewById(R.id.iv_ok);
        addKeyPressedListener();
    }

    private void addKeyPressedListener() {
        commitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPairCodeView();
            }
        });

    }
    private void showPairCodeView() {
        ((MainActivity)(mContext)).showPairCodeView();
    }


}
