package com.etlforge.integration.server.implementation.webservice.license;

import java.rmi.RemoteException;

public final class Konga_licenseserviceSoapBindingImpl implements WsLicenseService {
   @Override
   public WsKeyValue[] getLicenseKeyGenerationInfo(String var1, String var2) throws RemoteException {
      GetLicenseKeyGenerationInfoTask var3 = new GetLicenseKeyGenerationInfoTask(var1, var2);
      return var3.execute();
   }

   @Override
   public WsKeyValue[] getLicenseAgreement(String var1, String var2, String var3) throws RemoteException {
      GetLicenseAgreementTask var4 = new GetLicenseAgreementTask(var1, var2, var3);
      return var4.execute();
   }

   @Override
   public WsKeyValue[] activateLicense(String var1, String var2, String var3) throws RemoteException {
      ActivateLicenseTask var4 = new ActivateLicenseTask(var1, var2, var3);
      return var4.execute();
   }
}
