package com.compsci702.DigiReceipt.data;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;

public class ReceiptDetails implements Serializable{

    public static final String TABLE_NAME = "Receipt";
    public static final String COLUMN_RECEIPT_ID = TABLE_NAME + "_id";
    public static final String COLUMN_IMAGE_PATH = TABLE_NAME + "_image_path";
    public static final String COLUMN_TAGS = TABLE_NAME + "_tags";

    //Primary key is an auto generated integer
    @DatabaseField(generatedId = true, columnName = COLUMN_RECEIPT_ID)
    private int receiptID;

    //Path to image of receipt
    @DatabaseField(columnName = COLUMN_IMAGE_PATH)
    private String imagePath;

    //Words related to the receipt (MAY CHANGE TO ARRAY?)
    @DatabaseField(columnName = COLUMN_TAGS)
    private String tags;

    public ReceiptDetails(){}

    public ReceiptDetails(final String imagePath, final String words){
        this.imagePath = imagePath;
        this.words = words;
    }
}
