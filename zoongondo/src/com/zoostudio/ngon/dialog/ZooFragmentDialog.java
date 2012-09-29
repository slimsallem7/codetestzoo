package com.zoostudio.ngon.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoostudio.ngon.R;

public class ZooFragmentDialog extends DialogFragment {
	int mNum;
	private View mCustomView;
	private CharSequence mTitle;
	private CharSequence mMessage;
	protected View.OnClickListener mPositiveButtonClickListener;
    protected View.OnClickListener mNegativeButtonClickListener;
    protected View.OnClickListener mNeutralButtonClickListener;
    protected String mPositiveText;
    protected String mNegativeText;
    protected String mNeutralText;
    protected boolean mCancelable;
    protected Context mContext;
    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static ZooFragmentDialog newInstance(int num) {
    	ZooFragmentDialog f = new ZooFragmentDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_base, container, false);
        LinearLayout mContent = (LinearLayout) layout.findViewById(R.id.content);

        if (mCustomView != null) {
            mContent.addView(mCustomView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        } else {
            View mInnerLayout = inflater.inflate(R.layout.dialog_alert, null);
            mContent.addView(mInnerLayout);

            TextView title = (TextView) mInnerLayout.findViewById(R.id.title);
            if (mTitle != null) {
                title.setText(mTitle);
            } else {
                title.setVisibility(View.GONE);
                mInnerLayout.findViewById(R.id.divider).setVisibility(View.GONE);
            }

            TextView message = (TextView) mInnerLayout.findViewById(R.id.message);
            if (mMessage != null) {
                message.setText(mMessage);
            } else {
                message.setVisibility(View.GONE);
            }
        }
        return null;
    }
}
