package com.zoostudio.android.image;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import com.example.maskimage.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.widget.ImageView;
import android.util.AttributeSet;

public class ZooAvatarImageView extends ImageView {
	private static final int LOADING_THREADS = 4;
	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(LOADING_THREADS);

	private SmartImageTask currentTask;
	protected Paint paint;
	protected WebImage border;

	public ZooAvatarImageView(Context context) {
		super(context);
		initVariables();
	}

	public ZooAvatarImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVariables();
	}

	public ZooAvatarImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initVariables();
	}

	protected void initVariables() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		border = new WebImage("ZooBorderAvatar");
		Bitmap source =BitmapFactory.decodeStream(this.getContext().getResources().openRawResource(R.drawable.ic_avatar_default));
		setImageBitmap(source);
	}

	// Helpers to set image by URL
	public void setImageUrl(String url) {
		setImage(new WebImage(url));
	}

	public void setImageUrl(String url, final Integer fallbackResource) {
		setImage(new WebImage(url), fallbackResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(new WebImage(url), fallbackResource, loadingResource);
	}

	// Helpers to set image by contact address book id
	public void setImageContact(long contactId) {
		setImage(new ContactImage(contactId));
	}

	public void setImageContact(long contactId, final Integer fallbackResource) {
		setImage(new ContactImage(contactId), fallbackResource);
	}

	public void setImageContact(long contactId, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(new ContactImage(contactId), fallbackResource,
				fallbackResource);
	}

	// Set image using SmartImage object
	public void setImage(final SmartImage image) {
		setImage(image, null, null);
	}

	public void setImage(final SmartImage image, final Integer fallbackResource) {
		setImage(image, fallbackResource, fallbackResource);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource, final Integer loadingResource) {
		// Set a loading resource
		if (loadingResource != null) {
			setImageResource(loadingResource);
		}

		// Cancel any existing tasks for this image view
		if (currentTask != null) {
			currentTask.cancel();
			currentTask = null;
		}

		// Set up the new task
		currentTask = new SmartImageTask(getContext(), image);
		currentTask
				.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
					@Override
					public void onComplete(Bitmap bitmap) {
						if (bitmap != null) {
							setImageBitmap(bitmap);
						} else {
							// Set fallback resource
							if (fallbackResource != null) {
								setImageResource(fallbackResource);
							}
						}
					}
				});
		// Run the task in a threadpool
		threadPool.execute(currentTask);
	}

	@Override
	public void setImageBitmap(Bitmap source) {
		Bitmap bg = BitmapFactory.decodeStream(getContext().getResources()
				.openRawResource(R.drawable.ic_bg_circle));
		Bitmap result = Bitmap.createBitmap(bg.getWidth(),
				bg.getHeight(), Config.ARGB_8888);
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(bg, 0, 0, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		paint.setXfermode(null);

		super.setImageBitmap(result);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap bitmap = border.getBorderBitmap(getContext());
		canvas.drawBitmap(bitmap, 0, 0, paint);
	}

	public static void cancelAllTasks() {
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}
}