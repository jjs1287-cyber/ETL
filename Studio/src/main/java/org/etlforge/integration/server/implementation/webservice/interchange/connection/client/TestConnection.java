package org.etlforge.integration.server.implementation.webservice.interchange.connection.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TestConnection extends Remote {
   FileList testFileTypeConnection(String var1, String var2, ConnectionInfo var3, String var4, boolean var5) throws RemoteException;

   String testDatabaseConnection(String var1, String var2, ConnectionInfo var3, String var4, boolean var5) throws RemoteException;

   String testLdapConnection(String var1, String var2, ConnectionInfo var3, String var4, boolean var5) throws RemoteException;
}
