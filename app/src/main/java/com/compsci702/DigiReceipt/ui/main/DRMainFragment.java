package com.compsci702.DigiReceipt.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;

import butterknife.OnClick;

/**
 * Display two options for adding and viewing a receipt
 */
public class DRMainFragment extends DRBaseFragment<DRMainFragment.FragmentListener> {

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * FragmentListener
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  public interface FragmentListener {

    void onAddReceiptSelected();

    void onViewReceiptsSelected();
  }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * new instance
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  public static DRMainFragment newInstance() {
    DRMainFragment mainFragment = new DRMainFragment();
    return mainFragment;
  }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * life cycle
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  @Override protected View getContentView(LayoutInflater inflater, ViewGroup container) {
    return inflater.inflate(R.layout.fragment_main, container, false);
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

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * ui callbacks
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  @OnClick(R.id.add_receipt_button) void onAddReceiptSelected() {
    mListener.onAddReceiptSelected();
  }

  @OnClick(R.id.view_receipts_button) void onViewReceiptSelected() {
    mListener.onViewReceiptsSelected();
  }

}
