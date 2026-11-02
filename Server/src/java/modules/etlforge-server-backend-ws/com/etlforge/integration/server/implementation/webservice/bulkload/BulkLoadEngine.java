package com.etlforge.integration.server.implementation.webservice.bulkload;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BulkLoadEngine extends Remote {
   WsStartNewBulkLoadSessionResult startNewBulkLoadSession(String var1, String var2) throws RemoteException;

   WsBulkLoadResult runBulkLoad(WsBulkLoadSessionId var1, WsBulkLoadParams var2, WsSalesforceEndpoint var3, WsKeyValue[] var4) throws RemoteException;

   WsBulkLoadError closeBulkLoadSession(WsBulkLoadSessionId var1) throws RemoteException;
}
