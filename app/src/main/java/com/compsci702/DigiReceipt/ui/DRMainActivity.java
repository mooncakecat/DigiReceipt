package com.compsci702.DigiReceipt.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.base.DRBaseFragment;
import com.compsci702.DigiReceipt.ui.receipts.DRAddReceiptFragment;
import com.compsci702.DigiReceipt.ui.main.DRMainFragment;
import com.compsci702.DigiReceipt.ui.receipts.DRViewReceiptsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DRMainActivity extends AppCompatActivity implements DRMainFragment.FragmentListener,
        DRAddReceiptFragment.FragmentListener, DRViewReceiptsFragment.FragmentListener {

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * internal fields
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    @BindView(R.id.fragment_container) FrameLayout mFragmentContainer;
    @BindView(R.id.toolbar) Toolbar mToolbar;

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * lifecycle methods
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, DRMainFragment.newInstance(), "DRMainFragment")
                .commit();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_icon:
                DRSearchActivity.startActivity(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * ui callbacks
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    @Override public void onAddReceiptSelected() {
        replaceFragment(DRAddReceiptFragment.newInstance());
    }

    @Override public void onViewReceiptsSelected() {
        replaceFragment(DRViewReceiptsFragment.newInstance());
    }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * general methods
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private void replaceFragment(DRBaseFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
