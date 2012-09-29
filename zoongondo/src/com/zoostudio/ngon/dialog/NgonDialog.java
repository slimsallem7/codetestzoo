package com.zoostudio.ngon.dialog;

import com.zoostudio.ngon.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NgonDialog extends Dialog {
	protected static View mInnerCustomView;
	public NgonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}

	public NgonDialog(Context context, int theme) {
		super(context, theme);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}

	public NgonDialog(Context context) {
		super(context);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}

	public static class Builder {
		protected String mMessage;
		protected String mTitle;
		protected DialogInterface.OnClickListener mPositiveButtonClickListener;
		protected DialogInterface.OnClickListener mNegativeButtonClickListener;
		protected DialogInterface.OnClickListener mNeutralButtonClickListener;
		protected String mPositiveText;
		protected String mNegativeText;
		protected String mNeutralText;
		protected boolean mCancelable;
		protected View mCustomView;
		protected Context mContext;
		protected DialogInterface.OnCancelListener mOnCancelListener;

		public Builder(Context context) {
			mContext = context;
			mCancelable = true;
		}

		public Builder setCustomView(View v) {
			mCustomView = v;
			return this;
		}
		
		public Builder setInnerCustomView(View v) {
			mInnerCustomView = v;
			return this;
		}

		public Builder setTitle(int title) {
			return setTitle(mContext.getString(title).toUpperCase());
		}

		public Builder setTitle(String title) {
			mTitle = title.toUpperCase();
			return this;
		}

		public Builder setMessage(int msg) {
			return setMessage(mContext.getString(msg));
		}

		public Builder setMessage(String message) {
			mMessage = message;
			return this;
		}

		public Builder setPositiveButton(int text, DialogInterface.OnClickListener listener) {
			return setPositiveButton(mContext.getString(text).toUpperCase(), listener);
		}

		public Builder setPositiveButton(String text, DialogInterface.OnClickListener listener) {
			mPositiveText = text.toUpperCase();
			mPositiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int text, DialogInterface.OnClickListener listener) {
			return setNegativeButton(mContext.getString(text).toUpperCase(), listener);
		}

		public Builder setNegativeButton(String text, DialogInterface.OnClickListener listener) {
			mNegativeText = text.toUpperCase();
			mNegativeButtonClickListener = listener;
			return this;
		}

		public Builder setNeutralButton(int text, DialogInterface.OnClickListener listener) {
			return setNeutralButton(mContext.getString(text).toUpperCase(), listener);
		}

		public Builder setNeutralButton(String text, DialogInterface.OnClickListener listener) {
			mNeutralText = text.toUpperCase();
			mNeutralButtonClickListener = listener;
			return this;
		}

		public Builder setCancelable(boolean cancelable) {
			mCancelable = cancelable;
			return this;
		}

		public Builder setOnCancelListener(DialogInterface.OnCancelListener listener) {
			mOnCancelListener = listener;
			return this;
		}

		public void show() {
			create().show();
		}

		public NgonDialog create() {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final NgonDialog dialog = new NgonDialog(mContext, R.style.NgonDialog);
			View layout = inflater.inflate(R.layout.dialog_base, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			dialog.setCancelable(mCancelable);

			if (mOnCancelListener != null) dialog.setOnCancelListener(mOnCancelListener);

			LinearLayout mContent = (LinearLayout) layout.findViewById(R.id.content);

			if (mCustomView != null) {
				mContent.addView(mCustomView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			} else {
				LayoutInflater inflater2 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View mInnerLayout = inflater2.inflate(R.layout.dialog_alert, null);
				mContent.addView(mInnerLayout);

				TextView title = (TextView) mInnerLayout.findViewById(R.id.title);
				if (mTitle != null) {
					title.setText(mTitle);
				} else {
					title.setVisibility(View.GONE);
					mInnerLayout.findViewById(R.id.divider).setVisibility(View.GONE);
				}

				LinearLayout mInnerCustomViewLayout = (LinearLayout) mInnerLayout.findViewById(R.id.innercustomview);
				if (mInnerCustomView == null) {
					if (mMessage != null) {
						TextView message = new TextView(mContext);
						int padding = mContent.getResources().getDimensionPixelOffset(R.dimen.padding_medium);
						message.setPadding(padding, padding, padding, padding);
						message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
						message.setTextColor(mContent.getResources().getColor(R.color.NgonDoDarkGrey));
						message.setText(mMessage);
						mInnerCustomViewLayout.addView(message);
					}
				} else {
					mInnerCustomViewLayout.addView(mInnerCustomView);
				}

				Button positive = (Button) mInnerLayout.findViewById(R.id.positive);
				if (mPositiveButtonClickListener != null) {
					positive.setText(mPositiveText);
					positive.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mPositiveButtonClickListener.onClick(dialog, BUTTON_POSITIVE);
						}
					});
				} else {
					positive.setVisibility(View.GONE);
				}

				Button negative = (Button) mInnerLayout.findViewById(R.id.negative);
				if (mNegativeButtonClickListener != null) {
					negative.setText(mNegativeText);
					negative.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mNegativeButtonClickListener.onClick(dialog, BUTTON_NEGATIVE);
						}
					});
				} else {
					negative.setVisibility(View.GONE);
				}

				Button neutral = (Button) mInnerLayout.findViewById(R.id.neutral);
				if (mNeutralButtonClickListener != null) {
					neutral.setText(mNeutralText);
					neutral.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mNeutralButtonClickListener.onClick(dialog, BUTTON_NEUTRAL);
						}
					});
				} else {
					neutral.setVisibility(View.GONE);
				}
			}

			dialog.setContentView(layout);
			return dialog;
		}
	}
}
