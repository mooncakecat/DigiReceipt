package com.compsci702.DigiReceipt.core;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.compsci702.DigiReceipt.database.DRDbHelper;

/**
 * Application class.
 */
public class  DRApplication extends Application {

  public static DRApplication sInstance;
  public static Context sContext;
  private static DRApplicationHub sApplicationHub;
  private static DRDatabaseHub sDatabaseHub;
  private static DRNetworkHub sNetworkHub;
  private static DRDbHelper sDbHelper;

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * constructor
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  public DRApplication() {sInstance = this;}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * context
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */


  /**
   * Return the application context
   *
   * @return returns the application context
   */
  @NonNull
  public static Context getContext() {return sInstance.getApplicationContext();}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * application hub
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */


  /**
   * Get the application hub.
   *
   * @return the application hub.
   */
  @NonNull
  public static DRApplicationHub getApplicationHub() {
    if (sApplicationHub == null) sApplicationHub = new DRApplicationHub();
    return sApplicationHub;
  }

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * database hub
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  /**
   * Get the database hub.
   *
   * @return the database hub.
   */
  @NonNull
  public static DRDatabaseHub getDatabaseHub() {
    if (sDatabaseHub == null) sDatabaseHub = new DRDatabaseHub();
    return sDatabaseHub;
  }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * db helper
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  /**
   * Get the db helper.
   *
   * @return the db helper.
   */
  @NonNull public static DRDbHelper getDbHelper() {
    if (sDbHelper == null) sDbHelper = new DRDbHelper(getContext());
    return sDbHelper;
  }
  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * network hub
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  /**
   * Get the network hub.
   *
   * @return the network hub.
     */
  public static DRNetworkHub getNetworkHub() {
    if (sNetworkHub == null) sNetworkHub = new DRNetworkHub();
    return sNetworkHub;
  }
}
