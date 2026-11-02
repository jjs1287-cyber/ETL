package com.etlforge.integration.client.sfdc.facade;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.ConnectionResult;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.query.QueryRunner;
import org.etlforge.integration.salesforce.wsdl.CachingSObjectSchemaProvider;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;
import org.etlforge.util.name.Nameables;

public final class SalesforceFacade {
   private final SalesforceConnectionFactory connectionFactory;
   private final FacadeConnectionManager connectionManager;
   private final SObjectSchemaProviderFactory schemaProviderFactory;
   private volatile SObjectSchemaProvider schemaProvider;
   private final Map<String, SObjectSchema> flatObjectDefs = Collections.synchronizedMap(new HashMap<>());
   private final Object lock = new Object();

   public SalesforceFacade(SalesforceConnectionFactory var1, SObjectSchemaProviderFactory var2) {
      this.connectionFactory = var1;
      this.connectionManager = new FacadeConnectionManager(var1);
      this.schemaProviderFactory = var2;
   }

   public SalesforceConnectionFactory getConnectionFactory() {
      return this.connectionFactory;
   }

   public SalesforceConnection getConnection() {
      synchronized (this.lock) {
         return this.connectionManager.getConnection();
      }
   }

   public SObjectSchemaProvider getSchemaProvider() {
      synchronized (this.lock) {
         return this.schemaProvider;
      }
   }

   public SalesforceConnection login(SalesforceConnector var1) throws SalesforceException {
      try {
         SalesforceConnection var2;
         SalesforceConnection var3;
         synchronized (this.lock) {
            var2 = this.getConnection();
            var3 = this.connectionManager.login(var1.getOrgIdentifier());
            if (var2 != var3) {
               this.closeSchemaProvider();
               this.flatObjectDefs.clear();
               this.schemaProvider = this.schemaProviderFactory.newProvider(var3);
            }
         }

         if (var2 != var3) {
            this.updateConnectorOnSuccessfulLogin(var1);
         }

         return var3;
      } catch (SalesforceException var7) {
         this.updateConnectorOnFailedLogin(var1, var7);
         throw var7;
      }
   }

   private void closeSchemaProvider() {
      if (this.schemaProvider != null) {
         this.schemaProvider.close();
         this.schemaProvider = null;
      }
   }

   private void updateConnectorOnSuccessfulLogin(SalesforceConnector var1) {
      ConnectionResult var2 = ConnectionResult.success();
      this.updateOrgsConnectionStatus(var1, var2);
   }

   private void updateOrgsConnectionStatus(SalesforceConnector var1, ConnectionResult var2) {
      boolean var3 = var1.updateConnectionStatus(var2);
      if (var3) {
         try {
            var1.save();
         } catch (InterchangeSavingException var5) {
            ErrorLog.log(this.getClass(), "Failed to save the updated connection status for the Salesforce Org " + var1.getName(), var5);
         }
      }
   }

   private void updateConnectorOnFailedLogin(SalesforceConnector var1, SalesforceException var2) {
      ConnectionResult var3 = ConnectionResult.error(var2);
      this.updateOrgsConnectionStatus(var1, var3);
   }

   public List<SObjectSchema> fetchAvailableSObjects() throws SalesforceException {
      SalesforceFacade.SObjectFetcher var1 = new SalesforceFacade.SObjectFetcher();
      return var1.runWithRetry();
   }

   public SObjectSchema lookupObjectDefinition(String var1) {
      return this.flatObjectDefs.get(var1);
   }

   public List<SObjectSchema> refreshObjectList() throws SalesforceException {
      synchronized (this.lock) {
         Preconditions.checkState(this.schemaProvider != null, "Must login first.");
         if (this.schemaProvider instanceof CachingSObjectSchemaProvider) {
            CachingSObjectSchemaProvider var2 = (CachingSObjectSchemaProvider)this.schemaProvider;
            var2.clearCache();
         }

         return this.fetchAvailableSObjects();
      }
   }

   public SObjectSchema refreshFlatObject(String var1) throws SalesforceException {
      SalesforceFacade.SObjectDefinitionRefresher var2 = new SalesforceFacade.SObjectDefinitionRefresher(var1);
      return var2.runWithRetry();
   }

   public QueryRunner createQueryRunner() {
      synchronized (this.lock) {
         SalesforceConnection var2 = this.getConnection();
         if (var2 == null) {
            throw new IllegalStateException("Must login first.");
         }

         QueryRunner var3 = new QueryRunner(this.connectionFactory, var2.getOrgIdentifier());
         var3.useExistingConnection(var2);
         return var3;
      }
   }

   public void close() {
      synchronized (this.lock) {
         this.connectionManager.closeCurrentConnection();
         this.closeSchemaProvider();
      }
   }

   private abstract class Executor<T> {
      private Executor() {
      }

      public final T runWithRetry() throws SalesforceException {
         synchronized (SalesforceFacade.this.lock) {
            Object var10000;
            try {
               var10000 = this.call();
            } catch (SalesforceException var4) {
               if (var4.isInvalidSessionId()) {
                  return this.retry();
               }

               throw var4;
            }

            return (T)var10000;
         }
      }

      protected abstract T call() throws SalesforceException;

      private T retry() throws SalesforceException {
         this.reconnect();
         return this.call();
      }

      private void reconnect() throws SalesforceException {
         SalesforceConnection var1 = SalesforceFacade.this.getConnection();
         OrgIdentifier var2 = var1.getOrgIdentifier();
         SalesforceFacade.this.close();
         SalesforceConnection var3 = SalesforceFacade.this.connectionManager.login(var2);
         SalesforceFacade.this.schemaProvider = SalesforceFacade.this.schemaProviderFactory.newProvider(var3);
      }
   }

   private class SObjectDefinitionRefresher extends SalesforceFacade.Executor<SObjectSchema> {
      private final String objectName;

      public SObjectDefinitionRefresher(String var2) {
         this.objectName = var2;
      }

      protected SObjectSchema call() throws SalesforceException {
         Preconditions.checkState(SalesforceFacade.this.schemaProvider != null, "Must login first.");
         SObjectSchema var1;
         if (SalesforceFacade.this.schemaProvider instanceof CachingSObjectSchemaProvider) {
            var1 = ((CachingSObjectSchemaProvider)SalesforceFacade.this.schemaProvider).refreshObjectDefinition(this.objectName);
         } else {
            var1 = SalesforceFacade.this.schemaProvider.describeObject(this.objectName);
         }

         SalesforceFacade.this.flatObjectDefs.put(this.objectName, var1);
         return var1;
      }
   }

   private class SObjectFetcher extends SalesforceFacade.Executor<List<SObjectSchema>> {
      private SObjectFetcher() {
      }

      protected List<SObjectSchema> call() throws SalesforceException {
         Preconditions.checkState(SalesforceFacade.this.schemaProvider != null, "Must login first.");
         List var1 = SalesforceFacade.this.schemaProvider.listAllObjects();
         this.storeObjectDefinitions(var1);
         return var1;
      }

      private void storeObjectDefinitions(List<SObjectSchema> var1) {
         synchronized (SalesforceFacade.this.flatObjectDefs) {
            SalesforceFacade.this.flatObjectDefs.clear();
            Nameables.addToMap(SalesforceFacade.this.flatObjectDefs, var1);
         }
      }
   }
}
