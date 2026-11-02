package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Stub;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerCallbackAdapter;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.debug.client.BreakLocation;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TargetNode;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.serverapi.ServerTestCall;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.DebuggingParameters;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProvider;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProviderServiceLocator;

abstract class AbstractServerTestWsCall extends CppWebServiceServerCall implements ServerTestCall {
   private static final String WS_NAME = "konga_treemapper_infoprovider";
   private final DebugService debugService;
   private TreeMapperInfoProviderServiceLocator locator;
   private String user;
   private String password;
   private ServerGuid serverId;
   private String serverName;

   public AbstractServerTestWsCall(CppWebServiceConfiguration var1, DebugService var2) {
      super(var1);
      this.debugService = var2;
   }

   @Override
   public void stopTest(DebugSession var1) {
      if (this.init(var1, new ServerCallbackAdapter() {})) {
         try {
            TreeMapperInfoProvider var2 = this.getLocator().getkonga_treemapper_infoprovider();
            this.getConfiguration().setTimeOut((Stub)var2);
            var2.stop(this.getUser(), this.getPassword(), var1.getId().toString());
         } catch (RemoteException var3) {
            this.logStopError(var3);
         } catch (ServiceException var4) {
            this.logStopError(new IntegrationServerException("Failed to call webservice: " + var4.getMessage(), var4));
         } catch (Exception var5) {
            this.logStopError(var5);
         }
      }
   }

   protected final boolean init(DebugSession var1, ServerCallback var2) {
      try {
         this.locator = new TreeMapperInfoProviderServiceLocator();
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(this.locator, "konga_treemapper_infoprovider", var3, var2)) {
            return false;
         }

         this.locator.setkonga_treemapper_infoproviderEndpointAddress(var3.toString());
         this.user = this.getConfiguration().getUserName();
         this.password = this.getConfiguration().getPasswordHash();
         this.serverId = this.getConfiguration().getServerId();
         this.serverName = this.getConfiguration().getServerName();
         return true;
      } catch (Exception var4) {
         var2.caught(var4);
         return false;
      }
   }

   protected static final DebuggingParameters getDefaultDebuggingParams() {
      DebuggingParameters var0 = new DebuggingParameters();
      var0.setCommand("Run");
      return var0;
   }

   protected static final String getTargetNodePath(DebugSession var0) {
      TargetNode var1 = var0.getTargetNode();
      return var1 == null ? null : var1.getPath();
   }

   protected final void checkDebuggingResult(DebugSession var1, TestResult var2, ServerTestCallback var3) {
      if (!var1.isIsolatedTest() && !var1.getId().equals(this.debugService.getCurrentSessionId())) {
         var3.cancelled();
      } else if (isTestComplete(var2)) {
         var3.completed(var1, var2);
      } else {
         var1.paused(var2);
      }
   }

   private static boolean isTestComplete(TestResult var0) {
      BreakLocation[] var1 = var0.getCallStack();
      return var1.length == 0;
   }

   protected final TreeMapperInfoProviderServiceLocator getLocator() {
      return this.locator;
   }

   protected final String getUser() {
      return this.user;
   }

   protected final String getPassword() {
      return this.password;
   }

   protected final ServerGuid getServerId() {
      return this.serverId;
   }

   protected final String getServerName() {
      return this.serverName;
   }

   private void logStopError(Exception var1) {
      ErrorLog.log(this.getClass(), "An error occurred on the server side while handling the stop command.", var1);
   }
}
