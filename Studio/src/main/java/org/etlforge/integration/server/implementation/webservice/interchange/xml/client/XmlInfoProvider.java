package org.etlforge.integration.server.implementation.webservice.interchange.xml.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface XmlInfoProvider extends Remote {
   CXmlStructureFile[] getXmlStructureFiles(String var1, String var2) throws RemoteException;

   CXmlStructureFileDownloadResult getXmlStructureFile(String var1, String var2, CXmlStructureFile var3) throws RemoteException;

   String[] getPossibleRootNames(String var1, String var2, CXmlStructureFile var3) throws RemoteException;

   CXmlStructureFileUploadResult uploadStructureFile(String var1, String var2, CXmlStructureFile var3, CXmlStructureFile var4) throws RemoteException;
}
