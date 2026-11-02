package org.etlforge.integration.ldap.client;

import org.etlforge.application.Application;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetLdapStructuresCallback;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetObjectClassesCallback;
import org.etlforge.integration.client.mapping.serverapi.ldap.LdapClassSearchParameters;
import org.etlforge.integration.client.mapping.serverapi.ldap.LdapInfoProvider;
import org.etlforge.integration.client.mapping.serverapi.ldap.LdapStructureSearchParameters;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.ldap.LdapConnectionParameters;
import org.etlforge.integration.ldap.client.cache.LdapLocationStructureCacheAccessor;
import org.etlforge.integration.ldap.client.cache.LdapStructureCache;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;

public final class LdapInfoProviderService {
   private final LdapInfoProvider provider;

   public LdapInfoProviderService() {
      IntegrationServer var1 = IntegrationServer.getInstance();
      this.provider = var1.getServerCall(LdapInfoProvider.class);
   }

   public void getObjectClassDefinitions(LdapLocation var1, GetObjectClassesCallback var2, boolean var3) {
      Runnable var4 = this.createGetClassDefinitionsTask(var1, var2);
      if (var3) {
         var4.run();
      } else {
         Application.getWorker().submitForParallel(var4);
      }
   }

   public void getStructures(LdapLocation var1, NumericOid[] var2, GetLdapStructuresCallback var3, boolean var4) {
      Runnable var5 = this.createGetStructuresTask(var1, var2, var3);
      if (var4) {
         var5.run();
      } else {
         Application.getWorker().submitForParallel(var5);
      }
   }

   private static IntegrationEntityId getSourceOrTargetIdImpl(LdapLocation var0) {
      IntegrationEntityId var1 = var0.getSourceId();
      if (var1 == null) {
         var1 = var0.getTargetId();
      }

      return var1;
   }

   private Runnable createGetClassDefinitionsTask(final LdapLocation var1, final GetObjectClassesCallback var2) {
      final LdapClassSearchParameters var3 = new LdapClassSearchParameters() {
         @Override
         public IntegrationEntityId getSourceOrTargetId() {
            return LdapInfoProviderService.getSourceOrTargetIdImpl(var1);
         }

         @Override
         public LdapConnectionParameters getConnectionParameters() {
            return var1.getConnectionParameters();
         }

         @Override
         public String getSearchString() {
            return "";
         }

         @Override
         public boolean skipAbstractClasses() {
            return true;
         }

         @Override
         public boolean skipObsoleteClasses() {
            return true;
         }
      };
      return new Runnable() {
         @Override
         public void run() {
            LdapInfoProviderService.GetObjectClassesCallbackImpl var1x = new LdapInfoProviderService.GetObjectClassesCallbackImpl(var1, var2);

            try {
               LdapInfoProviderService.this.provider.getObjectClasses(var3, var1x);
            } catch (Exception var3x) {
               var1x.caught(var3x);
            }
         }
      };
   }

   private Runnable createGetStructuresTask(final LdapLocation var1, final NumericOid[] var2, final GetLdapStructuresCallback var3) {
      final LdapStructureSearchParameters var4 = new LdapStructureSearchParameters() {
         @Override
         public IntegrationEntityId getSourceOrTargetId() {
            return LdapInfoProviderService.getSourceOrTargetIdImpl(var1);
         }

         @Override
         public LdapConnectionParameters getConnectionParameters() {
            return var1.getConnectionParameters();
         }

         @Override
         public NumericOid[] getObjectClasses() {
            return var2;
         }

         @Override
         public boolean skipObsoleteAttributes() {
            return true;
         }
      };
      return new Runnable() {
         @Override
         public void run() {
            LdapInfoProviderService.GetLdapStructuresCallbackImpl var1x = new LdapInfoProviderService.GetLdapStructuresCallbackImpl(var1, var3);

            try {
               LdapInfoProviderService.this.provider.getLdapStructures(var4, var1x);
            } catch (Exception var3x) {
               var1x.caught(var3x);
            }
         }
      };
   }

   private static final class GetLdapStructuresCallbackImpl implements GetLdapStructuresCallback {
      private final LdapLocation ldapLoc;
      private final GetLdapStructuresCallback externalCallback;

      public GetLdapStructuresCallbackImpl(LdapLocation var1, GetLdapStructuresCallback var2) {
         this.ldapLoc = var1;
         this.externalCallback = var2;
      }

      @Override
      public void structuresRetrieved(ObjectClassStructure[] var1) {
         try {
            this.externalCallback.structuresRetrieved(var1);
         } finally {
            this.updateCache(var1);
         }
      }

      @Override
      public void cancelled() {
         this.externalCallback.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         this.externalCallback.caught(var1);
      }

      private void updateCache(ObjectClassStructure[] var1) {
         LdapStructureCache var2 = LdapLocationStructureCacheAccessor.getCache(this.ldapLoc);
         var2.storeClassStructures(var1);
      }
   }

   private static final class GetObjectClassesCallbackImpl implements GetObjectClassesCallback {
      private final LdapLocation ldapLoc;
      private final GetObjectClassesCallback externalCallback;

      public GetObjectClassesCallbackImpl(LdapLocation var1, GetObjectClassesCallback var2) {
         this.ldapLoc = var1;
         this.externalCallback = var2;
      }

      @Override
      public void objectClassesRetrieved(ObjectClassDefinition[] var1) {
         try {
            this.externalCallback.objectClassesRetrieved(var1);
         } finally {
            this.updateCache(var1);
         }
      }

      @Override
      public void cancelled() {
         this.externalCallback.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         this.externalCallback.caught(var1);
      }

      private void updateCache(ObjectClassDefinition[] var1) {
         LdapStructureCache var2 = LdapLocationStructureCacheAccessor.getCache(this.ldapLoc);
         var2.storeObjectClasses(var1);
      }
   }
}
