package com.compsci702.DigiReceipt.data;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;

public class ReceiptDetails implements Serializable{

    //Primary key is an auto generated integer
    @DatabaseField(generatedId = true, columnName = "receiptID")
    public int receiptID;

    //Path to image of receipt
    public String imagePath;

    //Words related to the receipt (MAY CHANGE TO ARRAY?)
    public String words;

    public ReceiptDetails(){}

    public ReceiptDetails(final String imagePath, final String words){
        this.imagePath = imagePath;
        this.words = words;
    }
}
