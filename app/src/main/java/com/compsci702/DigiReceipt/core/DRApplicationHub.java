package com.compsci702.DigiReceipt.core;

import android.support.annotation.NonNull;
import android.util.Log;

import com.compsci702.DigiReceipt.database.DRReceiptDb;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;

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
	 * receipt details
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * Get an observable that emits the receipt details.
	 *
	 * @return Return an observable that emits the receipt details.
	 * The observable will emit a static list of data, then complete.
	 */
	@NonNull public Observable<? extends List<? extends DRReceipt>> getReceiptDetails() {
		return mDatabaseHub.getReceiptDetails(); //???
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
						Log.d(TAG, "Receipt added successfully: " + receipt);
					}

					@Override public void onCompleted() {
						// do nothing
					}

					@Override public void onError(Throwable e) {
						Log.e(TAG, "error in addReceipt", e);
					}
				});
	}
}

