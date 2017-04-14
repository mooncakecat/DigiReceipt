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
    if (!mediaStorageDir.exists()){
      if (!mediaStorageDir.mkdirs()){
        return null;
      }
    }

    @SuppressLint("SimpleDateFormat")
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    return new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
  }
}
