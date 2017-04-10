package com.compsci702.DigiReceipt.ui.receipts;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

	private static final int GRID_COLUMNS = 2;

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

		// FIXME: 4/2/2017 Should be done on a new thread
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/DigiReceipt";
		File directory = new File(path);
		final File[] files = directory.listFiles();
		Log.i("GetFiles", path);

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

                @Override public String getText() {
                    return null;
                }
            };
			mReceipts.add(receipt);
		}

		mAdapter = new DRReceiptsRecyclerViewAdapter(getActivity(), mReceipts, this);
		mAdapter.notifyDataSetChanged();

		final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), GRID_COLUMNS);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setHasFixedSize(true);
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
