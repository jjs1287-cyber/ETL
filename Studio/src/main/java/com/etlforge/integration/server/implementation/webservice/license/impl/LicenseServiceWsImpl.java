package com.etlforge.integration.server.implementation.webservice.license.impl;

import com.etlforge.integration.license.LicenseInfo;
import com.etlforge.integration.license.server.ActivateLicenseCallback;
import com.etlforge.integration.license.server.CheckLicenseCallback;
import com.etlforge.integration.license.server.GetLicenseAgreementCallback;
import com.etlforge.integration.license.server.GetLicenseKeyGenerationInfoCallback;
import com.etlforge.integration.license.server.LicenseKeyGenerationInfo;
import com.etlforge.integration.license.server.LicenseService;
import com.etlforge.integration.server.implementation.webservice.license.client.WsKeyValue;
import com.etlforge.integration.server.implementation.webservice.license.client.WsLicenseService;
import com.etlforge.integration.server.implementation.webservice.license.client.WsLicenseServiceServiceLocator;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.zip.DataFormatException;
import javax.xml.rpc.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginKeyValuesHandler;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceServerCall;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.server.implementation.webservice.interchange.authenticate.impl.AuthenticateWsImpl;
import org.etlforge.util.zip.ZipUtils;

public final class LicenseServiceWsImpl extends JavaWebServiceServerCall implements LicenseService {
   private static final String SERVICE_NAME = "konga_licenseservice";
   private final AuthenticateWsImpl authenticator;

   public LicenseServiceWsImpl(JavaWebServiceConfiguration var1, AuthenticateWsImpl var2) {
      super(var1);
      this.authenticator = var2;
   }

   @Override
   public void getLicenseKeyGenerationInfo(LoginCredentials var1, GetLicenseKeyGenerationInfoCallback var2) {
      if (Thread.currentThread().isInterrupted()) {
         var2.cancelled();
      } else {
         try {
            WsLicenseService var3 = this.getService(var1);
            WsKeyValue[] var4 = var3.getLicenseKeyGenerationInfo(var1.getUser(), var1.getPasswordHash());
            ServerInfo var5 = this.getServerInfo(var4, var1);
            LicenseKeyGenerationInfo var6 = new WsKeyValueToLicenseKeyGenerationInfo(var5).apply(var4);
            var2.licenseKeyGenerationInfoReceived(var6);
         } catch (RemoteException var7) {
            if (InvalidCredentialsException.isInvalidCredentialsError(var7)) {
               var2.invalidCredentials(new InvalidCredentialsException(var7));
            } else {
               var2.caught(this.convert(var7));
            }
         } catch (IntegrationServerException var8) {
            var2.caught(new IntegrationServerException("Server exception: " + var8.getMessage(), var8));
         }
      }
   }

   private ServerInfo getServerInfo(WsKeyValue[] var1, LoginCredentials var2) throws IntegrationServerException {
      for (WsKeyValue var6 : var1) {
         if ("server-guid".equals(var6.getKey())) {
            ServerGuid var7 = new ServerGuid(var6.getValue());
            return new ServerInfo(var7, var2.getServer());
         }
      }

      throw new IntegrationServerException("The ETLForge server did not return all necessary information. It did not return its server GUID.");
   }

   private WsLicenseService getService(LoginCredentials var1) throws IntegrationServerException {
      try {
         WsLicenseServiceServiceLocator var2 = new WsLicenseServiceServiceLocator();
         StringBuilder var3 = new StringBuilder();
         JavaWebServiceConfiguration var4 = (JavaWebServiceConfiguration)this.getConfiguration();
         String var5 = var4.replaceJavaPort(var1.getServer());
         if (!this.getConfiguration().configureWebservice(var2, "konga_licenseservice", var5, var3)) {
            return null;
         }

         var2.setkonga_licenseserviceEndpointAddress(var3.toString());
         return var2.getkonga_licenseservice();
      } catch (ServiceException var6) {
         throw new IntegrationServerException("Failed to call web service: " + var6.getMessage(), var6);
      }
   }

   @Override
   public void getLicenseAgreement(String var1, LoginCredentials var2, GetLicenseAgreementCallback var3) {
      if (Thread.currentThread().isInterrupted()) {
         var3.cancelled();
      } else {
         try {
            WsLicenseService var4 = this.getService(var2);
            String var5 = ZipUtils.deflateAndBase64EncodeString(var1);
            WsKeyValue[] var6 = var4.getLicenseAgreement(var2.getUser(), var2.getPasswordHash(), var5);
            String var7 = this.checkError(var6);
            if (StringUtils.isEmpty(var7)) {
               var3.gotLicenseAgreement(this.unpackLicenseAgreement(var6));
            } else {
               var3.licenseError(var7);
            }
         } catch (RemoteException var8) {
            if (InvalidCredentialsException.isInvalidCredentialsError(var8)) {
               var3.invalidCredentials(new InvalidCredentialsException(var8));
            } else {
               var3.caught(this.convert(var8));
            }
         } catch (IntegrationServerException var9) {
            var3.caught(new IntegrationServerException("Server exception: " + var9.getMessage(), var9));
         } catch (Exception var10) {
            var3.caught(var10);
         }
      }
   }

   private String checkError(WsKeyValue[] var1) {
      for (WsKeyValue var5 : var1) {
         if ("error".equals(var5.getKey())) {
            String var6 = var5.getValue();
            if (StringUtils.isNotBlank(var6)) {
               return this.inflateError(var6);
            }
         }
      }

      return null;
   }

   private String inflateError(String var1) {
      try {
         return ZipUtils.base64DecodeAndInflateString(var1);
      } catch (DataFormatException var3) {
         return "The server reported that an error occurred in the license activation process, but the error message was lost in the transfer to the client. Please contact ETLForge support.";
      }
   }

   private String unpackLicenseAgreement(WsKeyValue[] var1) throws Exception {
      for (WsKeyValue var5 : var1) {
         if ("agreement".equals(var5.getKey())) {
            return ZipUtils.base64DecodeAndInflateString(var5.getValue());
         }
      }

      throw new Exception("The ETLForge server did not send a license agreement. This is a bug. Please contact ETLForge support.");
   }

   @Override
   public void activateLicense(String var1, LoginCredentials var2, ActivateLicenseCallback var3) {
      if (Thread.currentThread().isInterrupted()) {
         var3.cancelled();
      } else {
         try {
            WsLicenseService var4 = this.getService(var2);
            String var5 = ZipUtils.deflateAndBase64EncodeString(var1);
            WsKeyValue[] var6 = var4.activateLicense(var2.getUser(), var2.getPasswordHash(), var5);
            String var7 = this.checkError(var6);
            if (StringUtils.isEmpty(var7)) {
               var3.licenseWasActivated(this.getServerInfo(var6, var2));
            } else {
               var3.activationError(var7);
            }
         } catch (RemoteException var8) {
            if (InvalidCredentialsException.isInvalidCredentialsError(var8)) {
               var3.invalidCredentials(new InvalidCredentialsException(var8));
            } else {
               var3.caught(this.convert(var8));
            }
         } catch (IntegrationServerException var9) {
            var3.caught(new IntegrationServerException("Server exception: " + var9.getMessage(), var9));
         } catch (Exception var10) {
            var3.caught(var10);
         }
      }
   }

   @Override
   public void checkLicense(LoginCredentials var1, CheckLicenseCallback var2) {
      try {
         this.authenticator.setKeyValuesHandler(new LicenseServiceWsImpl.LicenseCheckDispatcher(var1, var2));
         this.authenticator.login(var1);
      } catch (InvalidCredentialsException var4) {
         var2.caught(var4);
      } catch (IntegrationServerException var5) {
         var2.caught(var5);
      }
   }

   private static class LicenseCheckDispatcher implements LoginKeyValuesHandler {
      private final LoginCredentials credentials;
      private final CheckLicenseCallback callback;

      public LicenseCheckDispatcher(LoginCredentials var1, CheckLicenseCallback var2) {
         this.credentials = var1;
         this.callback = var2;
      }

      @Override
      public void handle(ServerInfo var1, Map<String, String> var2) throws IntegrationServerException {
         LicenseInfo var3 = new LicenseInfo(var1, var2);
         this.callback.receivedInfo(var3, this.credentials);
      }
   }
}
