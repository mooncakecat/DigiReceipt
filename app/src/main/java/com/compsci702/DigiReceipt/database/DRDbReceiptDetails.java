package com.compsci702.DigiReceipt.database;

import android.support.annotation.NonNull;

import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = DRDbReceiptDetails.TABLE_NAME)
public class DRDbReceiptDetails implements DRReceipt {

    public static final String TABLE_NAME = "ReceiptDetails";
    public static final String LOCAL_DATABASE_ID = TABLE_NAME + "id";
    public static final String COLUMN_RECEIPT_ID = TABLE_NAME + "_receipt_id";
    public static final String COLUMN_IMAGE_PATH = TABLE_NAME + "_receipt_image_path";
    public static final String COLUMN_TAGS = TABLE_NAME + "_receipt_tags";


    @DatabaseField(generatedId = true, columnName = LOCAL_DATABASE_ID)
    private int dbID;

    //Primary key is an auto generated integer
    @DatabaseField(columnName = COLUMN_RECEIPT_ID)
    private int receiptID;

    //Path to image of receipt
    @DatabaseField(columnName = COLUMN_IMAGE_PATH)
    private String imagePath;

    //Words related to the receipt (MAY CHANGE TO ARRAY?)
    @DatabaseField(columnName = COLUMN_TAGS)
    private String tags;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * constructors
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public DRDbReceiptDetails(){}

    public DRDbReceiptDetails(@NonNull DRReceipt receipt){
        receiptID = receipt.getId();
        imagePath = receipt.getFilename();
        //GET TAGS FROM API
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * getters
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public int getLocalDatabaseID() {return dbID;}
    public String getTags(){return tags;}
    @NonNull @Override
    public int getId() {return receiptID;}
    @NonNull @Override
    public String getFilename() {return imagePath;}
}
