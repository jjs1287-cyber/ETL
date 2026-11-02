package com.etlforge.integration.server.engine.bulkload.rest;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.sql.Connection;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.etlforge.integration.server.db.trandb.ProxySettingsSupplier;
import org.etlforge.util.net.ProxySettings;
import org.etlforge.util.net.ProxyUtils;

public final class HttpClientCreator {
   public static HttpClient newHttpClientDB(Connection var0) {
      ProxySettingsSupplier var1 = new ProxySettingsSupplier(var0);
      return newHttpClient(var1);
   }

   public static HttpClient newHttpClient() {
      return newHttpClient(Suppliers.ofInstance(ProxySettings.NONE));
   }

   public static HttpClient newHttpClient(Supplier<ProxySettings> var0) {
      DefaultHttpClient var1 = new DefaultHttpClient();
      ProxySettings var2 = (ProxySettings)var0.get();
      ProxyUtils.configureApacheHttpClient(var1, var2);
      return var1;
   }

   private HttpClientCreator() {
   }
}
