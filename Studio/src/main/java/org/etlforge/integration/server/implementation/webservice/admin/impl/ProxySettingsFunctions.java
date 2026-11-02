package org.etlforge.integration.server.implementation.webservice.admin.impl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import org.etlforge.collections.Pair;
import org.etlforge.integration.server.implementation.webservice.admin.client.WsProxySettings;
import org.etlforge.util.net.Port;
import org.etlforge.util.net.ProxySettings;

final class ProxySettingsFunctions {
   public static Function<ProxySettings, WsProxySettings> toWs() {
      return new ProxySettingsFunctions.ToWs();
   }

   public static Function<WsProxySettings, ProxySettings> fromWs() {
      return new ProxySettingsFunctions.FromWs();
   }

   private ProxySettingsFunctions() {
   }

   private static class FromWs implements Function<WsProxySettings, ProxySettings> {
      private FromWs() {
      }

      public ProxySettings apply(WsProxySettings var1) {
         String var2 = var1.getUserName();
         Pair var3 = ProxySettings.toUserAndDomain(var2);
         ProxySettings.CertificatePolicy var4 = var1.isTrustAllCertificates()
            ? ProxySettings.CertificatePolicy.TRUST_ALL
            : ProxySettings.CertificatePolicy.STRICT;
         return new ProxySettings(var1.getHost(), Port.valueOf(var1.getPort()), (String)var3.first, var1.getPassword(), (String)var3.second, var4);
      }
   }

   private static class ToWs implements Function<ProxySettings, WsProxySettings> {
      private ToWs() {
      }

      public WsProxySettings apply(ProxySettings var1) {
         WsProxySettings var2 = new WsProxySettings();
         String var3 = var1.getHost();
         var2.setHost(var3);
         var2.setEnabled(!Strings.isNullOrEmpty(var3));
         var2.setPort(var1.getPort().getPort());
         var2.setUserName(var1.getDomainQualifiedUser());
         var2.setPassword(var1.getPassword());
         var2.setTrustAllCertificates(var1.getCertPolicy() == ProxySettings.CertificatePolicy.TRUST_ALL);
         return var2;
      }
   }
}
