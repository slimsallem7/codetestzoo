package com.zoostudio.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.zoostudio.adapter.item.PhotoItem;
import com.zoostudio.android.image.SmartImageView;
import com.zoostudio.ngon.R;

public class SpotPhotoAdapter extends ArrayAdapter<PhotoItem> {

    private LayoutInflater mInflater;
    private int mGalleryItemBackground;

    public SpotPhotoAdapter(Context context) {
        super(context, R.id.actionbar);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TypedArray attr = context.obtainStyledAttributes(R.styleable.HelloGallery);
        mGalleryItemBackground = attr.getResourceId(R.styleable.HelloGallery_android_galleryItemBackground, 0);
        attr.recycle();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoItem item = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.item_photo, null);
            viewHolder.image = (SmartImageView) convertView.findViewById(R.id.imgDishCheckin);
            viewHolder.image.setBackgroundResource(mGalleryItemBackground);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.image.setImageUrl(item.getMediumPath());

        return convertView;
    }

    private static class ViewHolder {
        private SmartImageView image;
    }

}
