package com.compsci702.DigiReceipt.core;

import android.support.annotation.NonNull;
import android.util.Log;

import com.compsci702.DigiReceipt.database.DRReceiptDb;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Application hub.
 */
public class DRApplicationHub {

	private static final String TAG = "DRApplicationHub";

	private final DRDatabaseHub mDatabaseHub = DRApplication.getDatabaseHub();
	private final DRNetworkHub mNetworkHub = DRApplication.getNetworkHub();

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * receipts
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * Get an observable that emits the receipt details.
	 *
	 * @return Return an observable that emits the receipt details.
	 * The observable will emit a static list of data, then complete.
	 */
	// FIXME: 4/15/2017 Currently only used for testing, not required in final usage
	@NonNull public List<DRReceiptDb> getReceipt() throws SQLException {
		try {
			return mDatabaseHub.getReceipts();
		} catch (SQLException e) {
			Log.e(TAG, "error in getReceipt", e);
		}
		return null;
	}

	/**
	 * Starts an observable, in the background, that adds the receipt to DRReceiptDb.
	 *
	 * @param receipt The receipt to add to DRReceiptDb.
	 */
	public void addReceipt(@NonNull DRReceipt receipt) {
		mDatabaseHub.addReceipt(receipt)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<DRReceiptDb>() {
					@Override public void onNext(DRReceiptDb receipt) {
						Log.i(TAG, "Receipt added successfully: " + receipt);
					}

					@Override public void onCompleted() {
						// do nothing
					}

					@Override public void onError(Throwable e) {
						Log.e(TAG, "Error in addReceipt: ", e);
					}
				});
	}

	public Observable<List<? extends DRReceipt>> searchReceipt(@NonNull String query) {
		return mDatabaseHub.searchReceipts(query).toObservable();
	}
}