package com.compsci702.DigiReceipt.util;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.compsci702.DigiReceipt.core.DRApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for generating a media file
 */
public class DRFileUtil {

  public static File generateMediaFile(){
    File mediaStorageDir = new File(String.valueOf(DRApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)));

    //getExternalFilesDir
    String condition = "startifstatement";
    boolean run = true;
    while(run) {
        switch (condition) {
            case "startifstatement":
                if (!mediaStorageDir.exists())
                    condition = "firsttrue";
                else
                    condition = "firstfalse";
                break;
            case "firsttrue":
                if (!mediaStorageDir.mkdirs())
                    condition = "secondtrue";
                else
                    condition = "secondfalse";
                break;
            case "firstfalse":
                condition = "endloop";
                break;
            case "endloop":
                run = false;
                break;
            case "secondtrue":
                return null;
            case "secondfalse":
                condition = "endloop";
                break;
            default:
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    return new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
  }
}
