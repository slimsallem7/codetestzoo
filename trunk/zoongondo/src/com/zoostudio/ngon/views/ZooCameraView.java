package com.zoostudio.ngon.views;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ZooCameraView extends SurfaceView implements
		SurfaceHolder.Callback {
	private SurfaceHolder mSurfaceHolder;
	private Camera mCamera;
	private int LARGEST_WIDTH;
	private int LARGEST_HEIGHT;
	private OnFocusListener mSaveImageListener;
	private Parameters parameters;

	public ZooCameraView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ZooCameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ZooCameraView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (LARGEST_HEIGHT == 0 && LARGEST_WIDTH == 0) {
			LARGEST_WIDTH = MeasureSpec.getSize(widthMeasureSpec);
			LARGEST_HEIGHT = MeasureSpec.getSize(heightMeasureSpec);
			Log.i("Camera", "onMeasure =" + LARGEST_WIDTH + "|"
					+ LARGEST_HEIGHT);
		}
	}

	public ZooCameraView(Context context, Camera camera,
			OnFocusListener listener) {
		super(context);
		this.mSaveImageListener = listener;
		this.mCamera = camera;
		parameters = this.mCamera.getParameters();

		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

		List<String> scenemodes = mCamera.getParameters()
				.getSupportedSceneModes();
		if (scenemodes != null)
			if (scenemodes.indexOf(Camera.Parameters.SCENE_MODE_STEADYPHOTO) != -1) {
				parameters
						.setSceneMode(Camera.Parameters.SCENE_MODE_STEADYPHOTO);
			}

		this.mCamera.setParameters(parameters);
		this.mSurfaceHolder = this.getHolder();
		this.mSurfaceHolder.addCallback(this);
		this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mCamera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		try {
			Log.i("Camera", "surfaceCreated");
			getBestSize();
			mCamera.setPreviewDisplay(surfaceHolder);
		} catch (Exception e) {
			e.printStackTrace();
			mCamera.release();
		}
	}

	public boolean getMaxSize(int rotation) {
		Camera.Parameters parameters = mCamera.getParameters();
		List<Size> size = parameters.getSupportedPictureSizes();
		Iterator<Camera.Size> cei = size.iterator();
		int bestWith = 0;
		int bestHeight = 0;
		while (cei.hasNext()) {
			Camera.Size aSize = cei.next();
			if (bestWith <= aSize.width && bestHeight <= aSize.height) {
				bestWith = aSize.width;
				bestHeight = aSize.height;
			}
		}
		parameters.setPictureSize(bestWith, bestHeight);
		parameters.setJpegQuality(100);
		parameters.setPictureFormat(PixelFormat.JPEG);
		try {
			mCamera.setParameters(parameters);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public float getBestSize() {
		Camera.Parameters parameters = mCamera.getParameters();
		int orient = getContext().getResources().getConfiguration().orientation;
		if (orient != Configuration.ORIENTATION_LANDSCAPE) {
			parameters.set("orientation", "portrait");
			mCamera.setDisplayOrientation(90);
			parameters.setRotation(90);
		}
		parameters.setPreviewSize(LARGEST_WIDTH, LARGEST_HEIGHT);
		try {
			mCamera.setParameters(parameters);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		return 0;

	}

	private Camera.AutoFocusCallback focusCallback = new AutoFocusCallback() {
		@Override
		public synchronized void onAutoFocus(boolean sucess, Camera camera) {
			isFocusing = false;
			if (sucess) {
				mSaveImageListener.onFocus();
			} else {
				mSaveImageListener.onNormal();
			}
		}
	};

	private volatile boolean isFocusing;
	private boolean turnOnflash;

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
	}

	public void turnOffFlash() {
		turnOnflash = false;

	}

	public void turnOnFlash() {
		turnOnflash = true;
	}

	public void preTakeCamera() {
		String value = turnOnflash ? Parameters.FLASH_MODE_ON
				: Parameters.FLASH_MODE_OFF;
		parameters.setFlashMode(value);
		mCamera.setParameters(parameters);
	}
	
	public void onTakeCameraDone(){
		parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
		mCamera.setParameters(parameters);
	}
	
	public interface OnFocusListener {
		public void onFocus();

		public void onNormal();
	}

	public synchronized void focusChange() {
		if (isFocusing) {
			return;
		} else {
			mCamera.autoFocus(focusCallback);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mSaveImageListener.onNormal();
			mCamera.autoFocus(focusCallback);
		}
		return super.onTouchEvent(event);
	}
}
