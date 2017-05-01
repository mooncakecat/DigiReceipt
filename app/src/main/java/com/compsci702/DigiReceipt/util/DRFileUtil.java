package com.compsci702.DigiReceipt.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.compsci702.DigiReceipt.core.DRApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for generating a media file
 */
public class DRFileUtil {

  public static File generateMediaFile(){
    File mediaStorageDir = null;
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      mediaStorageDir = new File(String.valueOf(DRApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
    }else{
      mediaStorageDir = new File(DRApplication.getContext().getFilesDir(), "DigiReceipt");

    }
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
