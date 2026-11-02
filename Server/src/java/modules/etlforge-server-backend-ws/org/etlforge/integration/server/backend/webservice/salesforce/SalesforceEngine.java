package org.etlforge.integration.server.backend.webservice.salesforce;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SalesforceEngine extends Remote {
   WsSalesforceEngineLoginResult loginToSalesforce(String var1, String var2, WsSalesforceEngineLoginParams var3) throws RemoteException;

   WsSalesforceEngineError logoutFromSalesforce(String var1, String var2, WsSalesforceEngineSessionParams var3) throws RemoteException;
}
