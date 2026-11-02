package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Stub;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.common.server.ClientServerCommunicationUtils;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TargetNode;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.de.DataElementScope;
import org.etlforge.integration.debug.client.de.DataElementUtils;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.integration.debug.serverapi.TestScriptProvider;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.DebuggingParameters;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProvider;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsTestResult;

public final class TestScriptProviderWsImpl extends AbstractServerTestWsCall implements TestScriptProvider {
   public TestScriptProviderWsImpl(CppWebServiceConfiguration var1, DebugService var2) {
      super(var1, var2);
   }

   @Override
   public void testExpression(DebugSession var1, String var2, ServerTestCallback var3) {
      this.testExpressionImpl(var1, var2, var3);
   }

   @Override
   public void resumeTest(DebugSession var1, ServerTestCallback var2) {
      this.testExpressionImpl(var1, null, var2);
   }

   private void testExpressionImpl(DebugSession var1, String var2, ServerTestCallback var3) {
      if (Thread.currentThread().isInterrupted()) {
         var3.cancelled();
      } else if (this.init(var1, var3)) {
         if (Thread.currentThread().isInterrupted()) {
            var3.cancelled();
         } else {
            try {
               TreeMapperInfoProvider var4 = this.getLocator().getkonga_treemapper_infoprovider();
               this.getConfiguration().setTimeOut((Stub)var4);
               DebuggingParameters var5 = WsDebuggingParametersBuilder.build(var1.getParams());
               String var6 = this.getSourceDataElementString(var1);
               String var7 = ClientServerCommunicationUtils.encodeString(var2);
               WsTestResult var8 = var4.testExpression(this.getUser(), this.getPassword(), var6, var7, var5);
               this.decodeResultString(var8);
               if (Thread.currentThread().isInterrupted()) {
                  var3.cancelled();
                  return;
               }

               TestResult var9 = TestResultBuilder.build(var8, this.getServerId(), this.getServerName(), var1, getTargetNodePath(var1));
               this.checkDebuggingResult(var1, var9, var3);
            } catch (RemoteException var10) {
               var3.caught(this.convert(var10));
            } catch (ServiceException var11) {
               var3.caught(new IntegrationServerException("Failed to call webservice: " + var11.getMessage(), var11));
            } catch (Exception var12) {
               var3.caught(var12);
            }
         }
      }
   }

   private String getSourceDataElementString(DebugSession var1) {
      String var2 = DataElementUtils.toNameValuePairString(var1.getParams().getDataElements(), DataElementScope.SOURCE);
      TargetNode var3 = var1.getTargetNode();
      String var4 = var3 == null ? "" : var3.encode();
      return ClientServerCommunicationUtils.encodeString(var2 + var4);
   }

   private void decodeResultString(WsTestResult var1) {
      String var2 = var1.getResult();
      String var3 = ClientServerCommunicationUtils.decodeString(var2);
      var1.setResult(var3);
   }
}
