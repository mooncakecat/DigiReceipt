package com.compsci702.DigiReceipt.core;

import android.support.annotation.NonNull;
import android.util.Log;

import com.compsci702.DigiReceipt.database.DRReceiptDb;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Single;
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
	@NonNull public Observable<List<? extends DRReceipt>> getReceipts() {
		return mDatabaseHub.getReceipts().toObservable();
	}

	/**
	 * Starts an observable, in the background, that adds the receipt to DRReceiptDb.
	 *
	 * @param receipt The receipt to add to DRReceiptDb.
	 */
	public Observable<? extends DRReceipt> addReceipt(@NonNull DRReceipt receipt) {
		return mDatabaseHub.addReceipt(receipt);
	}

	public Observable<List<? extends DRReceipt>> searchReceipt(@NonNull String query) {
		return mDatabaseHub.searchReceipts(query).toObservable();
	}
}