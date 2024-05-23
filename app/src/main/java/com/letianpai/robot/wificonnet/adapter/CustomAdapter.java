package com.letianpai.robot.wificonnet.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.letianpai.robot.wificonnet.R;
import com.letianpai.robot.wificonnet.callback.KeyPressCallback;
import com.letianpai.robot.wificonnet.view.KeyButton;

import java.util.List;
/**
 * @author liujunbin
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<String> data;

    public CustomAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.textView.setText(data.get(position));
        holder.keyButton.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        KeyButton keyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            keyButton = itemView.findViewById(R.id.keyButton);
            keyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String buttonText = ((Button) v).getText() + "";
                    if (!TextUtils.isEmpty(buttonText)){
                        KeyPressCallback.getInstance().pressKey(KeyPressCallback.KEY_TYPE_VALUE,buttonText);
                    }
//                    Log.e("letianpai_test","buttonText: "+ buttonText);
                }
            });

        }
    }
}
