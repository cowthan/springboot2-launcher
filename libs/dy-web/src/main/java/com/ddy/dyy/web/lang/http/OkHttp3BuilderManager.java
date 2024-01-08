package com.ddy.dyy.web.lang.http;



import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.ddy.dyy.web.lang.http.utils.HttpsSignUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qiaoliang on 2017/11/18.
 */

public class OkHttp3BuilderManager {

    static OkHttpClient.Builder okHttpClientBuilder;

    public static boolean isDnsEnabled = false;

    public static OkHttpClient.Builder getOkHttpClientBuilder(){
        if(okHttpClientBuilder == null){
            HttpsSignUtils.SSLParams sslParams = HttpsSignUtils.getSslSocketFactory(null, null, null);
            okHttpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .readTimeout(10000, TimeUnit.MILLISECONDS)
                    .writeTimeout(10000, TimeUnit.MILLISECONDS)
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);  //设置可访问所有的https网站


            //公共header设置
            Interceptor mTokenInterceptor = new Interceptor() {
                @Override public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder builder = originalRequest.newBuilder();
                    Request authorised = builder.build();
                    return chain.proceed(authorised);
                }
            };
            okHttpClientBuilder.addNetworkInterceptor(mTokenInterceptor);

        }
        return okHttpClientBuilder;
    }

}
