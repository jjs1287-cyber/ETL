package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DeployInterchangeData extends Remote {
   CDeployProjectCheckResult checkProjectForDeploy(String var1, String var2, String var3, CIntegrationEntity[] var4) throws RemoteException;

   CDeployResults deployInterchangeData(String var1, String var2, InterchangeIdHolder var3, InterchangeDataHolder var4) throws RemoteException;

   CFileCheckResult checkRequiredFiles(String var1, String var2, CFileDependencies var3, CRequiredFiles var4) throws RemoteException;

   CDeployResults uploadJtrFiles(String var1, String var2, CJtrFiles var3) throws RemoteException;

   CDeployResults uploadJitterpak(String var1, String var2, CUploadedJitterpak var3) throws RemoteException;

   CDeployResults deleteInterchangeProjects(String var1, String var2, String[] var3) throws RemoteException;
}
