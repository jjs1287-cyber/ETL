package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.etlforge.application.appdir.CacheDirectory;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.SalesforceVersion;
import org.etlforge.integration.salesforce.wsdl.CachingSObjectSchemaProvider;
import org.etlforge.integration.salesforce.wsdl.FileCacheErrorReporter;
import org.etlforge.integration.salesforce.wsdl.FileCachingSObjectSchemaProvider;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;
import org.etlforge.io.Folder;
import org.etlforge.util.crypto.DigestUtils;

public final class ClientSObjectSchemaProviderFactory implements SObjectSchemaProviderFactory {
   private final Folder cacheRoot;
   private final SObjectSchemaProviderFactory sfdcFactory;
   private final Map<ClientSObjectSchemaProviderFactory.CacheKey, ClientSObjectSchemaProviderFactory.Cache> currentCaches = Maps.newHashMap();
   private final Object lock = new Object();

   public ClientSObjectSchemaProviderFactory(CacheDirectory var1, SObjectSchemaProviderFactory var2) {
      this.cacheRoot = new Folder(var1.getLocation(), "SalesforceMetadata");
      this.sfdcFactory = Preconditions.checkNotNull(var2);
   }

   @Override
   public SObjectSchemaProvider newProvider(SalesforceConnection var1) {
      Preconditions.checkNotNull(var1);
      ClientSObjectSchemaProviderFactory.CacheKey var2 = this.createCacheKey(var1);
      synchronized (this.lock) {
         ClientSObjectSchemaProviderFactory.Cache var4 = this.currentCaches.get(var2);
         if (var4 == null) {
            var4 = this.createNewCache(var1, var2);
            this.currentCaches.put(var2, var4);
         }

         return var4;
      }
   }

   private ClientSObjectSchemaProviderFactory.CacheKey createCacheKey(SalesforceConnection var1) {
      return new ClientSObjectSchemaProviderFactory.CacheKey(var1.getOrgIdentifier());
   }

   private ClientSObjectSchemaProviderFactory.Cache createNewCache(SalesforceConnection var1, ClientSObjectSchemaProviderFactory.CacheKey var2) {
      OrgIdentifier var3 = var1.getOrgIdentifier();
      SalesforceVersion var4 = var3.getVersion();
      Folder var5 = this.cacheRoot.subFolder(var2.label).subFolder(var4.getId());
      return new ClientSObjectSchemaProviderFactory.Cache(var2, var5, this.sfdcFactory.newProvider(var1));
   }

   private class Cache implements CachingSObjectSchemaProvider {
      private final ClientSObjectSchemaProviderFactory.CacheKey key;
      private final FileCachingSObjectSchemaProvider impl;

      public Cache(ClientSObjectSchemaProviderFactory.CacheKey var2, Folder var3, SObjectSchemaProvider var4) {
         this.key = var2;
         boolean var5 = false;
         this.impl = new FileCachingSObjectSchemaProvider(var4, var3, var5, new ClientSObjectSchemaProviderFactory.ErrorReporter());
      }

      @Override
      public List<SObjectSchema> listAllObjects() throws SalesforceException {
         return this.impl.listAllObjects();
      }

      @Override
      public SObjectSchema describeObject(String var1) throws SalesforceException {
         return this.impl.describeObject(var1);
      }

      @Override
      public List<SObjectSchema> describeObjects(List<String> var1) throws SalesforceException {
         return this.impl.describeObjects(var1);
      }

      @Override
      public SObjectSchema refreshObjectDefinition(String var1) throws SalesforceException {
         return this.impl.refreshObjectDefinition(var1);
      }

      @Override
      public void clearCache() {
         this.impl.clearCache();
      }

      @Override
      public void close() {
         this.impl.close();
         synchronized (ClientSObjectSchemaProviderFactory.this.lock) {
            ClientSObjectSchemaProviderFactory.this.currentCaches.remove(this.key);
         }
      }
   }

   private static class CacheKey {
      public final String label;

      public CacheKey(OrgIdentifier var1) {
         String var2 = var1.getUserName();
         String var3 = var1.getLoginUrl();
         this.label = DigestUtils.getMd5DigestAsHexString(var2 + "|" + var3);
      }

      @Override
      public int hashCode() {
         return this.label.hashCode();
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            return var1 instanceof ClientSObjectSchemaProviderFactory.CacheKey
               ? this.label.equals(((ClientSObjectSchemaProviderFactory.CacheKey)var1).label)
               : false;
         }
      }
   }

   private static class ErrorReporter implements FileCacheErrorReporter {
      private ErrorReporter() {
      }

      @Override
      public void report(Exception var1, String var2) {
         ErrorLog.internal(FileCachingSObjectSchemaProvider.class, var2, var1);
      }
   }
}
