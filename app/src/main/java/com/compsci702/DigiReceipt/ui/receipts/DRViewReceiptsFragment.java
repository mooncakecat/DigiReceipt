package com.compsci702.DigiReceipt.ui.receipts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.core.DRApplication;
import com.compsci702.DigiReceipt.core.DRApplicationHub;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Display a list of digitalised receipts
 */
public class DRViewReceiptsFragment extends DRBaseFragment<DRViewReceiptsFragment.FragmentListener> implements
		DRReceiptsRecyclerViewAdapter.AdapterListener {

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * FragmentListener
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public interface FragmentListener {
		void onReceiptSelected(String receiptFilename);
	}

	DRApplicationHub mApplicationHub = DRApplication.getApplicationHub();
	private static final int GRID_COLUMNS = 2;

	private DRReceiptsRecyclerViewAdapter mAdapter;
	private final List<DRReceipt> mReceipts = new ArrayList<>();

	@BindView(R.id.recyclerview) RecyclerView mRecyclerView;

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * new instance
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static DRViewReceiptsFragment newInstance() {
		return new DRViewReceiptsFragment();
	}

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * life cycle
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Override protected View getContentView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_view_receipts, container, false);
	}

	@Override protected void onViewCreated(Bundle savedInstanceState) {

		mAdapter = new DRReceiptsRecyclerViewAdapter(getActivity(), mReceipts, this);
		mAdapter.notifyDataSetChanged();

		final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), GRID_COLUMNS);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setHasFixedSize(true);
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
				Collections.reverse(receipts);
				mReceipts.addAll(receipts);
				mAdapter.notifyDataSetChanged();
			}

			@Override public void onCompleted() {

			}

			@Override public void onError(Throwable e) {

			}
		});
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	  * DRReceiptsRecyclerViewAdapter.AdapterListener
      * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Override public void onReceiptSelected(String receiptFilename) {
		mListener.onReceiptSelected(receiptFilename);
	}
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * base class overrides
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@NonNull @Override protected Class getFragmentListenerClass() {
		return FragmentListener.class;
	}

	@Override protected void updateView() {
	}

	@NonNull @Override public String getScreenTitle() {
		return getString(R.string.view_receipts);
	}
}
