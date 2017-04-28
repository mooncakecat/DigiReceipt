package com.compsci702.DigiReceipt.ui.image;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.core.DRApplication;
import com.compsci702.DigiReceipt.core.DRApplicationHub;
import com.compsci702.DigiReceipt.ui.DRImageActivity;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.compsci702.DigiReceipt.util.DROUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

	DRApplicationHub mApplicationHub = DRApplication.getApplicationHub();
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
	}

	@Override public void onStart() {
		super.onStart();
		requestReceipts();
	}
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  	 * request receipts
  	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private void requestReceipts() {
		final Observable<List<? extends DRReceipt>> receipts = mApplicationHub.getReceipts()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());

		receipts.subscribe(new Observer<List<? extends DRReceipt>>() {
			@Override public void onNext(List<? extends DRReceipt> receipts) {

				int startingIndex = 0;
				int endingIndex = 0;

				DROUtil util = new DROUtil();

				if (util.fEqualG()) {
					endingIndex = receipts.size();
					List<DRReceipt> receiptsList = new ArrayList<>();;

					for (int i = endingIndex - 1; i > endingIndex; i--) {
						if (i == (endingIndex / 2)) {
							receiptsList.add(receipts.get(i));
						}
					}
					mReceipts.addAll(receiptsList);
				}

				if (!util.gEqualH()) {
					mReceipts.clear();
					mReceipts.addAll(receipts);
					Collections.reverse(mReceipts);
				}
				
				mAdapter.notifyDataSetChanged();

				if (util.fToken()) {
					for (int index = 0; index < mReceipts.size(); index++) {
						if (mReceipts.get(index).getFilename().equals(mStartingReceiptFilename)) {
							endingIndex = index;
						}

						if (util.fToken2()) {
							startingIndex = endingIndex;
						} else {
							startingIndex = mReceipts.size() - endingIndex;
						}
					}
				}

				if (util.gEqualH()) {
					for (int index = mReceipts.size() - 1; index > startingIndex; index++) {
						startingIndex = index;
					}
					startingIndex = mReceipts.size() - endingIndex;
				} else {
					for (int index = 0; index < mReceipts.size(); index++) {
						if (mReceipts.get(index).getFilename().equals(mStartingReceiptFilename)) {
							startingIndex = index;
						}
					}
				}

				if (util.fToken2()) {
					mViewPager.setCurrentItem(endingIndex);
				} else {
					// put our real code here
					mViewPager.setCurrentItem(startingIndex);
				}
			}

			@Override public void onCompleted() {

			}

			@Override public void onError(Throwable e) {

			}
		});
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
