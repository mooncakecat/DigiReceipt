package com.compsci702.DigiReceipt.core;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Application class.
 */
public class DRApplication extends Application {

  private static DRApplication sInstance;
  private static DRApplicationHub sApplicationHub;
  private static DRDatabaseHub sDatabaseHub;
  private static DRNetworkHub sNetworkHub;

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * constructor
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  public DRApplication() {
    sInstance = this;
  }

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * context
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

  /**
   * Return the application context
   *
   * @return returns the application context
   */
  @NonNull
  public static Context getContext() {
    return sInstance.getApplicationContext();
  }

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
