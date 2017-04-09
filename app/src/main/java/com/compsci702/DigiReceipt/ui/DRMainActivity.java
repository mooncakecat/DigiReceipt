package com.compsci702.DigiReceipt.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.core.DRApplication;
import com.compsci702.DigiReceipt.core.DRApplicationHub;
import com.compsci702.DigiReceipt.core.DRDatabaseHub;
import com.compsci702.DigiReceipt.database.DRDbHelper;
import com.compsci702.DigiReceipt.database.DRReceiptDb;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;
import com.compsci702.DigiReceipt.ui.main.DRMainFragment;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.compsci702.DigiReceipt.ui.receipts.DRAddReceiptFragment;
import com.compsci702.DigiReceipt.ui.receipts.DRViewReceiptsFragment;
import com.compsci702.DigiReceipt.util.DRFileUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Single;

public class DRMainActivity extends AppCompatActivity implements DRMainFragment.FragmentListener,
		DRAddReceiptFragment.FragmentListener, DRViewReceiptsFragment.FragmentListener {

  	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  	 * internal fields
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
  	DRApplicationHub mApplicationHub = DRApplication.getApplicationHub();
	private List<DRReceiptDb> mReceipts = new ArrayList<>();

	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_CODE = 5;
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
		//Test for DB
		try{getReceipts();}
		catch(SQLException e){e.printStackTrace();}
		searchReceipts();
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
			Uri pictureURI = Uri.fromFile(DRFileUtil.generateMediaFile());
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureURI);
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Toast.makeText(this, R.string.image_saved, Toast.LENGTH_SHORT).show();
			//Test for DB
			addReceipt();
		}
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

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * test for DB
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private void addReceipt(){
		Log.i("DRMainActivity","----Add receipt in MainActivity----");
		//Dummy Data
		DRReceipt receipt = new DRReceipt() {
			@NonNull
			@Override
			public int getId(){return 0;}

			@NonNull
			@Override
			public String getFilename() {
				return "C://DigiReceipt4";
			}

			@Override
			public String getTags() {
				return "Banana Orange Apple";
			}
		};
		mApplicationHub.addReceipt(receipt);
		Log.i("DRMainActivity", "----Added receipt in MainActivity---- ID: "+ receipt.getId());
		Log.i("DRMainActivity", "----Added receipt in MainActivity---- File path: "+ receipt.getFilename());
		Log.i("DRMainActivity", "----Added receipt in MainActivity---- Tags: "+ receipt.getTags());
	}

	private void getReceipts() throws SQLException {
		Log.i("DRMainActivity","----Get receipt in MainActivity----");
		try{
			mReceipts = mApplicationHub.getReceipt();
			for (DRReceiptDb receipt : mReceipts){
				Log.i("DRMainActivity", "----Get receipt in MainActivity---- ID: "+ receipt.getId());
				Log.i("DRMainActivity", "----Get receipt in MainActivity---- File path: "+ receipt.getFilename());
				Log.i("DRMainActivity", "----Get receipt in MainActivity---- Tags: "+ receipt.getTags());
			}
			Log.i("DRMainActivity", "----Get receipt in MainActivity---- Size: "+ mReceipts.size());
		}
		catch(SQLException e){e.printStackTrace();}
	}

	private void searchReceipts() {
		//getReceiptsForSearchQuery works but I'm not sure how to call it with Single (checked by removing Single)

		/*Log.i("DRMainActivity","----Search receipt in MainActivity----");
		Cursor a = mApplicationHub.searchReceipt("Apple");
		Log.i("DRMainActivity","----Search receipt in MainActivity---- Result size: " + a.getCount());*/

	}
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * end of test for DB
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
}
