package org.etlforge.integration.server.implementation.webservice.interchange.webservice.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WebServiceInfoProvider extends Remote {
   CWsdlFiles getWsdlFiles(String var1, String var2) throws RemoteException;

   CWsdlFileDownloadResult downloadWsdl(String var1, String var2, CWsdlFile var3) throws RemoteException;

   CWsdlFileUploadResult uploadWsdl(String var1, String var2, CWsdlFile var3, CWsdlFile var4) throws RemoteException;

   SoapSchemaGenerationResult generateSoapSchema(String var1, String var2, WebServiceOperationData var3, String var4, String var5, boolean var6, boolean var7) throws RemoteException;

   String[] deleteWsdl(String var1, String var2, CWsdlFile var3, boolean var4) throws RemoteException;

   WebServiceJtr getInputAndOutputJTR(String var1, String var2, WebServiceOperationData var3) throws RemoteException;
}
