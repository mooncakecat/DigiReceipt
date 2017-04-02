package com.compsci702.DigiReceipt.ui.receipts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;

/**
 * Add a receipt to be digitalised
 */
public class DRAddReceiptFragment extends DRBaseFragment<DRAddReceiptFragment.FragmentListener> {

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * FragmentListener
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  public interface FragmentListener {
  }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * new instance
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  public static DRAddReceiptFragment newInstance() {
    DRAddReceiptFragment addReceiptFragment = new DRAddReceiptFragment();
    return addReceiptFragment;
  }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * life cycle
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  @Override protected View getContentView(LayoutInflater inflater, ViewGroup container) {
    return inflater.inflate(R.layout.fragment_add_receipts, container, false);
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
    return getActivity().getResources().getString(R.string.add_a_receipt);
  }
}
