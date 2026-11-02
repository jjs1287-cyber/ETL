package com.etlforge.integration.salesforce.wsdl;

import java.io.InputStream;
import org.etlforge.application.cache.DataCache;

public interface SalesforceWsdlInfoCache extends DataCache {
   String ID = SalesforceWsdlInfoCache.class.getName();
   SalesforceWsdlInfoCache NONE = new SalesforceWsdlInfoCache() {
      @Override
      public String getID() {
         return ID;
      }

      @Override
      public void storeInfo(String var1, InputStream var2, SalesforceWsdlInfo var3) {
      }

      @Override
      public SalesforceWsdlInfo getCachedInfo(String var1, InputStream var2) {
         return null;
      }
   };

   SalesforceWsdlInfo getCachedInfo(String var1, InputStream var2);

   void storeInfo(String var1, InputStream var2, SalesforceWsdlInfo var3);
}
