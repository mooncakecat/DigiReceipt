package com.compsci702.DigiReceipt.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * A square ImageView with a dynamic width
 */
public class DRSquareImageView extends AppCompatImageView {
	public DRSquareImageView(Context context) {
		super(context);
	}

	public DRSquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public DRSquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = getMeasuredWidth();
		//noinspection SuspiciousNameCombination
		setMeasuredDimension(width, width);
	}
}
