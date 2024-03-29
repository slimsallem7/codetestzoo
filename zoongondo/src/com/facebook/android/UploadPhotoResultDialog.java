package com.facebook.android;

import org.json.JSONException;
import org.json.JSONObject;

import com.zoostudio.ngon.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UploadPhotoResultDialog extends Dialog {

    private String response, photo_id;
    private TextView mOutput, mUsefulTip;
    private Button mViewPhotoButton, mTagPhotoButton;
    private ImageView mUploadedPhoto;
    private Activity activity;
    private ProgressDialog dialog;
    private boolean hidePhoto = false;
    private Handler mHandler;

    public UploadPhotoResultDialog(Activity activity, String title, String response) {
        super(activity);
        this.activity = activity;
        this.response = response;
        setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();

        setContentView(R.layout.upload_photo_response);
        LayoutParams params = getWindow().getAttributes();
        params.width = LayoutParams.FILL_PARENT;
        params.height = LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        mOutput = (TextView) findViewById(R.id.apiOutput);
        mUsefulTip = (TextView) findViewById(R.id.usefulTip);
        mViewPhotoButton = (Button) findViewById(R.id.view_photo_button);
        mTagPhotoButton = (Button) findViewById(R.id.tag_photo_button);
        mUploadedPhoto = (ImageView) findViewById(R.id.uploadedPhoto);

        JSONObject json;
        try {
            json = Util.parseJson(response);
            final String photo_id = json.getString("id");
            this.photo_id = photo_id;

            mOutput.setText(json.toString(2));
            mUsefulTip.setText("Photo tip");
            Linkify.addLinks(mUsefulTip, Linkify.WEB_URLS);

            mViewPhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hidePhoto) {
                        mViewPhotoButton.setText("View Photo");
                        hidePhoto = false;
                        mUploadedPhoto.setImageBitmap(null);
                    } else {
                        hidePhoto = true;
                        mViewPhotoButton.setText("Hide photo");
                        /*
                         * Source tag: view_photo_tag
                         */
                        Bundle params = new Bundle();
                        params.putString("fields", "picture");
                        dialog = ProgressDialog.show(activity, "",
                                "Plz wait", true, true);
                        dialog.show();
                        Utility.mAsyncRunner.request(photo_id, params,
                                new ViewPhotoRequestListener());
                    }
                }
            });
            mTagPhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                     * Source tag: tag_photo_tag
                     */
                    setTag();
                }
            });
        } catch (JSONException e) {
            setText("Exception " + e.getMessage());
        } catch (FacebookError e) {
            setText("Exception " + e.getMessage());
        }
    }

    public void setTag() {
        String relativePath = photo_id + "/tags/" + Utility.userUID;
        Bundle params = new Bundle();
        params.putString("x", "5");
        params.putString("y", "5");
        Utility.mAsyncRunner.request(relativePath, params, "POST", new TagPhotoRequestListener(),
                null);
    }

    public class ViewPhotoRequestListener extends BaseRequestListener {

        @Override
        public void onComplete(final String response, final Object state) {
            try {
                JSONObject json = Util.parseJson(response);
                final String pictureURL = json.getString("picture");
                if (TextUtils.isEmpty(pictureURL)) {
                    setText("Error getting \'picture\' field of the photo");
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            new FetchImage().execute(pictureURL);
                        }
                    });
                }
            } catch (JSONException e) {
                dialog.dismiss();
                setText("" + e.getMessage());
            } catch (FacebookError e) {
                dialog.dismiss();
                setText("" + e.getMessage());
            }
        }

        public void onFacebookError(FacebookError error) {
            dialog.dismiss();
            setText("" + error.getMessage());
        }
    }

    public class TagPhotoRequestListener extends BaseRequestListener {

        @Override
        public void onComplete(final String response, final Object state) {
            if (response.equals("true")) {
                String message = "User tagged in photo at (5, 5)" + "\n";
                message += "Api Response: " + response;
                setText(message);
            } else {
                setText("User could not be tagged.");
            }
        }

        public void onFacebookError(FacebookError error) {
            setText("" + error.getMessage());
        }
    }

    public void setText(final String txt) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mOutput.setText(txt);
            }
        });
    }

    private class FetchImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            return Utility.getBitmap(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            dialog.dismiss();
            mUploadedPhoto.setImageBitmap(result);
        }
    }
}
