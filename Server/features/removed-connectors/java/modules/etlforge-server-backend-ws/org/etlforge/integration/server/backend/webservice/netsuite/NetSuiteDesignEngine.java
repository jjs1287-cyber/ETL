package org.etlforge.integration.server.backend.webservice.netsuite;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;

public interface NetSuiteDesignEngine extends Remote {
   WsNetSuiteSavedSearch getSavedSearch(WsEndpointParameters var1, String var2) throws RemoteException;

   WsNetSuiteObjectCustomFieldsNew getNetSuiteCustomFieldsNew(WsEndpointParameters var1, boolean var2, String var3) throws RemoteException;

   WsNetSuiteDefinitionContents getNetSuiteDefinitionContents(WsEndpointParameters var1) throws RemoteException;
}
