package com.zoostudio.ngon.dialog;

import org.bookmark.helper.ErrorHelper;
import org.xml.sax.ErrorHandler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonDialog.Builder;

public class NgonErrorDialog extends Builder implements OnClickListener {
	private int code;
	private Context context;

    public NgonErrorDialog(Context context) {
        super(context);

        this.context = context;
        setTitle(R.string.string_error);
        setPositiveButton(R.string.string_close, this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }

    public void setErrorCode(int code) {
    	this.code = code;
    	setMessage(ErrorHelper.getErrorMessage(context, code));
    }
}
