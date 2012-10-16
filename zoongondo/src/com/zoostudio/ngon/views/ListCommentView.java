package com.zoostudio.ngon.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoostudio.adapter.item.ReviewItem;
import com.zoostudio.android.image.ZooAvatarImageView;
import com.zoostudio.ngon.R;

public class ListCommentView extends LinearLayout implements OnClickListener {
	private ArrayList<ReviewItem> datas;
	private Button viewMore;
	private OnViewAllCommentListener listener;

	public ListCommentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVariables();
	}

	public ListCommentView(Context context) {
		super(context);
		initVariables();
	}

	private void initVariables() {
		datas = new ArrayList<ReviewItem>();
		this.setOrientation(LinearLayout.VERTICAL);
	}

	public void setOnViewAllCommentListener(OnViewAllCommentListener listener) {
		this.listener = listener;
	}

	public void setDatas(ArrayList<ReviewItem> datas) {
		this.datas = datas;
		setUpViews();
	}

	private void setUpViews() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		TextView mTitle = new TextView(getContext());
		String strTitle = getContext().getString(R.string.string_comment);
		float textSize = getContext().getResources().getDimensionPixelOffset(
				R.dimen.text_size_title_review);
		mTitle.setText(strTitle.toUpperCase());
		mTitle.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		mTitle.setTextSize(textSize);
		mTitle.setTextColor(Color.parseColor("#c63321"));
		mTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		LinearLayout.LayoutParams paramsTitle = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		addView(mTitle, paramsTitle);

		for (int i = 0, n = datas.size(); i < n; i++) {
			View item = inflater.inflate(R.layout.item_review, null);

			ReviewItem data = datas.get(i);

			ZooAvatarImageView avatar = (ZooAvatarImageView) item
					.findViewById(R.id.review_avatar);
			avatar.setImageUrl(data.getUser().getAvatar());
			TextView userName = (TextView) item
					.findViewById(R.id.review_username);
			userName.setText("vietbq");
			TextView reviewTime = (TextView) item
					.findViewById(R.id.review_time);
			reviewTime.setText("19/7/2012");
			TextView reviewContent = (TextView) item
					.findViewById(R.id.review_content);
			reviewContent.setText("Test");
			addView(item);
		}

		viewMore = new Button(getContext());
		String strTitleBtn = getContext().getString(
				R.string.title_btn_view_all_comment);
		viewMore.setText(strTitleBtn);

		int dimen5 = getResources().getDimensionPixelOffset(R.dimen.dimen5);
		viewMore.setTextColor(Color.WHITE);
		viewMore.setPadding(dimen5 * 2, dimen5, dimen5 * 3, dimen5 * 2);
		viewMore.setGravity(Gravity.CENTER_VERTICAL);
		viewMore.setBackgroundColor(Color.parseColor("#b72a32"));
		LinearLayout.LayoutParams paramViewMore = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramViewMore.gravity = Gravity.RIGHT;
		paramViewMore.setMargins(0, dimen5 * 3, 0, dimen5);
		addView(viewMore, paramViewMore);
		viewMore.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (null != listener)
			listener.onViewAllCommentClick();
	}

	public interface OnViewAllCommentListener {
		public void onViewAllCommentClick();
	}
}
