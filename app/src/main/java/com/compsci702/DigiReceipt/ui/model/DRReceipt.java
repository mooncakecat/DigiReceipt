package com.compsci702.DigiReceipt.ui.model;

/**
 * A receipt
 */
public interface DRReceipt {
    int getId();

    String text = "";

    String getFilename();

    String getTags();
}
