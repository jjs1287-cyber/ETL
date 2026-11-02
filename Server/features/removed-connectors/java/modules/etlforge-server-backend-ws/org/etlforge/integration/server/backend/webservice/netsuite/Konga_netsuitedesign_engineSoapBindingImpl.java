package org.etlforge.integration.server.backend.webservice.netsuite;

import java.rmi.RemoteException;
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;

public class Konga_netsuitedesign_engineSoapBindingImpl implements NetSuiteDesignEngine {
   @Override
   public WsNetSuiteDefinitionContents getNetSuiteDefinitionContents(WsEndpointParameters var1) throws RemoteException {
      return ConnectorNetSuiteDesignEngineImpl.getNetSuiteDefinitionContents(var1);
   }

   @Override
   public WsNetSuiteObjectCustomFieldsNew getNetSuiteCustomFieldsNew(WsEndpointParameters var1, boolean var2, String var3) throws RemoteException {
      return ConnectorNetSuiteDesignEngineImpl.getNetSuiteCustomFieldsNew(var1, var2, var3);
   }

   @Override
   public WsNetSuiteSavedSearch getSavedSearch(WsEndpointParameters var1, String var2) throws RemoteException {
      return ConnectorNetSuiteDesignEngineImpl.getSavedSearch(var1, var2);
   }
}
