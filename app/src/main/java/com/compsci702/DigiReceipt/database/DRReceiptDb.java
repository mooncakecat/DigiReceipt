package com.compsci702.DigiReceipt.database;

import android.support.annotation.NonNull;

import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Implementation of a {@link DRReceipt} to be persisted to the database
 */
@DatabaseTable(tableName = DRDbHelper.TABLE_RECEIPT)
public class DRReceiptDb implements DRReceipt {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE_PATH = "filename";
    public static final String COLUMN_TAGS = "tags";

    @DatabaseField(generatedId = true, columnName = COLUMN_ID) private int mId;
    @DatabaseField(columnName = COLUMN_IMAGE_PATH) private String mFilename;
    @DatabaseField(columnName = COLUMN_TAGS) private String mTags;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     * constructors
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public DRReceiptDb() {
        // Empty constructor required for ORMLite
    }

    public DRReceiptDb(@NonNull DRReceipt receipt) {
        mId = receipt.getId();
        mFilename = receipt.getFilename();
        mTags = receipt.getText();
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     * getters
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    @NonNull @Override public int getId() {
        return mId;
    }

    @NonNull @Override public String getFilename() {
        return mFilename;
    }

    // currently there is nothing stopping the tags from being empty, i.e. take a photo that has no receipt in it
    public String getText() {
        return mTags;
    }
}