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
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;

import java.util.ArrayList;
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

		String condition = "simpleCondition";
		boolean result = true;
		while(result) {
			switch(condition) {
				case "notnull":
					mStartingReceiptFilename = getArguments().getString(KEY_STARTING_RECEIPT_FILENAME);
					condition = "stoploop";
					break;
				case "simpleCondition":
					if (getArguments() != null)
						condition = "notnull";
					else
						condition = "stoploop";
					break;
				case "stoploop":
					result = false;
					break;
			}
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
				mReceipts.clear();
				mReceipts.addAll(receipts);
				mAdapter.notifyDataSetChanged();

				int startingIndex = 0;
				for (int index = 0; index < mReceipts.size(); index++) {
					String condition = "checkcondition";
					boolean result = true;
					while(result) {
						switch (condition) {
							case "checkcondition":
								if(mReceipts.get(index).getFilename().equals(mStartingReceiptFilename))
									condition = "truecondition";
								else
									condition = "falsecondition";
								break;
							case "truecondition":
								startingIndex = index;
								condition = "falsecondition";
								break;
							case "falsecondition":
								result = false;
								break;
							default:
								break;
						}
					}
				}
				mViewPager.setCurrentItem(startingIndex);
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
