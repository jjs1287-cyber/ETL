package org.etlforge.integration.server.implementation.webservice.filemanagement.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileManager extends Remote {
   CFileDownloadResult downloadFile(String var1, String var2, CDownloadedFile var3) throws RemoteException;
}
