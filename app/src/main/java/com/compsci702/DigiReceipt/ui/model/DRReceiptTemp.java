package com.compsci702.DigiReceipt.ui.model;

/**
 *
 */

public class DRReceiptTemp {

    //text is placeholder only since I don't have an actual phone to take photos of receipts
    //private String text = "BananaOrange apple";
    private String text = "";

    public DRReceiptTemp (String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }
}
