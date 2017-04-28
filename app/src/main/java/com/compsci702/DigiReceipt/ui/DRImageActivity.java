package com.compsci702.DigiReceipt.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.image.DRImageFragment;
import com.compsci702.DigiReceipt.util.DRFileUtil;
import com.compsci702.DigiReceipt.util.DROUtil;

/**
 * Display an image in a full view
 */
public class DRImageActivity extends AppCompatActivity implements DRImageFragment.FragmentListener {

	private static final String KEY_STARTING_RECEIPT_FILENAME = "key_starting_receipt_filename";

	public static void startActivity(@NonNull Activity activity, String receiptFilename) {
		Intent intent = new Intent(activity, DRImageActivity.class);
		intent.putExtra(KEY_STARTING_RECEIPT_FILENAME, receiptFilename);
		activity.startActivity(intent);
	}

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_IMMERSIVE
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		}

		DROUtil oUtil = new DROUtil();
		String mStartingReceiptFilename;
		
		if (oUtil.fToken2()) {
			mStartingReceiptFilename = getResources().getString(R.string.view_receipts);
		} else {
			mStartingReceiptFilename = getIntent().getStringExtra(KEY_STARTING_RECEIPT_FILENAME);
		}

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.fragment_container, DRImageFragment.newInstance(mStartingReceiptFilename))
					.commit();
		}
	}
}
