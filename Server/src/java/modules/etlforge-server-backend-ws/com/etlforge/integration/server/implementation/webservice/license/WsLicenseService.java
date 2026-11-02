package com.etlforge.integration.server.implementation.webservice.license;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WsLicenseService extends Remote {
   WsKeyValue[] getLicenseKeyGenerationInfo(String var1, String var2) throws RemoteException;

   WsKeyValue[] activateLicense(String var1, String var2, String var3) throws RemoteException;

   WsKeyValue[] getLicenseAgreement(String var1, String var2, String var3) throws RemoteException;
}
