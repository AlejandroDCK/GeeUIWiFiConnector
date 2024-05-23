package com.letianpai.robot.wificonnet.view;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.letianpai.robot.wificonnet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liujunbin
 */
public class WifiAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<ScanResult> wifiList;

    public WifiAdapter(Context context,List<ScanResult> dataList){
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.wifiList = dataList;


    }

    @Override
    public int getCount() {
        return wifiList.size();
    }

    @Override
    public ScanResult getItem(int position) {
        return wifiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.wifi_item,null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.wifiName.setText(getItem(position).SSID);

        return convertView;
    }

    class ViewHolder {
        TextView wifiName;
        ImageView checkedIcon;


        public ViewHolder(View convertView){
            wifiName = (TextView) convertView.findViewById(R.id.wifi_ssid);
            checkedIcon = (ImageView) convertView.findViewById(R.id.wifi_checked);
            convertView.setTag(this);
        }
    }

}
