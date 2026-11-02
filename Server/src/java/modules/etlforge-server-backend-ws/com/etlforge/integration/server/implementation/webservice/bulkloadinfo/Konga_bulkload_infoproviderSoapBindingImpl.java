package com.etlforge.integration.server.implementation.webservice.bulkloadinfo;

import java.rmi.RemoteException;

public class Konga_bulkload_infoproviderSoapBindingImpl implements BulkLoadInfoProvider {
   @Override
   public WsBulkLoadInstance[] listInstances(String var1, String var2, WsInstanceFilter var3) throws RemoteException {
      ListInstancesTask var4 = new ListInstancesTask(var1, var2, var3);
      return var4.execute();
   }

   @Override
   public WsRecordOfSentBatch[] listBatches(String var1, String var2, WsBulkLoadInstanceId var3) throws RemoteException {
      ListBatchesTask var4 = new ListBatchesTask(var1, var2, var3);
      return var4.execute();
   }
}
