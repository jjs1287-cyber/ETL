package org.etlforge.integration.server.implementation.webservice.pluginmanagement.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PluginManager extends Remote {
   FilterAsXml[] getFilters(String var1, String var2) throws RemoteException;
}
