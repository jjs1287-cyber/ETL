package org.etlforge.integration.client.datalocation.cache;

public interface LoginNameCache {
   String[] getLoginNames();

   boolean removeLoginName(String var1);
}
