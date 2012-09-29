package com.zoostudio.zooslideshow;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.zoostudio.android.image.SmartImageView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ZooLikerView extends LinearLayout {
	private ArrayList<LikerItem> mLikers;
	private int mTotal;
	
	public ZooLikerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVariables();
	}

	public ZooLikerView(Context context) {
		super(context);
	}
	
	private void initVariables(){
		mLikers = new ArrayList<LikerItem>();
		mTotal = 0;
	}
	
	public void setDatas(ArrayList<LikerItem> urlImages,int total){
		this.mLikers = urlImages;
		this.mTotal = total;
		setUpViews();
	}
	
	private void setUpViews(){
		if(mLikers.size() ==0){
			this.setVisibility(View.GONE);
			return;
		}
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 10, 0, 10);
		for(int i = 0, n = mLikers.size(); i < n; i++){
			View view = inflate(getContext(),R.layout.item_liker, null);
			LikerItem item = mLikers.get(i);
			SmartImageView imageView = (SmartImageView) view.findViewById(R.id.imagAvatarLiker);
//			imageView.setImageUrl(item.getUrlAvatar());
			TextViewDetailLikeCount likeCount = (TextViewDetailLikeCount) view.findViewById(R.id.txtTotalLike);
			likeCount.setText("+" + item.getLikeCount());
			addView(view, params);
		}
		
		if(mTotal == 0) return;
		RelativeLayout layoutWrapPlush = new RelativeLayout(getContext());
		layoutWrapPlush.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams paramWrap = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT,1);
		addView(layoutWrapPlush, paramWrap);
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView txtViewPlus = (TextView) inflater.inflate(R.layout.totallike, null);
		txtViewPlus.setText(mTotal + "+");
		layoutWrapPlush.addView(txtViewPlus);
	}
}
