package com.etlforge.integration.client.ui.sfdc;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.interchange.entity.DefaultEntityRenamer;
import org.etlforge.integration.client.ui.interchange.entity.EntityRenamer;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;

public final class SalesforceWizardRenamer implements EntityRenamer {
   private final EntityPersistor persistor;
   private String newWizardName;
   private String oldWizardName;
   private Map<IntegrationEntity, String> rollbackData;
   private SalesforceWizardEntity wizard;

   public SalesforceWizardRenamer(EntityPersistor var1) {
      this.persistor = var1;
   }

   public void setOldWizardName(String var1) {
      this.oldWizardName = var1;
   }

   @Override
   public boolean setNewName(IntegrationEntity var1, String var2) {
      this.wizard = (SalesforceWizardEntity)var1;
      boolean var3 = true;
      this.prepareRename(var2);
      if (this.rename(var1, var2)) {
         boolean var4 = this.renameImplementingItems();
         if (!var4) {
            this.rollback();
            var3 = false;
         }
      }

      this.oldWizardName = null;
      return var3;
   }

   private void prepareRename(String var1) {
      this.newWizardName = var1;
      if (this.oldWizardName == null) {
         this.oldWizardName = this.wizard.getName();
      }

      this.rollbackData = Maps.newHashMap();
   }

   private boolean rename(IntegrationEntity var1, String var2) {
      this.addRollbackData(var1);
      DefaultEntityRenamer var3 = new DefaultEntityRenamer(this.persistor);
      return var3.setNewName(var1, var2);
   }

   private void addRollbackData(IntegrationEntity var1) {
      this.rollbackData.put(var1, var1.getName());
   }

   private boolean renameImplementingItems() {
      WizardItemBrowser var1 = new WizardItemBrowser(this.wizard);

      for (IntegrationEntity var3 : var1.getItemsToRename()) {
         boolean var4 = this.renameImplementingItem(var3);
         if (!var4) {
            return false;
         }
      }

      return true;
   }

   private boolean renameImplementingItem(IntegrationEntity var1) {
      String var2 = this.getNewNameForImplementingItem(var1);
      return var2.equals(var1.getName()) ? true : this.rename(var1, var2);
   }

   private String getNewNameForImplementingItem(IntegrationEntity var1) {
      String var2 = getSuggestedNameForImplementingItem(var1, this.oldWizardName, this.newWizardName);
      if (var2.equals(var1.getName())) {
         return var2;
      }

      String var3 = var2;
      IntegrationEntity var4 = var1.getParent();
      if (var4 instanceof Folder) {
         var3 = EntityUtils.getAvailableChildName((Folder)var4, var2);
      }

      return var3;
   }

   static String getSuggestedNameForImplementingItem(IntegrationEntity var0, String var1, String var2) {
      String var3 = var0.getName();
      int var4 = var3.indexOf(var1);
      if (var4 == -1) {
         return var3;
      }

      String var5 = var3.substring(0, var4) + var2;
      int var6 = var4 + var1.length();
      if (var6 < var3.length()) {
         var5 = var5 + var3.substring(var6);
      }

      return var5;
   }

   private void rollback() {
      for (Entry var2 : this.rollbackData.entrySet()) {
         this.rename((IntegrationEntity)var2.getKey(), (String)var2.getValue());
      }
   }
}
