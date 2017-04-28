package com.compsci702.DigiReceipt.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.search.DRSearchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity for searching for a receipt
 */
public class DRSearchActivity extends AppCompatActivity implements DRSearchFragment.FragmentListener{

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.searchview) SearchView mSearchView;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, DRSearchActivity.class);
        activity.startActivity(intent);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // only add the fragment if the activity is started for the first time
        boolean run = true;
        String condition = "checkcondition";
        while(run) {
            switch (condition) {
                case "conditiontrue":
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, DRSearchFragment.newInstance())
                            .commit();
                    condition = "endloop";
                    break;
                case "endloop":
                    run = false;
                    break;
                case "checkcondition":
                    if(savedInstanceState == null)
                        condition = "conditiontrue";
                    else
                        condition = "endloop";
                    break;
                default:
                    break;
            }
        }
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 	 * menu
  	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onReceiptSelected(String receiptFilename) {
        DRImageActivity.startActivity(this, receiptFilename);
    }
}
