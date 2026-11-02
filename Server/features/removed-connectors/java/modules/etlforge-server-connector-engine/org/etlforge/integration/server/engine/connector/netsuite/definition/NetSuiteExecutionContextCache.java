package org.etlforge.integration.server.engine.connector.netsuite.definition;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteGetDataCenterUrls;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteExecutionContextCache {
   private final Cache<String, String> urlCache = CacheBuilder.newBuilder().maximumSize(1000L).build();
   private final Cache<String, NetSuiteDefinitionExecution> executionCache;
   private final Cache<String, NetSuiteDefinitionDesign> designCache;

   public NetSuiteExecutionContextCache(int var1, int var2) {
      this.executionCache = CacheBuilder.newBuilder().maximumSize(var1).build();
      this.designCache = CacheBuilder.newBuilder().maximumSize(var2).build();
   }

   String getDataCenterUrlImpl(final NetSuitePassport var1) throws ExecutionException {
      return this.urlCache.get(var1.getAccountKey(), new Callable<String>() {
         public String call() throws Exception {
            return NetSuiteGetDataCenterUrls.getWebservicesUrl(var1);
         }
      });
   }

   NetSuiteDefinitionExecution getExecutionImpl(final ProxySettings var1, final String var2) throws ExecutionException {
      return this.executionCache.get(var2, new Callable<NetSuiteDefinitionExecution>() {
         public NetSuiteDefinitionExecution call() throws Exception {
            return NetSuiteDefinitionFactory.createDefinitionExecution(var1, var2);
         }
      });
   }

   NetSuiteDefinitionDesign getDesignImpl(final ProxySettings var1, final String var2) throws ExecutionException {
      return this.designCache.get(var2, new Callable<NetSuiteDefinitionDesign>() {
         public NetSuiteDefinitionDesign call() throws Exception {
            return NetSuiteDefinitionFactory.createNetSuiteDefinitionDesign(var1, var2);
         }
      });
   }
}
