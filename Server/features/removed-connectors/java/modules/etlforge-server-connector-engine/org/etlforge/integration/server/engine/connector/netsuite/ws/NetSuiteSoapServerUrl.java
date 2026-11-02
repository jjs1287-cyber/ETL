package org.etlforge.integration.server.engine.connector.netsuite.ws;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteSoapServerUrl {
   private final String wsdlUrl;
   private final Cache<String, String> urlCache;

   private NetSuiteSoapServerUrl(String var1) {
      this.wsdlUrl = var1;
      this.urlCache = CacheBuilder.newBuilder().maximumSize(1000L).build();
   }

   private String getWsdlUrl() {
      return this.wsdlUrl;
   }

   private String getSoapServerUrl(ProxySettings var1, String var2) {
      try {
         Object var3 = this.getSoapServerUrlImpl(var1, var2);
         if (var3 != null) {
            return var3.toString();
         } else {
            throw new ConnectorWebServiceRuntimeException("can not get netsuite url for account " + var2);
         }
      } catch (ExecutionException var4) {
         throw new ConnectorWebServiceRuntimeException(var4);
      }
   }

   private Object getSoapServerUrlImpl(ProxySettings var1, String var2) throws ExecutionException {
      return this.urlCache.get(var2, new Callable<String>() {
         public String call() throws Exception {
            return "";
         }
      });
   }
}
