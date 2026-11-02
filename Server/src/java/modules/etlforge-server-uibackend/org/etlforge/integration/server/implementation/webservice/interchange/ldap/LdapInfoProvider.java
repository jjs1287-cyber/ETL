package org.etlforge.integration.server.implementation.webservice.interchange.ldap;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;

public interface LdapInfoProvider extends Remote {
   WsObjectClassInfoReturn getObjectClasses(
      String var1, String var2, String var3, String var4, WsLdapConnectionParams var5, String var6, boolean var7, boolean var8, WsKeyValuePairs var9
   ) throws RemoteException;

   WsClassStructureReturn getObjectStructures(
      String var1, String var2, String var3, String var4, WsLdapConnectionParams var5, String[] var6, boolean var7, WsKeyValuePairs var8
   ) throws RemoteException;
}
