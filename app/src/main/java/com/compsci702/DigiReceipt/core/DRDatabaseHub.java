package com.compsci702.DigiReceipt.core;

import rx.Observable;
import rx.Subscriber;

import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.compsci702.DigiReceipt.database.DRDbHelper;
import com.compsci702.DigiReceipt.database.DRDbReceiptDetails;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.j256.ormlite.dao.Dao;

import java.util.List;

/**
 * Database Hub.
 */
public class DRDatabaseHub {

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * member variables
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    @NonNull private final DRDbHelper mDbHelper;


    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * constructor
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    public DRDatabaseHub() {
        mDbHelper = DRApplication.getDbHelper();
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * add receipt
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /**
     * Add the given receipt to the list of receipts.
     *
     * @param receipt The receipt to add to list.
     *
     * @return Return an observable that adds the receipts to the list of receipts
     * and emits a single value (the receipt from the database), then completes.
     * Unsubscribing from this observable will not cancel the addition.
     */
    @NonNull public Observable<DRDbReceiptDetails> addReceipt(@NonNull final DRReceipt receipt) {
        final Dao<DRDbReceiptDetails, ?> dao = mDbHelper.getTable(DRDbReceiptDetails.class);
        return Observable.create(new Observable.OnSubscribe<DRDbReceiptDetails>() {
            @Override public void call(Subscriber<? super DRDbReceiptDetails> subscriber) {
                try {

                    final DRDbReceiptDetails dbReceipt;

                   // create a database version of the location
                    dbReceipt = new DRDbReceiptDetails(receipt);

                    // create or update the location
                    final int status = dao.create(dbReceipt);
                    if (status == -1) {
                        throw new SQLiteException("error inserting receipt: " + receipt);
                    }

                    // pass to subscriber
                    if (subscriber.isUnsubscribed()) return;
                    subscriber.onNext(dbReceipt);
                    subscriber.onCompleted();

                } catch (Throwable e) {
                    // pass to subscriber
                    if (subscriber.isUnsubscribed()) return;
                    subscriber.onError(e);
                }
            }
        });
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get favourite locations
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    /**
     * Get an observable that returns the receipt details.
     *
     * @return Return an observable that returns emits the receipt details. The observable will emit a single
     *         list (the receipt details) then complete.
     */
    @NonNull public Observable<? extends List<? extends DRDbReceiptDetails>> getReceiptDetails() {
        final Dao<DRDbReceiptDetails, ?> dao = mDbHelper.getTable(DRDbReceiptDetails.class);
        return Observable.create(new Observable.OnSubscribe<List<? extends DRDbReceiptDetails>>() {
            @Override public void call(Subscriber<? super List<? extends DRDbReceiptDetails>> subscriber) {
                try {

                    // query all receipt details (CHANGE TO QUERY FOR ONES WITH MATCHING TAG??)
                    List<DRDbReceiptDetails> receipts = dao.queryForAll();

                    // pass to subscriber
                    if (subscriber.isUnsubscribed()) return;
                    subscriber.onNext(receipts);
                    subscriber.onCompleted();

                } catch (Throwable e) {
                    // pass to subscriber
                    if (subscriber.isUnsubscribed()) return;
                    subscriber.onError(e);
                }
            }
        });
    }
}
