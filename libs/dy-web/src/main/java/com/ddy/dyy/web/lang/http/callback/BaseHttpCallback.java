package com.ddy.dyy.web.lang.http.callback;

import com.ddy.dyy.web.lang.http.AyoRequest;

public abstract class BaseHttpCallback<T> {

	public BaseHttpCallback(){
	}
	
	public abstract void onFinish(AyoRequest request, boolean isSuccess, FailInfo failInfo, T data);

}
