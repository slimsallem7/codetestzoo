package com.zoostudio.ngon.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoostudio.ngon.R;

public class CheckBoxExtended extends LinearLayout {
	private boolean mChecked;
	private OnCheckedChangeListener mOnCheckedChangeListener;
	private boolean mBroadcasting = false;
	private CheckBox mCheckBox;
	private TextView mTitle;

	public CheckBoxExtended(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initLayout();
		initDefaults(attrs);
	}

	public CheckBoxExtended(Context context) {
		super(context);
		
		initLayout();
	}

	private void initLayout() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_checkboxextended, this);
		
		mCheckBox = (CheckBox) findViewById(R.id.checkBox);
		mTitle = (TextView) findViewById(R.id.title);
	}

	private void initDefaults(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NgonDo_Views);
		
		String text = a.getString(R.styleable.NgonDo_Views_text);
		if (text != null) mTitle.setText(text);
		a.recycle();
		
		ColorStateList textColorStateList = null;
		int textColor = Color.BLACK;
		int textSize = 15;
		String textStyle = "normal";
		
		StringBuilder builder = new StringBuilder();
		
		TypedArray appearance = getContext().obtainStyledAttributes(attrs, R.styleable.TextAppearance);
		if (appearance != null) {
			int n = appearance.getIndexCount();
			for (int i = 0; i < n; i++) {
				int attr = appearance.getIndex(i);
				
				switch (attr) {
				case R.styleable.TextAppearance_textColor:
					textColorStateList = appearance.getColorStateList(attr);
					textColor = appearance.getColor(R.styleable.TextAppearance_textColor, textColor);
					break;
					
				case R.styleable.TextAppearance_textSize:
					textSize = appearance.getDimensionPixelOffset(attr, 0);
					builder.append("1:" + textSize).append("-");
					if (textSize == 0) {
						int sizeId = appearance.getResourceId(R.styleable.TextAppearance_textSize, 0);
						builder.append("2:" + sizeId).append("-");
						if (sizeId == 0) {
							textSize = 15;
						} else {
							textSize = getResources().getDimensionPixelOffset(sizeId);
						}
						builder.append("3:" + textSize);
					}
					break;
					
				case R.styleable.TextAppearance_textStyle:
					textStyle = appearance.getString(attr);
					break;

				default:
					break;
				}
			}
		}
		
		appearance.recycle();
		
		if (textColorStateList != null) mTitle.setTextColor(textColorStateList);
		else mTitle.setTextColor(textColor);
		
		mTitle.setTextSize(textSize);
		mTitle.setText("Text:" + builder.toString());
		
		if (textStyle.equals("bold")) {
			mTitle.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		} else if (textStyle.equals("italic")) {
			mTitle.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
		} else if (textStyle.equals("bold|italic")){
			mTitle.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC);	
		} else {
			mTitle.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
		}
	}
	
	public static interface OnCheckedChangeListener {
		void onCheckedChange(View v, boolean isChecked);
	}
	
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeListener = listener;
	}
	
	@Override
	public boolean performClick() {
		toggle();
		return super.performClick();
	}

	public void toggle() {
		setChecked(!mChecked);
	}

	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mChecked = checked;
			changeCheckedState();
			
			if (mBroadcasting ) {
				return;
			}
			
			mBroadcasting = true;
			if (mOnCheckedChangeListener != null) {
				mOnCheckedChangeListener.onCheckedChange(this, mChecked);
			}
			
			mBroadcasting = false;
		}
	}

	private void changeCheckedState() {
		mCheckBox.setChecked(mChecked);
	}
	
	public void setTitle(String title) {
		if (mTitle != null) {
			mTitle.setText(title);
		}
	}

	public void setTextColor(int color) {
		mTitle.setTextColor(color);
	}
}
