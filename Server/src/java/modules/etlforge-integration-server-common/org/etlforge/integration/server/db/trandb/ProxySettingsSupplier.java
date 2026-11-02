package org.etlforge.integration.server.db.trandb;

import com.google.common.base.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.Pair;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.crypto.Crypto;
import org.etlforge.util.net.Port;
import org.etlforge.util.net.ProxySettings;

public final class ProxySettingsSupplier implements Supplier<ProxySettings> {
   private final Connection backendConnection;

   public ProxySettingsSupplier(Connection var1) {
      this.backendConnection = var1;
   }

   private static Boolean getBoolean(String var0, boolean var1) {
      if (var0 == null) {
         return var1;
      } else {
         return !var0.equalsIgnoreCase("true") && !var0.equalsIgnoreCase("1") ? false : true;
      }
   }

   public ProxySettings get() {
      PreparedStatement var1 = null;

      try {
         var1 = this.backendConnection.prepareStatement("SELECT key_value FROM key_values WHERE key_name = ?");
         String var2 = this.getValue(var1, "http_proxy_usage");
         if (isProxyEnabled(var2)) {
            String var3 = this.getValue(var1, "http_proxy_host");
            if (StringUtils.isNotBlank(var3)) {
               String var4 = this.getValue(var1, "http_proxy_port");
               String var5 = this.getValue(var1, "http_proxy_username");
               String var6 = this.getValue(var1, "http_proxy_password");
               String var7 = this.getValue(var1, "http_proxy_trust_all_certs");
               ProxySettings.CertificatePolicy var8 = getBoolean(var7, false)
                  ? ProxySettings.CertificatePolicy.TRUST_ALL
                  : ProxySettings.CertificatePolicy.STRICT;
               return createSettings(var3, var4, var5, var6, var8);
            }
         }
      } catch (SQLException var14) {
         var14.printStackTrace();
      } finally {
         KongaDbUtils.close(var1);
      }

      return ProxySettings.NONE;
   }

   private static boolean isProxyEnabled(String var0) {
      if (StringUtils.isBlank(var0)) {
         return false;
      } else {
         return var0.equals("1") ? true : BooleanUtils.toBoolean(var0);
      }
   }

   private String getValue(PreparedStatement var1, String var2) throws SQLException {
      var1.setString(1, var2);
      ResultSet var3 = var1.executeQuery();

      try {
         return var3.next() ? var3.getString(1) : null;
      } finally {
         var3.close();
      }
   }

   private static ProxySettings createSettings(String var0, String var1, String var2, String var3, ProxySettings.CertificatePolicy var4) {
      Port var5 = createPort(var1);
      String var6 = decryptPassword(var3);
      Pair var7 = ProxySettings.toUserAndDomain(var2);
      return new ProxySettings(var0, var5, (String)var7.first, var6, (String)var7.second, var4);
   }

   private static String decryptPassword(String var0) {
      return StringUtils.isEmpty(var0) ? var0 : Crypto.decryptString(var0);
   }

   private static Port createPort(String var0) {
      return StringUtils.isNotEmpty(var0) ? Port.valueOf(var0) : Port.DEFAULT;
   }
}
