package com.compsci702.DigiReceipt.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Base Fragment class for Fragments. This base class provides the following functionality:
 * <p>
 * -  Automatically handles binding up of Fragment listener type
 * <p>
 * - Automatically binds and unbinds views using ButterKnife
 *
 * @param <L> The Fragment listener interface type
 */
public abstract class DRBaseFragment<L> extends Fragment {

  protected L mListener;

  private Unbinder mBinder;

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * abstract methods
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  @NonNull protected abstract Class getFragmentListenerClass();

  protected abstract View getContentView(LayoutInflater inflater, ViewGroup container);

  protected abstract void onViewCreated(Bundle savedInstanceState);

  protected abstract void updateView();

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * lifecycle
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  @Override public void onAttach(Context context) {
    super.onAttach(context);

    // attempt to get listener instance from parent Fragment
    Fragment parentFragment = getParentFragment();
    Activity parentActivity = getActivity();

    if (getFragmentListenerClass().isInstance(parentFragment)) {
      //noinspection unchecked
      mListener = (L) parentFragment;

    } else if (getFragmentListenerClass().isInstance(parentActivity)) {
      //noinspection unchecked
      mListener = (L) parentActivity;

    } else {
      throw new IllegalStateException("Fragment attached to parent (" + parentActivity.getClass()
        .getName() + ") without FragmentListener: " + getFragmentListenerClass());
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = getContentView(inflater, container);
    mBinder = ButterKnife.bind(this, view);

    onViewCreated(savedInstanceState);
    updateView();

    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mBinder.unbind();
  }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * general
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  public String getScreenTitle() {
    return null;
  }
}
