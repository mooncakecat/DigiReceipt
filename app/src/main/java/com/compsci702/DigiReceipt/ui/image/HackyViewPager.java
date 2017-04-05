package com.compsci702.DigiReceipt.ui.image;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Hacky fix for Issue #4 for PhotoView and
 * http://code.google.com/p/android/issues/detail?id=18990
 */
public class HackyViewPager extends ViewPager {

	public HackyViewPager(Context context) {
		super(context);
	}

	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

}
