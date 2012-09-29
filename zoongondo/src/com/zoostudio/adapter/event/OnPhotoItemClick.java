package com.zoostudio.adapter.event;

import com.zoostudio.adapter.item.PhotoItem;
import com.zoostudio.ngon.ui.ViewPhotoActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnPhotoItemClick implements OnItemClickListener {

    private Activity mActvitiy;

    public OnPhotoItemClick(Activity activity) {
        mActvitiy = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
        PhotoItem item = (PhotoItem) adapterView.getItemAtPosition(pos);

        Intent intent = new Intent(mActvitiy, ViewPhotoActivity.class);
        intent.putExtra("photo_id", item.getId());
        intent.putExtra("path", item.getPath());

        mActvitiy.startActivity(intent);
    }

}
