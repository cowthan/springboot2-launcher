package com.ddy.dyy.web.lang.http;
import java.io.File;

import com.ddy.dyy.web.lang.http.callback.BaseHttpCallback;
import com.ddy.dyy.web.lang.http.callback.ProgressRequestListener;
import com.ddy.dyy.web.lang.http.callback.ProgressResponseListener;


/**
 * Created by Administrator on 2016/8/16.
 */
public abstract class HttpWorker {
    public abstract void fire(AyoRequest req, BaseHttpCallback<String> callback, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener);
    public abstract AyoResponse fireSync(AyoRequest req, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener);
    public abstract void cancelAll(Object tag);
    public abstract AyoResponse download(String url, File outFile, ProgressResponseListener progressResponseListener);
}
