package com.zoostudio.ngon.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonDialog.Builder;

public abstract class ConfirmWithoutReviewDialog extends Builder implements OnClickListener {

    public ConfirmWithoutReviewDialog(Context context) {
        super(context);

        initDialog();
    }

    private void initDialog() {
        setTitle(R.string.string_notice);
        setMessage(R.string.string_warning_without_review);
        setPositiveButton(R.string.string_sure, this);
        setNegativeButton(R.string.string_close, this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                doOk();
                dialog.dismiss();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                doCancel();
                dialog.dismiss();
                break;
        }
    }

    protected abstract void doCancel();

    protected abstract void doOk();
}
