package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Stub;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TestOperationResult;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.integration.debug.serverapi.TestOperationProvider;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.DebuggingParameters;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProvider;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsTestOperationResult;

public final class TestOperationProviderWsImpl extends AbstractServerTestWsCall implements TestOperationProvider {
   public TestOperationProviderWsImpl(CppWebServiceConfiguration var1, DebugService var2) {
      super(var1, var2);
   }

   @Override
   public void testOperation(DebugSession var1, OperationId var2, TransformationId var3, ServerTestCallback var4) {
      this.testOperationImpl(var1, var2, var3, var4);
   }

   @Override
   public void resumeTest(DebugSession var1, ServerTestCallback var2) {
      this.testOperationImpl(var1, null, null, var2);
   }

   private void testOperationImpl(DebugSession var1, OperationId var2, TransformationId var3, ServerTestCallback var4) {
      if (this.init(var1, var4)) {
         try {
            TreeMapperInfoProvider var5 = this.getLocator().getkonga_treemapper_infoprovider();
            this.getConfiguration().setTimeOut((Stub)var5);
            DebuggingParameters var6 = WsDebuggingParametersBuilder.build(var1.getParams());
            WsTestOperationResult var7 = var5.testOperation(
               this.getUser(), this.getPassword(), var1.getDebuggeeId().toString(), var3 != null ? var3.toString() : null, var6
            );
            TestOperationResult var8 = TestOperationResultBuilder.build(var7, this.getServerId(), this.getServerName(), var1, getTargetNodePath(var1));
            if (Thread.currentThread().isInterrupted()) {
               var4.cancelled();
               return;
            }

            this.checkDebuggingResult(var1, var8, var4);
         } catch (RemoteException var9) {
            var4.caught(this.convert(var9));
         } catch (ServiceException var10) {
            var4.caught(new IntegrationServerException("Failed to call webservice: " + var10.getMessage(), var10));
         } catch (Exception var11) {
            var4.caught(var11);
         }
      }
   }
}
