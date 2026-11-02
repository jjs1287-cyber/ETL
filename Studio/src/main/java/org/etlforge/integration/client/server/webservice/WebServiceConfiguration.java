package org.etlforge.integration.client.server.webservice;

import java.net.ConnectException;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.ServerPorts;
import org.etlforge.integration.client.server.login.LoginConfiguration;
import org.etlforge.integration.client.server.prefs.ServerCallTimeoutPreference;
import org.etlforge.integration.common.server.ServerErrors;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.system.callback.CallbackResultAdapter;

public abstract class WebServiceConfiguration {
   private final ServerManager serverManager;
   private final ServerPorts serverPorts;
   private final WebServiceConfigurationCallback configCallback;
   private final String wsPath;
   private final String etlforgeServiceName;
   private final LoginConfiguration loginConfiguration;

   public WebServiceConfiguration(String var1, String var2, ServerManager var3, ServerPorts var4, WebServiceConfigurationCallback var5) {
      this.wsPath = var1;
      this.etlforgeServiceName = var2;
      this.serverManager = var3;
      this.serverPorts = var4;
      this.configCallback = var5;
      this.loginConfiguration = var3.getLoginService().getConfiguration();
   }

   public final String getWsPath() {
      return this.wsPath;
   }

   public final String getEtlforgeService() {
      return this.etlforgeServiceName;
   }

   public final ServerManager getServerManager() {
      return this.serverManager;
   }

   public final ServerPorts getServerPorts() {
      return this.serverPorts;
   }

   public void setTimeOut(Stub var1) {
      var1.setTimeout((int)TimeUnit.MINUTES.toMillis(ServerCallTimeoutPreference.inMinutes()));
   }

   public boolean configureWebservice(Service var1, String var2, StringBuilder var3, ServerCallback var4) {
      final ServerCallback var5 = var4;
      CallbackResultAdapter var6 = new CallbackResultAdapter() {
         @Override
         public void cancelled(Object var1) {
            var5.cancelled();
         }

         @Override
         public void failed(Object var1) {
            var5.cancelled();
         }
      };
      if (!this.configCallback.assureThatWeHaveLoggedIn(var6)) {
         return false;
      } else {
         String var7 = this.getServerName();
         if (var7 != null && var7.length() != 0) {
            this.configureWebservice(var1, var2, var7, var3);
            return true;
         } else {
            throw new IllegalStateException("Must have logged in to server before using web services to connect to it.");
         }
      }
   }

   public final boolean configureWebservice(Service var1, String var2, String var3, StringBuilder var4) {
      var1.getEngine().setOption("sendMultiRefs", Boolean.FALSE);
      if (this.isUsingHttps()) {
         var4.append("https://");
      } else {
         var4.append("http://");
      }

      var4.append(var3);
      var4.append(this.getWsPath());
      var4.append(var2);
      return true;
   }

   public final ServerGuid getServerId() {
      return this.loginConfiguration.getServerId();
   }

   public final String getUserName() {
      return this.loginConfiguration.getUserName();
   }

   public final String getPassword() {
      return this.loginConfiguration.getPassword();
   }

   public final String getPasswordHash() {
      return this.loginConfiguration.getPasswordHash();
   }

   public String getServerName() {
      return this.loginConfiguration.getServerName();
   }

   public final ServerInfo getServerInfo() {
      return new ServerInfo(this.getServerId(), this.getServerName());
   }

   public final boolean isUsingHttps() {
      return this.loginConfiguration.isUsingHttps();
   }

   public final IntegrationServerException convertToIntegrationServerException(RemoteException var1) {
      Throwable var2 = var1.getCause();
      String var3 = "";
      if (var2 instanceof ConnectException) {
         String var4 = this.getServerName();
         var3 = "Failed to connect to the ETLForge server";
         if (var4 != null) {
            var3 = var3 + " " + var4;
         }

         var3 = var3
            + ". If you are not logged in, please login first. If you are logged in, this may indicate that the server is having a problem. Please make sure that the "
            + this.getEtlforgeService()
            + " is running. Also check the server log files.";
      } else {
         var3 = this.getServerErrorMessage(var1);
      }

      while (var2 != null) {
         System.out.println(var2.getClass());
         var2 = var2.getCause();
      }

      return new IntegrationServerException(var3, var1);
   }

   protected String getServerErrorMessage(RemoteException var1) {
      String var2 = this.removeExceptionClassFromMessage(var1.getMessage());
      var2 = var2 + ServerErrors.getServerMethodForErrorMessage(var1);
      var2 = var2 + ServerErrors.getCausedByCascade(var1);
      return StringUtils.substringBefore(var2, "Cause stacktrace: ");
   }

   private String removeExceptionClassFromMessage(String var1) {
      return var1 != null && var1.startsWith("org.etlforge.") ? StringUtils.substringAfter(var1, ": ") : var1;
   }

   public String throwException(String var1, Exception var2) throws IntegrationServerException {
      String var3 = patchForBugJBClient290(var2.getMessage());
      throw new IntegrationServerException(var3 + "\r\n\r\nThis error occurred when calling " + var1, var2);
   }

   private static String patchForBugJBClient290(String var0) {
      if (var0 == null) {
         return "";
      }

      String var1 = "; nested exception is:";
      if (var0.startsWith(var1)) {
         var0 = var0.substring(var1.length()).trim();
      }

      return var0;
   }

   private static void installTrustManager() {
      TrustManager[] var0 = new TrustManager[]{new X509TrustManager() {
         @Override
         public X509Certificate[] getAcceptedIssuers() {
            return null;
         }

         @Override
         public void checkClientTrusted(X509Certificate[] var1, String var2) {
         }

         @Override
         public void checkServerTrusted(X509Certificate[] var1, String var2) {
         }
      }};

      try {
         SSLContext var1 = SSLContext.getInstance("SSL");
         HostnameVerifier var2 = new HostnameVerifier() {
            @Override
            public boolean verify(String var1, SSLSession var2x) {
               return true;
            }
         };
         var1.init(null, var0, new SecureRandom());
         HttpsURLConnection.setDefaultSSLSocketFactory(var1.getSocketFactory());
         HttpsURLConnection.setDefaultHostnameVerifier(var2);
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   static {
      installTrustManager();
      Logger.getRootLogger().addAppender(new WebServiceAppender());
   }
}
