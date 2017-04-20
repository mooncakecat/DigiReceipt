package com.compsci702.DigiReceipt.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.core.DRApplication;
import com.compsci702.DigiReceipt.core.DRApplicationHub;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.compsci702.DigiReceipt.ui.receipts.DRReceiptsRecyclerViewAdapter;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class DRSearchFragment extends DRBaseFragment<DRSearchFragment.FragmentListener> implements
		DRReceiptsRecyclerViewAdapter.AdapterListener {

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * FragmentListener
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public interface FragmentListener {

	}

	private static final int QUERY_UPDATE_DELAY_SECONDS = 1;
	private static final int MINIMUM_QUERY_LENGTH = 1;

	private static final int GRID_COLUMNS = 4;

	private final DRApplicationHub mApplicationHub = DRApplication.getApplicationHub();
	private Subscription mSearchFilterSubscription;
	private List<DRReceipt> mReceipts = new ArrayList<>();

	private DRReceiptsRecyclerViewAdapter mAdapter;
	private String mQuery;

	@BindView(R.id.recyclerview) RecyclerView mRecyclerView;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     * new instance
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@NonNull public static Fragment newInstance() {
		return new DRSearchFragment();
	}

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * life cycle
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Override protected View getContentView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_search, container, false);
	}

	@Override protected void onViewCreated(Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		mAdapter = new DRReceiptsRecyclerViewAdapter(getActivity(), mReceipts, this);

		final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), GRID_COLUMNS);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setHasFixedSize(true);
	}

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     *  base class overrides
     *  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@NonNull @Override protected Class getFragmentListenerClass() {
		return FragmentListener.class;
	}

	@Override protected void updateView() {

	}


    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     * menu
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		final SearchView searchView = (SearchView) getActivity().findViewById(R.id.searchview);
		searchView.setIconified(false);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String query) {
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				search(query);
				return true;
			}
		});

		Observable<CharSequence> searchFilterObservable = RxSearchView.queryTextChanges(searchView)
				.throttleLast(QUERY_UPDATE_DELAY_SECONDS, TimeUnit.SECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.filter(new Func1<CharSequence, Boolean>() {
					@Override public Boolean call(CharSequence charSequence) {
						return charSequence.length() >= MINIMUM_QUERY_LENGTH;
					}
				});

		mSearchFilterSubscription = searchFilterObservable.subscribe(new Action1<CharSequence>() {
			@Override public void call(CharSequence charSequence) {
				final String query = charSequence.toString();
				search(query);
			}
		});

		// go back to previous screen if searchview is closed and the query is empty
		searchView.setOnCloseListener(new SearchView.OnCloseListener() {
			@Override public boolean onClose() {
				if (TextUtils.isEmpty(searchView.getQuery())) {
					getActivity().finish();
				}
				return false;
			}
		});
	}

	private void search(@NonNull final String query) {

		Observable<List<? extends DRReceipt>> searchObservable = mApplicationHub.searchReceipt(query)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());

		searchObservable.subscribe(new Observer<List<? extends DRReceipt>>() {
			@Override public void onCompleted() {

			}

			@Override public void onError(Throwable e) {

			}

			@Override public void onNext(List<? extends DRReceipt> receipts) {
				mReceipts.clear();
				mReceipts.addAll(receipts);
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override public void onReceiptSelected(String receiptFilename) {
		// FIXME: 4/14/2017 open the receipt up!
	}

}
