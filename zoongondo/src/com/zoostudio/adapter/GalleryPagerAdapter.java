package com.zoostudio.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.zoostudio.adapter.NgonGridMediaAdapter.OnItemSelectListener;
import com.zoostudio.adapter.item.MediaItem;
import com.zoostudio.android.image.ZooAjustImageView;
import com.zoostudio.ngon.R;
import com.zoostudio.ngon.views.PhotoToggleButton;

public class GalleryPagerAdapter extends PagerAdapter {

	protected ArrayList<MediaItem> data;
	private LayoutInflater inflater;
	private View view;
	private ZooAjustImageView imageView;
	private PhotoToggleButton button;
	private OnItemSelectListener listener;
	
	public GalleryPagerAdapter(Activity activity, ArrayList<MediaItem> datas) {
		this.data = datas;
		this.inflater = activity.getLayoutInflater();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	/**
	 * Create the page for the given position. The adapter is responsible for
	 * adding the view to the container given here, although it only must ensure
	 * this is done by the time it returns from {@link #finishUpdate()}.
	 * 
	 * @param container
	 *            The containing View in which the page will be shown.
	 * @param position
	 *            The page position to be instantiated.
	 * @return Returns an Object representing the new page. This does not need
	 *         to be a View, but can be some other container of the page.
	 */
	@Override
	public Object instantiateItem(View collection, int position) {
		Log.i("Gallery", "Instantiate Item" + position);
		view = inflater.inflate(R.layout.page_item, null);
		imageView = (ZooAjustImageView) view.findViewById(R.id.imageMedia);
		button = (PhotoToggleButton) view.findViewById(R.id.check_image);

		imageView.setImagePath(data.get(position).getPathMedia());
		button.setChecked(data.get(position).isSelected());
		button.setOnCheckedChangeListener(new ZooMediaCheck(position, data
				.get(position).getIdMedia()));
		((ViewPager) collection).addView(view, 0);
		return view;
	}
	
	public void setData(ArrayList<MediaItem> data) {
		this.data = data;
		notifyDataSetChanged();
	}
	/**
	 * Remove a page for the given position. The adapter is responsible for
	 * removing the view from its container, although it only must ensure this
	 * is done by the time it returns from {@link #finishUpdate()}.
	 * 
	 * @param container
	 *            The containing View from which the page will be removed.
	 * @param position
	 *            The page position to be removed.
	 * @param object
	 *            The same object that was returned by
	 *            {@link #instantiateItem(View, int)}.
	 */
	@Override
	public void destroyItem(View collection, int position, Object view) {
		Log.i("Gallery", "Destroy =" + position);
		((ViewPager) collection).removeView((RelativeLayout) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	/**
	 * Called when the a change in the shown pages has been completed. At this
	 * point you must ensure that all of the pages have actually been added or
	 * removed from the container as appropriate.
	 * 
	 * @param container
	 *            The containing View which is displaying this adapter's page
	 *            views.
	 */
	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

	private class ZooMediaCheck implements OnCheckedChangeListener {
		int position;
		long idMedia;

		public ZooMediaCheck(int position, long idMedia) {
			this.position = position;
			this.idMedia = idMedia;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			GalleryPagerAdapter.this.data.get(position).setSelected(isChecked);
			listener.onMediaChanged(isChecked, idMedia,
					GalleryPagerAdapter.this.data.get(position));
		}
	}

	public void setOnItemSelectListener(OnItemSelectListener listener) {
		this.listener = listener;
	}
}
