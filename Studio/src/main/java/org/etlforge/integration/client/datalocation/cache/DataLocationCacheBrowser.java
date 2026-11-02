package org.etlforge.integration.client.datalocation.cache;

public interface DataLocationCacheBrowser {
   String getName();

   String[] getCategories();

   String[] getValues(String var1);

   void setValues(String var1, String[] var2);
}
