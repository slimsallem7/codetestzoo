package com.zoostudio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoostudio.adapter.item.ReviewItem;
import com.zoostudio.ngon.R;
 
public class ReviewAdapter extends ArrayAdapter<ReviewItem> {

    private LayoutInflater mInflater;

    public ReviewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewItem item = getItem(position);
        ViewHolder holder;

        if (null == convertView) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_review, null);

            holder.username = (TextView) convertView.findViewById(R.id.review_username);
            holder.content = (TextView) convertView.findViewById(R.id.review_content);
            holder.time = (TextView) convertView.findViewById(R.id.review_time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.review_avatar);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.username.setText(item.getUser().getName());
        holder.content.setText(item.getContent());

        return convertView;
    }

    private static class ViewHolder {
        private TextView username, content, time;
        private ImageView avatar;
    }
}
