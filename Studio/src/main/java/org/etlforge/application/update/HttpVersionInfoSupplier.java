package org.etlforge.application.update;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.io.IOException;
import java.util.concurrent.Callable;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.net.ProxySettings;
import org.etlforge.util.net.ProxyUtils;

public final class HttpVersionInfoSupplier implements Callable<VersionInfo> {
   private final String url;
   private final HttpVersionInfoSupplier.ResponseParser responseParser;
   private volatile Supplier<ProxySettings> proxySettingsSupplier = Suppliers.ofInstance(ProxySettings.NONE);

   public HttpVersionInfoSupplier(String var1, HttpVersionInfoSupplier.ResponseParser var2) {
      this.url = ArgCheck.checkNotBlank(var1);
      this.responseParser = Preconditions.checkNotNull(var2);
   }

   public void setProxySettingsSupplier(Supplier<ProxySettings> var1) {
      this.proxySettingsSupplier = Preconditions.checkNotNull(var1);
   }

   public VersionInfo call() throws Exception {
      DefaultHttpClient var1 = new DefaultHttpClient();

      try {
         this.configureProxy(var1);
         HttpResponse var2 = this.sendRequest(var1);
         return this.parseResponse(var2);
      } finally {
         var1.getConnectionManager().shutdown();
      }
   }

   private void configureProxy(DefaultHttpClient var1) {
      ProxySettings var2 = this.proxySettingsSupplier.get();
      ProxyUtils.configureApacheHttpClient(var1, var2);
   }

   private HttpResponse sendRequest(DefaultHttpClient var1) throws IOException, ClientProtocolException {
      HttpGet var2 = new HttpGet(this.url);
      var2.addHeader("Cache-Control", "no-cache");
      var2.addHeader("Pragma", "no-cache");
      return var1.execute(var2);
   }

   private VersionInfo parseResponse(HttpResponse var1) throws Exception {
      StatusLine var2 = var1.getStatusLine();
      int var3 = var2.getStatusCode();
      if (var3 != 200) {
         throw new Exception("Failed to get information about new versions. Reason: " + var2.getReasonPhrase() + " [" + var3 + "]");
      } else {
         HttpEntity var4 = var1.getEntity();
         if (var4 != null) {
            String var5 = EntityUtils.toString(var4);
            return this.responseParser.parse(var5);
         } else {
            throw new Exception("Unknown error.");
         }
      }
   }

   public interface ResponseParser {
      VersionInfo parse(String var1) throws Exception;
   }
}
