package com.letianpai.robot.wificonnet.view

import android.content.Context
import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.letianpai.robot.wificonnet.R

/**
 * @author liujunbin
 */
class WifiAdapter(private val mContext: Context?, private val wifiList: List<ScanResult>) :
    BaseAdapter() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getCount(): Int {
        return wifiList.size
    }

    override fun getItem(position: Int): ScanResult {
        return wifiList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.wifi_item, null)
            ViewHolder(convertView)
        }
        val holder = convertView.tag as ViewHolder
        holder.wifiName.text = getItem(position).SSID

        return convertView
    }

    internal inner class ViewHolder(convertView: View) {
        var wifiName: TextView = convertView.findViewById<View>(R.id.wifi_ssid) as TextView
        var checkedIcon: ImageView = convertView.findViewById<View>(R.id.wifi_checked) as ImageView


        init {
            convertView.tag = this
        }
    }
}
