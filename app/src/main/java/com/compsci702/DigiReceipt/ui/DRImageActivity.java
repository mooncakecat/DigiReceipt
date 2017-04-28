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

/**
 * Display an image in a full view
 */
public class DRImageActivity extends AppCompatActivity implements DRImageFragment.FragmentListener {

	private static final String KEY_STARTING_RECEIPT_FILENAME = "key_starting_receipt_filename";

	private String mStartingReceiptFilename;

	public static void startActivity(@NonNull Activity activity, String receiptFilename) {
		Intent intent = new Intent(activity, DRImageActivity.class);
		intent.putExtra(KEY_STARTING_RECEIPT_FILENAME, receiptFilename);
		activity.startActivity(intent);
	}

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		Intent intent = getIntent();
		String condition = "ifstatements";
		boolean result = true;
		while(result) {
			switch (condition) {
				case "ifstatements":
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
						condition = "firstiftrue";
					else
						condition = "firstiffalse";
					break;
				case "secondiftrue":
					mStartingReceiptFilename = intent.getStringExtra(KEY_STARTING_RECEIPT_FILENAME);
					condition = "gotolastif";
					break;
				case "firstiffalse":
					condition = "gotosecondif";
					break;
				case "gotosecondif":
					if(intent.getExtras() != null)
						condition = "secondiftrue";
					else
						condition = "secondiffalse";
					break;
				case "firstiftrue":
					getWindow().getDecorView().setSystemUiVisibility(
							View.SYSTEM_UI_FLAG_IMMERSIVE
									| View.SYSTEM_UI_FLAG_FULLSCREEN
									| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
									| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
					condition = "gotosecondif";
					break;
				case "secondiffalse":
					condition = "gotolastif";
					break;
				case "gotolastif":
					if(savedInstanceState == null)
						condition = "truetrue";
					else
						condition = "falsefalse";
					break;
				case "truetrue":
					getSupportFragmentManager()
							.beginTransaction()
							.add(R.id.fragment_container, DRImageFragment.newInstance(mStartingReceiptFilename))
							.commit();
					condition = "falsefalse";
					break;
				case "falsefalse":
					result = false;
					break;
				default:
					break;
			}
		}
	}
}
