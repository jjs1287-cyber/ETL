package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlException;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfo;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfoCache;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlParser;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.salesforce.config.SalesforceConfig;

public final class WsdlFileParser {
   private final SalesforceConfig config;
   private final Function<String, InputStream> localWsdlFileStore;
   private SalesforceWsdlInfoCache cache = SalesforceWsdlInfoCache.NONE;

   public WsdlFileParser(SalesforceConfig var1, Function<String, InputStream> var2) {
      this.config = Preconditions.checkNotNull(var1);
      this.localWsdlFileStore = Preconditions.checkNotNull(var2);
   }

   public void setCache(SalesforceWsdlInfoCache var1) {
      Preconditions.checkNotNull(var1);
      this.cache = var1;
   }

   public SalesforceWsdlInfo parse(WsdlFile var1) throws SalesforceWsdlException {
      SalesforceWsdlInfo var2 = this.lookupCachedInfo(var1);
      if (var2 != null) {
         return var2;
      }

      SalesforceWsdlParser var3 = this.createWsdlParser(var1);
      var2 = var3.getParsedInfo();
      var2.setWsdlLocator(var1.getLocator());
      this.storeInCache(var1, var2);
      return var2;
   }

   private SalesforceWsdlInfo lookupCachedInfo(WsdlFile var1) {
      if (var1.isRemote()) {
         return null;
      }

      InputStream var2 = this.localWsdlFileStore.apply(var1.getLocator());
      return this.cache.getCachedInfo(var1.getLocator(), var2);
   }

   private SalesforceWsdlParser createWsdlParser(WsdlFile var1) throws SalesforceWsdlException {
      try {
         String var2 = var1.getLocator();
         if (var1.isRemote()) {
            return new SalesforceWsdlParser(new URL(var2), this.config);
         }

         InputStream var3 = this.localWsdlFileStore.apply(var2);
         return new SalesforceWsdlParser(var3, this.config);
      } catch (MalformedURLException var4) {
         throw new SalesforceWsdlException(var4);
      }
   }

   private void storeInCache(WsdlFile var1, SalesforceWsdlInfo var2) {
      if (!var1.isRemote()) {
         InputStream var3 = this.localWsdlFileStore.apply(var1.getLocator());
         this.cache.storeInfo(var1.getLocator(), var3, var2);
      }
   }
}
