package com.etlforge.integration.server.implementation.webservice.connector;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.connector.serverapi.TestNetSuiteQueryApi;
import org.etlforge.integration.client.connector.serverapi.TestNetSuiteQueryCallback;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceServerCall;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchResponse;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.server.backend.webservice.connector.client.ETLForgeConnectorEngine;
import org.etlforge.integration.server.backend.webservice.connector.client.WsConnectorExecutionResponse;
import org.etlforge.integration.server.backend.webservice.connector.client.WsConnectorInput;
import org.etlforge.integration.server.backend.webservice.connector.client.WsStatus;

public final class TestNetSuiteQueryApiSoapImpl extends JavaWebServiceServerCall implements TestNetSuiteQueryApi {
   public TestNetSuiteQueryApiSoapImpl(JavaWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void testQuery(NetSuiteEndpoint var1, ConnectorObject var2, String var3, TestNetSuiteQueryCallback var4) {
      TestNetSuiteQueryApiSoapImpl.Worker var5 = new TestNetSuiteQueryApiSoapImpl.Worker(this.getConfiguration());
      var5.testQuery(var1, var2, var3, var4);
   }

   private static class Worker extends ConnectorWsBase {
      public Worker(WebServiceConfiguration var1) {
         super(var1);
      }

      public void testQuery(NetSuiteEndpoint var1, ConnectorObject var2, String var3, TestNetSuiteQueryCallback var4) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         Preconditions.checkNotNull(var3);
         Preconditions.checkNotNull(var4);

         try {
            ETLForgeConnectorEngine var5 = this.getServerProxy(var4);
            if (var5 == null) {
               return;
            }

            ArrayList var6 = Lists.newArrayList();
            var6.add(NetSuiteFunction.FunctionType.QUERY);
            var6.add(var2);
            WsConnectorInput var7 = this.createWsConnectorInput(var1, var6);
            WsConnectorExecutionResponse var8 = var5.executeConnector(var7, var3);
            TestNetSuiteQueryApi.Result var9 = translateResult(var8);
            var4.testComplete(var9);
         } catch (RemoteException var10) {
            var4.caught(this.convert(var10));
         } catch (ServiceException var11) {
            var4.caught(new IntegrationServerException("Failed to call webservice: " + var11.getMessage(), var11));
         }
      }

      private static TestNetSuiteQueryApi.Result translateResult(WsConnectorExecutionResponse var0) {
         WsStatus var1 = var0.getStatus();
         int var2 = var1.getStatusCode();
         String var3 = var0.getConnectorResponse();
         if (var2 == 0) {
            NetSuiteSearchResponse var4 = NetSuiteSearchResponse.getNetSuiteSearchResponse(var3);
            return TestNetSuiteQueryApi.Result.success(var4);
         } else {
            return TestNetSuiteQueryApi.Result.failure(var2, var1.getMessage());
         }
      }
   }
}
