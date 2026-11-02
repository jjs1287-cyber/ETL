package org.etlforge.integration.server.implementation.webservice.useradmin.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;

public interface UserAdmin extends Remote {
   UsersAndGroups getUsersAndGroups(String var1, String var2) throws RemoteException;

   UsersAndGroups getUsersAndGroupsWithOptions(String var1, String var2, WsKeyValuePairs var3) throws RemoteException;

   void setUsersAndGroups(String var1, String var2, UsersAndGroups var3) throws RemoteException;

   void setUserInfo(String var1, String var2, User var3) throws RemoteException;

   User getUserInfo(String var1, String var2) throws RemoteException;
}
