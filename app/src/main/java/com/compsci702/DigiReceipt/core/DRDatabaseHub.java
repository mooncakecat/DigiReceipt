package com.compsci702.DigiReceipt.core;

import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.compsci702.DigiReceipt.database.DRDbHelper;
import com.compsci702.DigiReceipt.database.DRReceiptDb;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Single;
import rx.Subscriber;

/**
 * Database Hub.
 */
public class DRDatabaseHub {

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     * member variables
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    @NonNull private final DRDbHelper mDbHelper;

	private Dao<DRReceiptDb, Integer> receiptDao;


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
                    // create a database version of the receipt
                    dbReceipt = new DRReceiptDb(receipt);

                    // create or update the receipt
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
     * Returns all the entries in the DB as a list
     *
     * list (the receipt details) then complete.
     * @return Return a list of receipts.
     */
    public List<DRReceiptDb> getReceipts() throws SQLException {
        try{receiptDao = mDbHelper.getReceiptDao(); }
        catch(SQLException e){e.printStackTrace();}
        return receiptDao.queryForAll();
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     * search receipts
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public Single<List<? extends DRReceipt>> searchReceipts(@NonNull final String query){
		return Single.fromCallable(new Callable<List<? extends DRReceipt>>() {
			@Override public List<? extends DRReceipt> call() throws Exception {
				PreparedQuery preparedQuery = receiptDao.queryBuilder().where().like(DRReceiptDb.COLUMN_TAGS, "%"
                        + query + "%").prepare();
				List<DRReceiptDb> receipts = receiptDao.query(preparedQuery);
				return receipts;
			}
		});
	}
}