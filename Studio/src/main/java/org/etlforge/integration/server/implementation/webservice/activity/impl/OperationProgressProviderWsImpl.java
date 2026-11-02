package org.etlforge.integration.server.implementation.webservice.activity.impl;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.activity.client.progress.OperationProgressMessage;
import org.etlforge.integration.activity.serverapi.progress.OperationProgressCallback;
import org.etlforge.integration.activity.serverapi.progress.OperationProgressProvider;
import org.etlforge.integration.activity.serverapi.progress.OperationProgressRequest;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.server.implementation.webservice.activity.client.ActivityInfoProvider;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationInstanceFilter;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationProgress;
import org.etlforge.lang.Arrays2;

public final class OperationProgressProviderWsImpl extends AbstractActivityInfoProviderWsImpl implements OperationProgressProvider {
   public OperationProgressProviderWsImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void getOperationProgress(OperationProgressRequest var1, OperationProgressCallback var2) {
      try {
         ActivityInfoProvider var3 = this.getServerProxy(var2);
         if (var3 == null) {
            return;
         }

         WsOperationInstanceFilter var4 = startFilter(var1);
         if (Thread.currentThread().isInterrupted()) {
            var2.cancelled();
            return;
         }

         WsOperationProgress[] var5 = var3.getOperationProgress(this.userName, this.password, var4);
         if (Thread.currentThread().isInterrupted()) {
            var2.cancelled();
            return;
         }

         var2.handleProgressMessages(createClientSideResponse(var5));
      } catch (RemoteException var6) {
         var2.caught(this.convert(var6));
      }
   }

   private static List<OperationProgressMessage> createClientSideResponse(WsOperationProgress[] var0) {
      return var0 == null ? Collections.emptyList() : Arrays2.toList(var0, OperationProgressMessage.class, new Functions.Progress());
   }
}
