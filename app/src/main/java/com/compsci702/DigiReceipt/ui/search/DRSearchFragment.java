package com.compsci702.DigiReceipt.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class DRSearchFragment extends DRBaseFragment<DRSearchFragment.FragmentListener> {

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     * FragmentListener
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public interface FragmentListener {

    }

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
                // TODO: 28/03/2017 Start searching...
                return true;
            }
        });
    }
}
