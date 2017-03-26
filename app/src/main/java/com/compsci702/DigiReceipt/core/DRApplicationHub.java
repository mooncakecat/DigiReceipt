package com.compsci702.DigiReceipt.core;

/**
 * Application hub.
 */
public class DRApplicationHub {

	private final DRDatabaseHub mDatabaseHub = DRApplication.getDatabaseHub();
	private final DRNetworkHub mNetworkHub = DRApplication.getNetworkHub();

}
