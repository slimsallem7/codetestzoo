package com.zoostudio.custom.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zoostudio.custom.view.popup.OnValueSeekBarChanged;
import com.zoostudio.ngon.R;

public class ZooSeekBar extends View {
	private final static float DEVIATION = 1f;
	public static final float VERLOCITY = 0.04f;
	private static final int MOVE_LEFT = 0;
	private static final int MOVE_RIGHT = 1;
	private static final int MOVE_NONE = -1;
	private float mWidth;
	// Do dai cua 1 node
	private float mWidthNode;
	// Do cao cua 1 node
	private float mHeightNode;
	// So luong node
	private float mNumberOfPart;
	private Paint mPaintNode;
	private ArrayList<RectF> rectNodes;
	private float mDistance;
	private RectF mRectFBackground;
	// private float mCenterX;
	private float mDeviation;
	private RectF rectTest;
	private Paint mPaintTest;
	private boolean isAnimating;
	private boolean isDragging;
	private int mCurrentValue;
	private float newX;
	private Bitmap mSeekBitmap;

	private float mRadiusCircle;
	private float mCenterY;
	private Paint mPaintCircle;

	private float mPostionXCircle;
	private float mPostionYCircle;
	// private RectF mBoundCheck;

	private float mRealWidth;
	private float mHeightAxist;
	private RectF mBoundSeek;
	private float mLastX;
	private float mAmountPixel;
	private float mCurrentX;
	private OnValueSeekBarChanged mOnValueChanged;
	private boolean mBoundRight;
	private boolean mBoundLeft;
	private int mCurrrentMoving;

	private int mHeightSeek;
	private int mHeightView;
	private int mWidthSeek;
	// private float mDistanceToLeft;
	private float mTempNewX;
	private boolean isSelect;

	// private int mPaddingTop;
	private int mLastValue;

	// private ArrayList<String> titles;
	private Paint mPaintTitle;
	// private Paint mPaintTitleSelected;
	private float textSize;
	private int mColorTitle;
	private Drawable mDrawableSeekId;
	private boolean mShowNode;

	public ZooSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		loadDefault(context);
		loadAttributes(context, attrs);
		initVariables(context);
	}

	public ZooSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadDefault(context);
		loadAttributes(context, attrs);
		initVariables(context);
	}

	public ZooSeekBar(Context context) {
		super(context);
		loadDefault(context);
		initVariables(context);
	}

	public void setTitles(ArrayList<String> titles) {
		// this.titles = titles;
		this.mNumberOfPart = titles.size();
		initSeekVariable();
		initSeekBar();
	}

	private void loadDefault(Context context) {
		mHeightAxist = context.getResources().getDimension(
				R.dimen.seek_bar_line_thickness);
		mWidthNode = context.getResources().getDimension(
				R.dimen.seek_bar_node_thickness);
	}

	private void loadAttributes(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx
				.obtainStyledAttributes(attrs, R.styleable.ZooSeekBar);
		mHeightAxist = a.getDimension(R.styleable.ZooSeekBar_lineThickness,
				mHeightAxist);
		mWidthNode = a.getDimension(R.styleable.ZooSeekBar_nodeThickness,
				mWidthNode);
		mDrawableSeekId = a.getDrawable(R.styleable.ZooSeekBar_thumb);
		mShowNode = a.getBoolean(R.styleable.ZooSeekBar_showNode, true);
		mHeightNode = mWidthNode * 9;
		mNumberOfPart = 5;
		mLastValue = 0;
		mCurrentValue = 0;
		a.recycle();
	}

	private void initVariables(Context context) {

		mBoundLeft = true;
		mBoundRight = false;
		isAnimating = false;
		isDragging = false;
		// mBoundCheck = new RectF();
		mPostionXCircle = 0;
		mPostionYCircle = 0;
		newX = 0;

		mSeekBitmap = ((BitmapDrawable) mDrawableSeekId).getBitmap();
		// Tinh chieu cao cua View
		mHeightView = mSeekBitmap.getHeight();

		// Tinh chieu cao, chieu rong cua Seek
		mHeightSeek = mSeekBitmap.getHeight();
		mWidthSeek = mSeekBitmap.getWidth();

		mPostionYCircle = mHeightView / 2 - mHeightSeek / 2;

		// Tinh toan ban kinh cua seek
		mRadiusCircle = mWidthSeek / 2;
		// Tinh chieu cao cua Truc X
		// mHeightAxist = mHeightSeek / 10;

		mPaintCircle = new Paint();
		mPaintCircle.setAntiAlias(true);

		// Khoi tao cac node
		rectNodes = new ArrayList<RectF>();
		mRectFBackground = new RectF();

		for (int i = 0; i <= mNumberOfPart; i++) {
			rectNodes.add(new RectF());
		}
		// /
		// Khoi tao cac paint
		mPaintNode = new Paint();
		mPaintNode.setAntiAlias(true);
		mPaintNode.setColor(Color.rgb(120, 140, 23));

		mPaintTest = new Paint();
		mPaintTest.setAntiAlias(true);
		mPaintTest.setColor(Color.RED);
		mPaintTest.setStrokeWidth(1);
		mPaintTest.setStyle(Style.STROKE);
		//

		// Khoi tao paint cho Title
		textSize = 14;
		mColorTitle = Color.BLACK;
		mPaintTitle = new Paint();
		mPaintTitle.setTextSize(textSize);
		mPaintTitle.setColor(mColorTitle);

		rectTest = new RectF();
	}

	private void initSeekBar() {

		mPostionXCircle = (mCurrentValue * mDistance);

		if (mPostionXCircle <= 0) {
			mBoundLeft = true;
		}

		if ((mPostionXCircle + mRadiusCircle * 2) >= mWidth) {
			mBoundRight = true;
		}

		mBoundSeek = new RectF(mPostionXCircle, mPostionYCircle,
				mPostionXCircle + mWidthSeek, mPostionYCircle + mHeightSeek);

	}

	private void initSeekVariable() {
		float offsetX = mRadiusCircle;
		float offsetY = 0;

		mDistance = mRealWidth / mNumberOfPart;

		offsetY = mCenterY - mHeightNode / 2;

		for (int i = 0; i <= mNumberOfPart; i++) {
			if (i == mNumberOfPart) {
				rectNodes.get(i).set((mRealWidth + mRadiusCircle) - mWidthNode,
						offsetY, mRealWidth + mRadiusCircle,
						offsetY + mHeightNode);
			} else {
				rectNodes.get(i).set(offsetX, offsetY, offsetX + mWidthNode,
						offsetY + mHeightNode);
				offsetX += mDistance;
				offsetX -= mWidthNode / 2;
			}
		}
	}

	private void initNodes() {
		rectTest.set(0, 0, mWidth - 1, mHeightView - 1);
		mRealWidth = mWidth - mRadiusCircle * 2;
		mCenterY = mHeightView / 2;

		// Tinh toan Rect cua background
		mRectFBackground.set(mRadiusCircle, mCenterY - mHeightAxist / 2,
				mRealWidth + mRadiusCircle, mCenterY + mHeightAxist / 2);
		//

		initSeekVariable();
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {

		mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
		Log.i("Zoo", "Width  = " + mWidth);
		initNodes();
		initSeekBar();

		setMeasuredDimension(resolveSize((int) mWidth, widthMeasureSpec),
				resolveSize(mSeekBitmap.getHeight(), heightMeasureSpec));
	}

	private void drawBackground(Canvas canvas) {
		canvas.drawRect(mRectFBackground, mPaintNode);
	}

	// private void drawTest(Canvas canvas) {
	// canvas.drawRect(rectTest, mPaintTest);
	// }

	private void drawSeek(Canvas canvas) {
		canvas.drawBitmap(mSeekBitmap, null, mBoundSeek, mPaintCircle);
	}

	private void drawNode(Canvas canvas) {
		if (!mShowNode)
			return;
		for (RectF rect : rectNodes) {
			canvas.drawRect(rect, mPaintNode);
		}
	}

	// private void drawTitles(Canvas canvas) {
	//
	// }

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// drawTest(canvas);
		drawBackground(canvas);
		drawNode(canvas);
		drawSeek(canvas);
	}

	private boolean isContain(float x) {
		if (mBoundSeek.left - 10 <= x && mBoundSeek.right + 10 >= x) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isContain(event.getX()) && isAnimating == false) {
				mLastX = event.getX();
				// mDistanceToLeft = mLastX - mBoundSeek.left;
				isAnimating = false;
				isDragging = true;
			} else {
				isSelect = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			isSelect = false;
			mCurrentX = event.getX();
			mCurrrentMoving = getDirectionMoving(mCurrentX, mLastX);

			handleMove(event);
			mLastX = mCurrentX;
			break;
		case MotionEvent.ACTION_UP:
			// handleMove(event);
			Log.i("SEEK", "ACTION UP");
			// if (isSelect) {
			// Log.i("SEEK", "ACTION UP selected");
			// isSelect = false;
			// currentValue = getCurrentIndex(event.getX());
			// excuteAnimation();
			// }
		case MotionEvent.ACTION_CANCEL:
			Log.i("SEEK", "ACTION_CANCEL");
			if (isDragging == true) {
				isAnimating = true;
				mCurrentValue = getCurrentIndex(mBoundSeek.centerX());
				excuteAnimation();
				isDragging = false;
			} else if (isSelect) {
				Log.i("SEEK", "ACTION_CANCEL selected");
				isSelect = false;
				isAnimating = true;
				mCurrentValue = getCurrentIndex(event.getX());
				excuteAnimation();
			}
			break;
		}
		return true;
	}

	private void excuteAnimation() {
		newX = rectNodes.get(mCurrentValue).centerX() - mRadiusCircle;
		mPostionXCircle = mBoundSeek.left;
		mCurrrentMoving = getDirectionMoving(newX, mPostionXCircle);
		if (mCurrrentMoving == MOVE_LEFT) {
			mBoundRight = false;
		} else if (mCurrrentMoving == MOVE_RIGHT) {
			mBoundLeft = false;
		}
		new BackgroundAnimator().execute();
	}

	private int getCurrentIndex(float x) {
		for (int i = 0; i <= mNumberOfPart; i++) {
			if (i < mNumberOfPart && (rectNodes.get(i).centerX()) < x
					&& x < (rectNodes.get(i + 1).centerX())) {
				float distanceA = x - rectNodes.get(i).centerX();
				float distanceB = rectNodes.get(i + 1).centerX() - x;
				float rs = Math.min(distanceA, distanceB);
				if (rs == distanceA) {
					return i;
				} else {
					return (i + 1);
				}
			} else if (i == 0 && x < rectNodes.get(i).centerX()) {
				return i;
			} else if (i == mNumberOfPart && x > rectNodes.get(i).centerX()) {
				return i;
			}
		}
		return 0;
	}

	private void handleMove(MotionEvent event) {
		if (!isDragging) {
			return;
		}
		if (mCurrrentMoving == MOVE_LEFT && mBoundLeft) {
			return;
		} else if (mCurrrentMoving == MOVE_RIGHT && mBoundRight) {
			return;
		}

		isAnimating = false;
		mAmountPixel = mCurrentX - mLastX;
		mTempNewX = mBoundSeek.left + mAmountPixel;
		if (mCurrrentMoving == MOVE_LEFT && mTempNewX <= 0) {
			mAmountPixel = -mBoundSeek.left;
			mBoundLeft = true;
			// mAmountPixel = mAmountPixel + ( mBoundSeek.left + mAmountPixel);
		} else if (mCurrrentMoving == MOVE_RIGHT
				&& (mTempNewX + 2 * mRadiusCircle) >= mWidth) {
			mBoundRight = true;
			mAmountPixel = mWidth - mBoundSeek.right;
		}
		mBoundSeek.offset(mAmountPixel, 0);

		// if (mBoundSeek.left < 0) {
		// mBoundSeek.offset(mBoundSeek.left, 0);
		// mBoundLeft = true;
		// }
		//
		// if (mBoundSeek.right > mWidth) {
		// mBoundSeek.offset(-(mWidth - mBoundSeek.right), 0);
		// mBoundRight = true;
		// }
		invalidate();
	}

	private int getDirectionMoving(float currentX, float lastX) {
		Log.i("SEEK", "Current X = " + currentX + " | Last X = " + lastX);
		if (currentX > lastX) {
			mBoundLeft = false;
			return MOVE_RIGHT;
		} else if (currentX < lastX) {
			mBoundRight = false;
			return MOVE_LEFT;
		} else {
			return MOVE_NONE;
		}
	}

	public class BackgroundAnimator extends AsyncTask<Object, Object, Object> {
		private float mSpeed = 0.04f;

		// private float mMoving;

		public Object doInBackground(Object... args) {
			mDeviation = Math.abs(mPostionXCircle - newX);

			while (mDeviation > DEVIATION) {
				Log.i("Animation", "mDeviation = " + mDeviation);
				if (isAnimating == true) {
					if (mPostionXCircle > newX) {
						mAmountPixel = -1f - mSpeed;
					} else {
						mAmountPixel = 1f + mSpeed;
					}
					mDeviation -= (1f + mSpeed);
					mSpeed += VERLOCITY;
					if (mDeviation < 0) {
						mAmountPixel = mBoundSeek.left - newX;
						mBoundSeek.offset(mAmountPixel, 0);
					} else {
						mBoundSeek.offset(mAmountPixel, 0);
					}
					postInvalidate();
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					return null;
				}
			}
			isAnimating = false;
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (null != ZooSeekBar.this.mOnValueChanged
					&& mLastValue != mCurrentValue) {
				mLastValue = mCurrentValue;
				ZooSeekBar.this.mOnValueChanged.onValueChanged(mCurrentValue);
			}
		}
	}

	public void setOnValueChangeListener(OnValueSeekBarChanged listener) {
		this.mOnValueChanged = listener;
	}

	public void setCurrentValue(int value) {
		if (value != 0) {
			mBoundLeft = false;
			if (value != 5)
				mBoundRight = false;

			if (value == 0)
				mBoundLeft = true;
			if (value == 5)
				mBoundRight = true;

			this.mCurrentValue = value;
			this.mLastValue = mCurrentValue;
			this.invalidate();
		}
	}

}
