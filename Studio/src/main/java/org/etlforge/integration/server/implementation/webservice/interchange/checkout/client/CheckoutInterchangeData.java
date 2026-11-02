package org.etlforge.integration.server.implementation.webservice.interchange.checkout.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CheckoutInterchangeData extends Remote {
   InterchangeDataHolder checkoutInterchangeProject(String var1, String var2, String var3) throws RemoteException;

   DownloadDataHolder downloadIntegrationProject(String var1, String var2, String var3) throws RemoteException;

   RequiredFiles downloadRequiredFiles(String var1, String var2, int var3, String var4) throws RemoteException;

   InterchangeDataHolder getOperationTree(String var1, String var2) throws RemoteException;

   CInterchangeProjects getInterchangeProjects(String var1, String var2) throws RemoteException;
}
