package com.zoostudio.ngon.ui;

import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoostudio.ngon.NgonActivity;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonDialog;
import com.zoostudio.ngon.utils.Logger;
import com.zoostudio.ngon.views.ButtonUp;
import com.zoostudio.ngon.views.CheckBoxExtended;

public class SearchActivity extends NgonActivity {
	private static final String TAG = "SearchActivity";
	private ButtonUp mUp;
	private Button mFilter;

	@Override
	protected int setLayoutView() {
		return R.layout.activity_search;
	}

	@Override
	protected void initControls() {
		mUp = (ButtonUp) findViewById(R.id.btn_up);
		mFilter = (Button) findViewById(R.id.filter);
	}

	@Override
	protected void initVariables() {

	}

	@Override
	protected void initActions() {
		mUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		mFilter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogFilter();
			}
		});
	}

	protected void showDialogFilter() {
		NgonDialog.Builder builder = new NgonDialog.Builder(this);
		builder.setTitle(getString(R.string.dialog_searchfilter_title));
		builder.setNegativeButton(getString(R.string.dialog_close), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		View content = getLayoutInflater().inflate(R.layout.dialog_searchfilter, null);
		content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		builder.setInnerCustomView(content);
		
		NgonDialog dialog = builder.create();
		
		TextView selectDistance = (TextView) content.findViewById(R.id.current_distance);
		selectDistance.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogDistancesFilter();
			}
		});
		
		dialog.show();
	}

	private void showDialogDistancesFilter() {
		String[] distances = {"1km", "2km", "3km", "4km", "5km"};
		
		NgonDialog.Builder builder = new NgonDialog.Builder(this);
		builder.setTitle(getString(R.string.dialog_searchfilter_distance).toUpperCase());
		
		LinearLayout content = new LinearLayout(getApplicationContext());
		content.setOrientation(LinearLayout.VERTICAL);
		content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		builder.setInnerCustomView(content);
		
		final NgonDialog dialog = builder.create();

		int padding = getResources().getDimensionPixelOffset(R.dimen.padding_medium);
		
		for (int i = 0; i < distances.length; i++) {
			CheckBoxExtended ck = new CheckBoxExtended(getApplicationContext());
			ck.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 
					LinearLayout.LayoutParams.WRAP_CONTENT));
			ck.setPadding(padding, padding, padding, padding);
			ck.setTitle(distances[i]);
			ck.setTextColor(getResources().getColor(R.color.NgonDoBlack));

			content.addView(ck);
			Logger.e(TAG, "add + 1");
			
			ck.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			if (i == distances.length - 1) continue;
			
			View divider = new View(getApplicationContext());
			divider.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 
					getResources().getDimensionPixelOffset(R.dimen.dimen2)));
			divider.setBackgroundResource(R.drawable.img_horizontaldivider_pattern);
			content.addView(divider);
		}
		
		dialog.show();
	}
}
