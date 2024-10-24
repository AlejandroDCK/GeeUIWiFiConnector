package com.letianpai.robot.wificonnet.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.letianpai.robot.wificonnet.R
import com.letianpai.robot.wificonnet.callback.KeyPressCallback
import com.letianpai.robot.wificonnet.view.KeyButton

/**
 * @author liujunbin
 */
class CustomAdapter(private val data: List<String>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.textView.setText(data.get(position));
        holder.keyButton.text = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var keyButton: KeyButton = itemView.findViewById(R.id.keyButton)

        init {
            keyButton.setOnClickListener { v ->
                val buttonText = (v as Button).text.toString() + ""
                if (!TextUtils.isEmpty(buttonText)) {
                    KeyPressCallback.instance
                        .pressKey(KeyPressCallback.KEY_TYPE_VALUE, buttonText)
                }
                //                    Log.e("letianpai_test","buttonText: "+ buttonText);
            }
        }
    }
}
