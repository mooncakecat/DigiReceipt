package com.compsci702.DigiReceipt.ui.image;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Display an image
 */
public class DRImageFragment extends DRBaseFragment<DRImageFragment.FragmentListener> implements DRImagePagerAdapter
		.AdapterListener {

 	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * FragmentListener
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public interface FragmentListener {
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * internal fields
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private static final String KEY_STARTING_RECEIPT_FILENAME = "key_starting_receipt_filename";

	DRImagePagerAdapter mAdapter;
	final List<DRReceipt> mReceipts = new ArrayList<>();

	private String mStartingReceiptFilename;

	@BindView(R.id.view_pager) ViewPager mViewPager;

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * new instance
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@NonNull public static DRImageFragment newInstance(String mStartingReceiptFilename) {
		DRImageFragment fragment = new DRImageFragment();
		Bundle arguments = new Bundle();
		arguments.putString(KEY_STARTING_RECEIPT_FILENAME, mStartingReceiptFilename);
		fragment.setArguments(arguments);
		return fragment;
	}

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * lifecycle
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Override protected View getContentView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_images, container, false);
	}

	@Override protected void onViewCreated(Bundle savedInstanceState) {
		mAdapter = new DRImagePagerAdapter(getActivity(), mReceipts);
		mViewPager.setAdapter(mAdapter);

		if (getArguments() != null) {
			mStartingReceiptFilename = getArguments().getString(KEY_STARTING_RECEIPT_FILENAME);
		}

		// FIXME: 4/2/2017 Should be done on a new thread
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/DigiReceipt";
		File directory = new File(path);
		final File[] files = directory.listFiles();

		// reverse order to show latest receipts at the top
		for (int i = files.length - 1; i > -1; i--) {
			final String filePath = files[i].getAbsolutePath();
			DRReceipt receipt = new DRReceipt() {
				@Override public int getId() {
					return 0;
				}

				@Override public String getFilename() {
					return filePath;
				}

                @Override public String getTags() {
                    return null;
                }
            };
			mReceipts.add(receipt);
		}

		mAdapter.notifyDataSetChanged();

		int startingIndex = 0;
		for (int index = 0; index < mReceipts.size(); index++) {
			if (mReceipts.get(index).getFilename().equals(mStartingReceiptFilename)) {
				startingIndex = index;
			}
		}
		mViewPager.setCurrentItem(startingIndex);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * base class overrides
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@NonNull @Override protected Class getFragmentListenerClass() {
		return FragmentListener.class;
	}

	@Override protected void updateView() {

	}

}
