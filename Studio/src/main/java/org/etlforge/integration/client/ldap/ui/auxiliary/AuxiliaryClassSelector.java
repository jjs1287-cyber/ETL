package org.etlforge.integration.client.ldap.ui.auxiliary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.ldap.ui.DefaultClassStructureRetrieverCallback;
import org.etlforge.integration.client.ldap.ui.ObjectClassDefinitionRetriever;
import org.etlforge.integration.client.ldap.ui.ObjectClassStructureRetriever;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetObjectClassesCallback;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.LdapFunctors;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.integration.ldap.structure.ObjectClassType;
import org.etlforge.lang.Arrays2;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;

public final class AuxiliaryClassSelector {
   private final boolean disableReadOnlyAttrs;
   private LdapStructureServerCache ldapCache;

   private AuxiliaryClassSelector(boolean var1) {
      this.disableReadOnlyAttrs = var1;
   }

   public static AuxiliaryClassSelector newAuxClassSelectorForSource() {
      return new AuxiliaryClassSelector(false);
   }

   public static AuxiliaryClassSelector newAuxClassSelectorForTarget() {
      return new AuxiliaryClassSelector(true);
   }

   public void setLdapCache(LdapStructureServerCache var1) {
      this.ldapCache = var1;
   }

   public void select(LdapLocation var1, String var2, NumericOid[] var3, AuxiliaryClassSelectorCallback var4, WaitService var5) {
      AuxiliaryClassSelector.Worker var6 = new AuxiliaryClassSelector.Worker(var1, var2, var3, var4, var5);
      var6.execute();
   }

   private static final class ClassDefinitionsRetriever implements GetObjectClassesCallback {
      private final LdapLocation ldapLoc;
      private final LdapStructureServerCache ldapCache;
      private final AuxiliaryClassSelectorCallback externalCallback;
      private ObjectClassDefinition[] auxClasses;
      private final WaitService waitService;
      private final CountDownLatch latch;

      private ClassDefinitionsRetriever(LdapLocation var1, LdapStructureServerCache var2, AuxiliaryClassSelectorCallback var3, WaitService var4) {
         this.ldapLoc = var1;
         this.ldapCache = var2;
         this.externalCallback = var3;
         this.waitService = var4;
         this.latch = new CountDownLatch(1);
      }

      public static ObjectClassDefinition[] getAuxiliaryClasses(
         LdapLocation var0, LdapStructureServerCache var1, AuxiliaryClassSelectorCallback var2, WaitService var3
      ) {
         AuxiliaryClassSelector.ClassDefinitionsRetriever var4 = new AuxiliaryClassSelector.ClassDefinitionsRetriever(var0, var1, var2, var3);
         var4.submit();
         return var4.getAuxiliaryClasses();
      }

      private void submit() {
         ObjectClassDefinitionRetriever var1 = new ObjectClassDefinitionRetriever();
         var1.setLdapCache(this.ldapCache);
         var1.retrieveClassDefinitions(this.ldapLoc, this, true, this.waitService);
      }

      private ObjectClassDefinition[] getAuxiliaryClasses() {
         try {
            this.latch.await();
            return this.auxClasses;
         } catch (InterruptedException var2) {
            this.externalCallback.failed(var2);
            Thread.currentThread().interrupt();
            return null;
         }
      }

      @Override
      public void objectClassesRetrieved(ObjectClassDefinition[] var1) {
         ArrayList var2 = new ArrayList();

         for (ObjectClassDefinition var6 : var1) {
            if (var6.getType() == ObjectClassType.AUXILIARY) {
               var2.add(var6);
            }
         }

         this.auxClasses = (ObjectClassDefinition[]) var2.toArray(new ObjectClassDefinition[var2.size()]);
         this.latch.countDown();
      }

      @Override
      public void cancelled() {
         this.externalCallback.cancelled();
         this.latch.countDown();
      }

      @Override
      public void caught(Throwable var1) {
         Exception var2 = var1 instanceof Exception ? (Exception)var1 : new Exception(var1);
         this.externalCallback.failed(var2);
         this.latch.countDown();
      }
   }

   private class Worker extends KongaSwingWorker<ObjectClassStructure[], Void> {
      private final LdapLocation ldapLoc;
      private final String destinationClass;
      private final NumericOid[] preSelected;
      private final AuxiliaryClassSelectorCallback callback;
      private final WaitService waitService;

      public Worker(LdapLocation var2, String var3, NumericOid[] var4, AuxiliaryClassSelectorCallback var5, WaitService var6) {
         this.ldapLoc = var2;
         this.destinationClass = var3;
         this.preSelected = var4;
         this.callback = var5;
         this.waitService = var6;
      }

      protected ObjectClassStructure[] doInBackground() throws Exception {
         NumericOid[] var1 = this.getAvailableClasses(this.ldapLoc, this.callback, this.waitService);
         if (var1 != null && var1.length != 0) {
            ObjectClassStructureRetriever var2 = new ObjectClassStructureRetriever();
            var2.setLdapCache(AuxiliaryClassSelector.this.ldapCache);
            DefaultClassStructureRetrieverCallback var3 = new DefaultClassStructureRetrieverCallback();
            var2.retrieveStructures(var1, this.ldapLoc, var3, true, this.waitService);
            ObjectClassStructure[] var4 = var3.getStructures();
            if (var4 == null) {
               this.onIncompleteRequest(var3, this.callback);
            }

            return var4;
         } else {
            return null;
         }
      }

      private NumericOid[] getAvailableClasses(LdapLocation var1, AuxiliaryClassSelectorCallback var2, WaitService var3) {
         ObjectClassDefinition[] var4 = AuxiliaryClassSelector.ClassDefinitionsRetriever.getAuxiliaryClasses(
            var1, AuxiliaryClassSelector.this.ldapCache, var2, var3
         );
         if (var4 != null && var4.length == 0) {
            Alert.info("There are no auxiliary classes available", "No Available Classes");
            var2.cancelled();
         }

         return var4 == null ? null : Arrays2.transform(var4, NumericOid.class, LdapFunctors.CLASS_TO_OID);
      }

      private void onIncompleteRequest(DefaultClassStructureRetrieverCallback var1, AuxiliaryClassSelectorCallback var2) {
         if (var1.wasCancelled()) {
            var2.cancelled();
         } else {
            Throwable var3 = var1.getError();
            if (var3 == null) {
               return;
            }

            Exception var4;
            if (var3 instanceof Exception) {
               var4 = (Exception)var3;
            } else {
               var4 = new Exception(var3);
            }

            var2.failed(var4);
         }
      }

      protected void onSuccess(ObjectClassStructure[] var1) {
         if (var1 != null && var1.length > 0) {
            this.showDialog(this.destinationClass, var1, this.preSelected, this.callback);
         }
      }

      private void showDialog(String var1, ObjectClassStructure[] var2, NumericOid[] var3, AuxiliaryClassSelectorCallback var4) {
         String var5 = "Select Auxiliary Classes for \"" + var1 + "\"";
         AuxiliaryClassSelectionTree var6 = new AuxiliaryClassSelectionTree(var2, AuxiliaryClassSelector.this.disableReadOnlyAttrs, var5);
         var6.setSelectedClasses(var3);
         KongaDialog var7 = new KongaDialog(UiUtils.getActiveWindow(), var5);
         var7.standardLayout(var6, KongaDialog.OK_CANCEL);
         var7.manageLocation(AuxiliaryClassSelector.class);
         var7.setVisible(true);
         if (var7.wasOkPressed()) {
            List var8 = var6.getSelectedStructures();
            var4.completed((ObjectClassStructure[]) var8.toArray(new ObjectClassStructure[var8.size()]));
         } else {
            var4.cancelled();
         }
      }
   }
}
