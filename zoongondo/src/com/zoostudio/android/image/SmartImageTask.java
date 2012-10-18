package com.zoostudio.android.image;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.test.cache.CacheableBitmapWrapper;

public class SmartImageTask implements Runnable {
    private static final int BITMAP_READY = 0;

    private boolean cancelled = false;
    private OnCompleteHandler onCompleteHandler;
    private SmartImage image;
    private Context context;

    public static class OnCompleteHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	CacheableBitmapWrapper bitmap = (CacheableBitmapWrapper)msg.obj;
            onComplete(bitmap);
        }

        public void onComplete(CacheableBitmapWrapper bitmap){};
    }

    public SmartImageTask(Context context, SmartImage image) {
        this.image = image;
        this.context = context;
    }

    @Override
    public void run() {
        if(image != null) {
            complete(image.getWrap(context));
            context = null;
        }
    }

    public void setOnCompleteHandler(OnCompleteHandler handler){
        this.onCompleteHandler = handler;
    }

    public void cancel() {
        cancelled = true;
    }

    public void complete(CacheableBitmapWrapper bitmap){
        if(onCompleteHandler != null && !cancelled) {
            onCompleteHandler.sendMessage(onCompleteHandler.obtainMessage(BITMAP_READY, bitmap));
        }
    }
}