package com.zoostudio.ngon.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.ui.base.BaseFragmentScreen;

public class SelectMenu extends BaseFragmentScreen implements OnClickListener {
	private Button btnAddDish;
	private ListView lvDish;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.activity_select_menu, null);
		return mView;
	}

	@Override
	protected void initControls() {
		btnAddDish = (Button) mView.findViewById(R.id.btn_add_dish);
		btnAddDish.setOnClickListener(this);
		lvDish = (ListView) mView.findViewById(R.id.dish_list);
		lvDish.setEmptyView(mView.findViewById(R.id.no_dish));
	}

	@Override
	protected void initVariables() {
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_dish:
			break;
		}
	}

	@Override
	protected void initActions() {
		// TODO Auto-generated method stub
		
	}

}
