package com.compsci702.DigiReceipt.ui.receipts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;

/**
 * Display a list of digitalised receipts
 */
public class DRViewReceiptsFragment extends DRBaseFragment<DRViewReceiptsFragment.FragmentListener> {

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * FragmentListener
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  public interface FragmentListener {
  }

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

  }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  * base class overrides
  * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  @NonNull @Override protected Class getFragmentListenerClass() {
    return FragmentListener.class;
  }

  @Override protected void updateView() {

  }

  @Override public String getScreenTitle() {
    return getResources().getString(R.string.view_receipts);
  }
}
