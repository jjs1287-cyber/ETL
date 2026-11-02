package org.etlforge.integration.server.conf;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.sql.Connection;
import org.etlforge.db.DBConnectionInfo;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.db.ServerDbInfo;
import org.etlforge.integration.server.db.trandb.ProxySettingsSupplier;
import org.etlforge.util.net.ProxySettings;

public final class ServerProxySettings {
   private static final ProxySettings proxySetting = getFromDB();

   public static Supplier<ProxySettings> getSupplier() {
      ProxySettings var0 = get();
      return Suppliers.ofInstance(var0);
   }

   public static ProxySettings get() {
      return proxySetting;
   }

   private static ProxySettings getFromDB() {
      Connection var0 = null;

      try {
         DBConnectionInfo var1 = ServerDbInfo.getTranDbConnectionInfo();
         var0 = var1.getConnection();
         ProxySettingsSupplier var2 = new ProxySettingsSupplier(var0);
         return var2.get();
      } catch (Throwable var8) {
         var8.printStackTrace();
      } finally {
         KongaDbUtils.close(var0);
      }

      return ProxySettings.NONE;
   }
}
