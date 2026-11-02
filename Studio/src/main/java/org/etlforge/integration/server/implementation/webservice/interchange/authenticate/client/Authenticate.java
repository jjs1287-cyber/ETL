package org.etlforge.integration.server.implementation.webservice.interchange.authenticate.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;

public interface Authenticate extends Remote {
   AuthenticateResult login(String var1, String var2) throws RemoteException;

   AuthenticateResult loginWithVersion(String var1, String var2, String var3) throws RemoteException;

   AuthenticateResult loginWithVersionAndKeyValues(String var1, String var2, String var3, WsKeyValuePairs var4) throws RemoteException;
}
