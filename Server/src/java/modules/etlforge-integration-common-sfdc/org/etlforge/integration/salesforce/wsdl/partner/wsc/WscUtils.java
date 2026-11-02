package org.etlforge.integration.salesforce.wsdl.partner.wsc;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import java.net.Authenticator;
import org.apache.commons.lang.StringUtils;
import org.etlforge.util.net.ProxySettings;
import org.etlforge.util.net.ProxyUtils;
import sun.net.www.protocol.http.AuthCacheImpl;
import sun.net.www.protocol.http.AuthCacheValue;

public final class WscUtils {
   private static ProxySettings lastProxySettings;
   private static boolean tryToClearAuthCache = true;

   public static PartnerConnection createConnectionObjectForEstablishedConnection(String var0, String var1, Supplier<ProxySettings> var2, TraceSettings var3) throws ConnectionException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      ConnectorConfig var4 = new ConnectorConfig();
      var4.setManualLogin(false);
      var4.setServiceEndpoint(var0);
      var4.setSessionId(var1);
      var3.applyTo(var4);
      ProxySettings var5 = (ProxySettings)var2.get();
      applyProxySettings(var4, var5);
      PartnerConnection var6 = new PartnerConnection(var4);
      var6.setCallOptions("ETLForge/ETLForge/", null);
      return var6;
   }

   public static void applyProxySettings(ConnectorConfig var0, ProxySettings var1) {
      if (!StringUtils.isBlank(var1.getHost())) {
         var0.setProxy(var1.getHost(), var1.getPort().getPort());
         if (var1.hasUsername()) {
            var0.setProxyUsername(var1.getUser());
            var0.setProxyPassword(var1.getPassword());
            tryToClearAuthCache(var1);
            Authenticator.setDefault(var1.getAuthenticator());
         }

         ProxyUtils.updateNtlmDomainSystemProperty(var1);
      }
   }

   private static synchronized void tryToClearAuthCache(ProxySettings var0) {
      if (tryToClearAuthCache && lastProxySettings != null && !lastProxySettings.equals(var0)) {
         try {
            AuthCacheValue.setAuthCache(new AuthCacheImpl());
         } catch (Exception var2) {
            tryToClearAuthCache = false;
            var2.printStackTrace();
         }
      }

      lastProxySettings = var0;
   }

   private WscUtils() {
   }
}
