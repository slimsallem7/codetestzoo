package com.zoostudio.ngon.task;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;

import com.zoostudio.ngon.R;
import com.zoostudio.ngon.dialog.NgonErrorDialog;
import com.zoostudio.ngon.dialog.NgonProgressDialog;
import com.zoostudio.ngon.dialog.WaitingDialog;
import com.zoostudio.restclient.RestClientNotification;
import com.zoostudio.restclient.RestClientTask;

public class RequestTokenTask extends RestClientTask {

    private NgonProgressDialog waitingDialog;
    private Activity mActivity;

    public RequestTokenTask(Activity activity) {
        super(activity, false);
        mActivity = activity;
    }

    @Override
    protected void doExecute() {
        restClient.get("/auth/request_token");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        waitingDialog = new WaitingDialog(mActivity);
        waitingDialog.show();
    }

    @Override
    protected void onPostExecute(Integer statusRequest) {
        try {
            waitingDialog.dismiss();
            boolean status = result.getBoolean("status");

            if (status) {
                String token_key = result.getString("token");
                String token_secret = result.getString("token_secret");

                Editor editor = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE).edit();
                editor.putString("token_key", token_key);
                editor.putString("token_secret", token_secret);
                editor.commit();
            } else {
                NgonErrorDialog errorDialog = new NgonErrorDialog(getActivity());
                errorDialog.setMessage(R.string.error_get_token);
                errorDialog.setPositiveButton(R.string.string_try, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestTokenTask task = new RequestTokenTask(mActivity);
                        task.execute();
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

	@Override
	protected int parseJSONToObject(JSONObject jsonObject) {
		return RestClientNotification.OK;		
	}

}
