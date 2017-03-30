package com.compsci702.DigiReceipt.util;

import android.support.annotation.NonNull;

/**
 * Utility class to help load images
 */
public class DRImageUtil {

  // FIXME: 3/31/2017 replace with correct folder
  private static final String URL_ASSETS_IMAGE_FOLDER = "file:///android_asset/images/";

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * getImageUrl
 	* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  /**
   * Finds the URL for an image in the assets folder
   *
   * @param fileName File name of image whose url is being obtained
   * @return String containing the image url
   */
  public static String getImageUrl(@NonNull String fileName) {
    return URL_ASSETS_IMAGE_FOLDER + fileName;
  }

}
