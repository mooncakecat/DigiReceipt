package com.compsci702.DigiReceipt.core;

import rx.Observable;
import rx.Single;
import rx.Subscriber;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.compsci702.DigiReceipt.database.DRDbHelper;
import com.compsci702.DigiReceipt.database.DRReceiptDb;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.j256.ormlite.dao.Dao;

import java.util.List;
import java.util.concurrent.Callable;

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
     * @return Return an observable that adds the receipts to the list of receipts
     * and emits a single value (the receipt from the database), then completes.
     * Unsubscribing from this observable will not cancel the addition.
     */
    @NonNull public Observable<DRReceiptDb> addReceipt(@NonNull final DRReceipt receipt) {
        final Dao<DRReceiptDb, ?> dao = mDbHelper.getTable(DRReceiptDb.class);
        return Observable.create(new Observable.OnSubscribe<DRReceiptDb>() {
            @Override public void call(Subscriber<? super DRReceiptDb> subscriber) {
                try {

                    final DRReceiptDb dbReceipt;

                    // create a database version of the location
                    dbReceipt = new DRReceiptDb(receipt);

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
     * get receipts
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    /**
     * Get an observable that returns the receipt details.
     *
     * @return Return an observable that returns emits the receipt details. The observable will emit a single
     * list (the receipt details) then complete.
     */
    @NonNull public Observable<? extends List<? extends DRReceiptDb>> getReceiptDetails() {
        final Dao<DRReceiptDb, ?> dao = mDbHelper.getTable(DRReceiptDb.class);
        return Observable.create(new Observable.OnSubscribe<List<? extends DRReceiptDb>>() {
            @Override public void call(Subscriber<? super List<? extends DRReceiptDb>> subscriber) {
                try {

                    // query all receipt details (CHANGE TO QUERY FOR ONES WITH MATCHING TAG??)
                    List<DRReceiptDb> receipts = dao.queryForAll();

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

    @NonNull Single<Cursor> getReceiptsForSearchQuery(final String searchString) {
        return Single.fromCallable(new Callable<Cursor>() {
            @Override public Cursor call() throws Exception {
                final DRDbHelper dbHelper = DRApplication.getDbHelper();

                // Removing all repeated spaces and spaces at end of string
                String cleanSearchString = searchString.trim().replaceAll("[ ]{2,}", " ");

                // Constructing where query string that finds any Items that have every search term somewhere in
                // their searchtext
                //noinspection StringBufferReplaceableByString
                StringBuilder whereStringBuilder = new StringBuilder();
                whereStringBuilder.append(DRReceiptDb.COLUMN_TAGS);
                whereStringBuilder.append(" LIKE '%");
                // Compares each word separately
                whereStringBuilder.append(cleanSearchString.replace(" ", "%' AND " + DRReceiptDb.COLUMN_TAGS + " LIKE" +
                        " '%"));
                // Ensure query is case insensitive
                whereStringBuilder.append("%' COLLATE NOCASE");

                return dbHelper.getReadableDatabase().query(DRDbHelper.TABLE_RECEIPT,
                        new String[]{DRReceiptDb.COLUMN_ID, DRReceiptDb.COLUMN_IMAGE_PATH, DRReceiptDb.COLUMN_TAGS},
                        whereStringBuilder.toString(),
                        null,
                        null,
                        null,
                        null);
            }
        });
    }
}
