package com.zoostudio.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zoostudio.adapter.item.MenuItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.ngon.R;

public class SpotMenuAdapter extends ArrayAdapter<MenuItem> {

    private LayoutInflater mInflater;

    public SpotMenuAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public SpotMenuAdapter(Context context, int textViewResourceId,ArrayList<MenuItem> datas) {
        super(context, textViewResourceId,datas);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuItem item = getItem(position);
        ViewHolder holder;

        if (null == convertView) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_menu, null);
            holder.menuName = (TextView) convertView.findViewById(R.id.txtNameMenu);
            holder.menuPrice = (TextView) convertView.findViewById(R.id.menu_price);
            holder.imgMenu = (SmartImageView) convertView.findViewById(R.id.menu_image);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.menuName.setText(item.getName());
//        holder.menuPrice.setText(item.getPrice());
        
        return convertView;
    }

    private static class ViewHolder {
        private TextView menuName;
        private SmartImageView imgMenu;
        private TextView menuPrice;
    }
    
    
}
