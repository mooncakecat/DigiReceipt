package com.compsci702.DigiReceipt.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.compsci702.DigiReceipt.BuildConfig;
import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.core.DRNetworkHub;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;
import com.compsci702.DigiReceipt.ui.main.DRMainFragment;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.compsci702.DigiReceipt.ui.model.DRReceiptTemp;
import com.compsci702.DigiReceipt.ui.receipts.DRViewReceiptsFragment;
import com.compsci702.DigiReceipt.util.DRFileUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.compsci702.DigiReceipt.util.DRFileUtil.generateMediaFile;

public class DRMainActivity extends AppCompatActivity implements DRMainFragment.FragmentListener,
        DRViewReceiptsFragment.FragmentListener {

  	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  	 * internal fields
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_CODE = 5;
    public static Uri uri = null;

	@BindView(R.id.fragment_container) FrameLayout mFragmentContainer;
	@BindView(R.id.toolbar) Toolbar mToolbar;


  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * lifecycle methods
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);

		setSupportActionBar(mToolbar);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, DRMainFragment.newInstance(), "DRMainFragment")
					.commit();
		}
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.search_icon:
				DRSearchActivity.startActivity(this);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * ui callbacks
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Override public void onAddReceiptSelected() {
		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager
				.PERMISSION_GRANTED) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				requestPermissions(new String[]{android.Manifest.permission.CAMERA, Manifest.permission
						.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
			}
		} else {
			dispatchTakePictureIntent();
		}
	}

	@Override public void onViewReceiptsSelected() {
		replaceFragment(DRViewReceiptsFragment.newInstance());
	}

	@Override public void onReceiptSelected(String receiptFilename) {
		DRImageActivity.startActivity(this, receiptFilename);
	}

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * permission requests
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Now user should be able to use camera
				dispatchTakePictureIntent();
			} else {
				// Your app will not have this permission. Turn off all functions
				// that require this permission or it will force close
				Toast.makeText(this, R.string.permissions_error, Toast.LENGTH_LONG).show();
			}
		}
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// This causes crashes on api 24 and above: Uri pictureURI = Uri.fromFile(generateMediaFile());
			uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider",
					generateMediaFile());
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Toast.makeText(this, R.string.image_saved, Toast.LENGTH_SHORT).show();
            try {
                getTextFromObservable(uri.toString());
               // Toast.makeText(this, receipt.getText(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

	}
    private void getTextFromObservable(String uriForFilePath){
        DRNetworkHub.httpObservable(uriForFilePath.toString()).
                subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<DRReceiptTemp>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DRReceiptTemp receipt) {
                //Button button = (Button) findViewById(R.id.add_receipt_button);
                //button.setText(receipt.getText());
            }
        });

    }
  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * general methods
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private void replaceFragment(DRBaseFragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, fragment)
				.addToBackStack(null)
				.commit();
	}
}
