package com.zoostudio.ngon.ui;

import android.widget.ListView;

import com.zoostudio.adapter.ReviewAdapter;
import com.zoostudio.adapter.item.ReviewItem;
import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;

public class ActivityReviewSpot extends NgonActivity{
	private ListView mListReview;
	private ReviewAdapter mReviewAdapter;
	
	@Override
	protected int setLayoutView() {
		return R.layout.activity_review_spot;
	}

	@Override
	protected void initControls() {
		mListReview = (ListView) findViewById(R.id.listReview);
	}

	@Override
	protected void initVariables() {
		mReviewAdapter = new ReviewAdapter(getApplicationContext(), -1);
		mReviewAdapter.add(new ReviewItem(0));
		mReviewAdapter.add(new ReviewItem(1));
		mReviewAdapter.add(new ReviewItem(2));
		mReviewAdapter.add(new ReviewItem(0));
		mReviewAdapter.add(new ReviewItem(1));
		mReviewAdapter.add(new ReviewItem(2));
		mReviewAdapter.add(new ReviewItem(0));
	}

	@Override
	protected void initActions() {
		mListReview.setAdapter(mReviewAdapter);
	}

}
