package org.etlforge.integration.server.engine.connector.netsuite.definition;

import java.util.concurrent.ExecutionException;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteExecutionContext {
   public static NetSuiteExecutionContext INSTANCE = new NetSuiteExecutionContext();
   private final NetSuiteExecutionContextCache cache;

   public NetSuiteExecutionContext() {
      int var1 = NetSuiteConfigInstance.getNetSuiteConfig().getExecutionCacheSize();
      int var2 = NetSuiteConfigInstance.getNetSuiteConfig().getDesignCacheSize();
      this.cache = new NetSuiteExecutionContextCache(var1, var2);
   }

   public String getDataCenterUrl(NetSuitePassport var1) {
      try {
         return this.cache.getDataCenterUrlImpl(var1);
      } catch (ExecutionException var3) {
         var3.printStackTrace();
         throw new ConnectorWebServiceRuntimeException(var3);
      }
   }

   public NetSuiteDefinitionExecution getExecution(NetSuitePassport var1) {
      return this.getExecution(var1.getProxySettings(), var1.getNetsuiteWsdlDownloadUrl());
   }

   public NetSuiteDefinitionDesign getDesign(NetSuitePassport var1) {
      return this.getDesign(var1.getProxySettings(), var1.getNetsuiteWsdlDownloadUrl());
   }

   public NetSuiteDefinitionExecution getExecution(ProxySettings var1, String var2) {
      try {
         return this.cache.getExecutionImpl(var1, var2);
      } catch (ExecutionException var4) {
         var4.printStackTrace();
         throw new ConnectorWebServiceRuntimeException(var4);
      }
   }

   public NetSuiteDefinitionDesign getDesign(ProxySettings var1, String var2) {
      try {
         return this.cache.getDesignImpl(var1, var2);
      } catch (ExecutionException var4) {
         var4.printStackTrace();
         throw new ConnectorWebServiceRuntimeException(var4);
      }
   }
}
