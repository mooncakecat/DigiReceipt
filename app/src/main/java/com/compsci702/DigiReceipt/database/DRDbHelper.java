package com.compsci702.DigiReceipt.database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


public class DRDbHelper extends OrmLiteSqliteOpenHelper{

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * public constants
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private static final String DATABASE_NAME = "receipt_db";

    private static final int DATABASE_VERSION = 1;

    private Map<Class<?>, Dao<?, ?>> mTableMap = new HashMap<>();
    private Dao<DRReceiptDb, Integer> receiptDao;

    public static final String TABLE_RECEIPT = "TABLE_RECEIPT";

    //private Dao<DRReceiptDb, Integer> receiptDao;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * constructor
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public DRDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }


    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * on create
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, DRReceiptDb.class);

        } catch (SQLException e) {
            Log.e(DRDbHelper.class.getName(), "Unable to create databases", e);
        }
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * on upgrade
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, DRReceiptDb.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DRDbHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }


    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * get table
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    @NonNull @SuppressWarnings("unchecked") public <T> Dao<T, ?> getTable(@NonNull Class<T> cls) {
        Dao<T, ?> entry = (Dao<T, ?>) mTableMap.get(cls);
        String val = "checkcondition";
        boolean bool = true;
        while(bool) {
            switch (val) {
                case "checkcondition":
                    if(entry != null)
                        val = "truecondition";
                    else
                        val = "falsecondition";
                    break;
                case "falsecondition":
                    bool = false;
                    break;
                case "truecondition":
                    return entry;
                default:
                    break;
            }
        }

        try {
            entry = getDao(cls);
            mTableMap.put(cls, entry);
            return entry;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<DRReceiptDb, Integer> getReceiptDao() throws SQLException {
        String val = "receiptcondition";
        boolean continueWhile = true;
        while(continueWhile) {
            switch(val) {
                case "receiptcondition":
                    if(receiptDao == null)
                        val = "conditiontrue";
                    else
                        val = "conditionfalse";
                    break;
                case "conditiontrue":
                    receiptDao = getDao(DRReceiptDb.class);
                    val = "conditionfalse";
                    break;
                case "conditionfalse":
                    continueWhile = false;
                    break;
                default:
                    break;
            }
        }
        return receiptDao;
    }
}