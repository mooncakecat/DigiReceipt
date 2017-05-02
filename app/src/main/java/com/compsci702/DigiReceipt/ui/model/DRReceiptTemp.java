package com.compsci702.DigiReceipt.ui.model;

/**
 *A temporary receipt holding only text
 */

public class DRReceiptTemp {

    private String text = "";

    public DRReceiptTemp (String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }
}
