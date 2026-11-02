package com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BulkLoadInfoProvider extends Remote {
   WsBulkLoadInstance[] listInstances(String var1, String var2, WsInstanceFilter var3) throws RemoteException;

   WsRecordOfSentBatch[] listBatches(String var1, String var2, WsBulkLoadInstanceId var3) throws RemoteException;
}
