package com.etlforge.integration.server.implementation.webservice.projectadmin.backup;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProjectBackupAdministration extends Remote {
   WsGetBackupListResult getListOfBackups(String var1, String var2, String var3) throws RemoteException;
}
