package com.zoostudio.ngon.ui;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;

import com.zoostudio.adapter.NgonListImageAdapter;
import com.zoostudio.adapter.item.WrapItem;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.ngon.views.ImageRollIndicator;

public class ViewPhotoActivity extends NgonActivity {
	

	private  NgonListImageAdapter adapter;
	private ImageRollIndicator mIndicator;
	private Gallery mGallery;
	private ButtonUp mUp;
	
    @Override
    protected int setLayoutView() {
        return R.layout.activity_view_photo;
    }

    @Override
    protected void initControls() {
    	mGallery = (Gallery) findViewById(R.id.imageroll);
    	mIndicator = (ImageRollIndicator) findViewById(R.id.image_indicator);
    	mUp = (ButtonUp) findViewById(R.id.btn_up);
    }

    @Override
    protected void initVariables() {
    	ArrayList<WrapItem> data = new ArrayList<WrapItem>();
    	data.add(new WrapItem("1"));
    	data.add(new WrapItem("2"));
    	data.add(new WrapItem("3"));
    	data.add(new WrapItem("4"));
    	data.add(new WrapItem("5"));
    	
    	adapter = new NgonListImageAdapter(getApplicationContext(), 0, data,this);
    	mGallery.setAdapter(adapter);
    	mIndicator.setPageCount(adapter.getCount());
    	
    }

	@Override
	protected void initActions() {
    	mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long arg3) {
				mIndicator.setSelectedPage(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
    	
    	mUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
	}

}
