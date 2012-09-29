package com.zoostudio.ngon.dialog;

import com.zoostudio.ngon.R;

import android.content.Context;

public class WaitingDialog extends NgonProgressDialog {

    public WaitingDialog(Context context) {
        super(context);

        setMessage(R.string.string_waiting);
        setCancelable(false);
    }

}
