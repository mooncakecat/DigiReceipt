package com.compsci702.DigiReceipt.ui.receipts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Display a list of digitalised receipts
 */
public class DRViewReceiptsFragment extends DRBaseFragment<DRViewReceiptsFragment.FragmentListener> implements
        DRReceiptsRecyclerViewAdapter.AdapterListener {

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * FragmentListener
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public interface FragmentListener {
    }

    private final int GRID_COLUMNS = 4;

    private DRReceiptsRecyclerViewAdapter mAdapter;
    private final List<DRReceipt> mReceipts = new ArrayList<>();

    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * new instance
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public static DRViewReceiptsFragment newInstance() {
        DRViewReceiptsFragment viewReceiptFragment = new DRViewReceiptsFragment();
        return viewReceiptFragment;
    }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * life cycle
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    @Override protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_view_receipts, container, false);
    }

    @Override protected void onViewCreated(Bundle savedInstanceState) {
        mAdapter = new DRReceiptsRecyclerViewAdapter(getActivity(), mReceipts, this);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), GRID_COLUMNS);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
      * DRREceiptsRecyclerViewAdapater.AdapterListener
      * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    @Override public void onReceiptSelected() {
        // FIXME: 30/03/2017
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
