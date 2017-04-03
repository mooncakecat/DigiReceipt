package com.compsci702.DigiReceipt.ui.model;

import android.support.annotation.NonNull;

/**
 * A receipt
 */
public interface DRReceipt {
    @NonNull int getId();

    @NonNull String getFilename();
}
