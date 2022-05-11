package com.beiming.uhf_test.utils.pic;

import android.annotation.TargetApi;
import android.view.View;

@TargetApi(16)
public class SDK16 {

	public static void postOnAnimation(View view, Runnable r) {
		view.postOnAnimation(r);
	}
	
}
