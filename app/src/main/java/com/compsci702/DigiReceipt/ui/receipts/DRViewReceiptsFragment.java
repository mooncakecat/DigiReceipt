package com.compsci702.DigiReceipt.ui.receipts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.core.DRApplication;
import com.compsci702.DigiReceipt.core.DRApplicationHub;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Display a list of receipts
 */
public class DRViewReceiptsFragment extends DRBaseFragment<DRViewReceiptsFragment.FragmentListener> implements
		DRReceiptsRecyclerViewAdapter.AdapterListener {

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * FragmentListener
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public interface FragmentListener {
		void onReceiptSelected(String receiptFilename);
		void onAddReceiptTextViewSelected();
	}

	private static final int GRID_COLUMNS = 2;

	DRApplicationHub mApplicationHub = DRApplication.getApplicationHub();
	private Subscription mReceiptsSubscription;

	private State mFragmentState = State.INITIAL;

	private DRReceiptsRecyclerViewAdapter mAdapter;
	private final List<DRReceipt> mReceipts = new ArrayList<>();

	@BindView(R.id.recyclerview) RecyclerView mRecyclerView;
	@BindView(R.id.loading_progress_indicator) ProgressBar mLoadingProgressIndicator;
	@BindView(R.id.empty_recycler_view) LinearLayout mEmptyRecyclerViewLayout;

	private enum State{
		INITIAL, LOADING, LOADED, ERROR;
	}

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

	@Override public void onStop() {
		super.onStop();
		cancelRequestReceipts();
	}

	private void setState(State state) {
		mFragmentState = state;
		updateView();
	}

	@OnClick(R.id.add_receipt_text_view) void onAddReceiptTextViewSelected(){
		mListener.onAddReceiptTextViewSelected();
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   	 * general methods
   	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Override protected void updateView() {
		mRecyclerView.setVisibility(getContentVisible() ? View.VISIBLE : View.GONE);
		mLoadingProgressIndicator.setVisibility(getLoadingIndicatorVisible() ? View.VISIBLE : View.GONE);
		mEmptyRecyclerViewLayout.setVisibility(getNoReceiptsVisible() ? View.VISIBLE : View.GONE);
	}

	private boolean getContentVisible() {
		return mFragmentState == State.LOADED && !mReceipts.isEmpty();
	}

	private boolean getLoadingIndicatorVisible() {
		return mFragmentState == State.LOADING;
	}

	private boolean getNoReceiptsVisible() {
		return (mFragmentState == State.LOADED || mFragmentState == State.INITIAL) && mReceipts.isEmpty();
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  	 * request receipts
  	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private void cancelRequestReceipts() {
		if (mReceiptsSubscription != null) mReceiptsSubscription.unsubscribe();
	}
	
	private void requestReceipts() {
		
		cancelRequestReceipts();
		
		final Observable<List<? extends DRReceipt>> observable = mApplicationHub.getReceipts()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());

		mReceiptsSubscription = observable.subscribe(new Observer<List<? extends DRReceipt>>() {
			@Override public void onNext(List<? extends DRReceipt> receipts) {
				mReceipts.clear();
				Collections.reverse(receipts);
				mReceipts.addAll(receipts);
				mAdapter.notifyDataSetChanged();
			}

			@Override public void onCompleted() {
				setState(State.LOADED);
			}

			@Override public void onError(Throwable e) {
				setState(State.ERROR);
			}
		});
		setState(State.LOADING);
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

	@NonNull @Override public String getScreenTitle() {
		return getString(R.string.view_receipts);
	}
}
