package com.etlforge.integration.client.deploy.ui;

import com.etlforge.integration.client.deploy.api.SharedItemInfo;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.ui.widget.table.AbstractKongaTableModel;
import org.etlforge.ui.widget.table.TableColumnMetaData;

class SharedItemsInfoTableModel extends AbstractKongaTableModel {
   public static final TableColumnMetaData ITEM = new TableColumnMetaData("Item", IntegrationEntity.class, false, 300);
   public static final TableColumnMetaData PROJECT = new TableColumnMetaData("Project", String.class, false, 200);
   public static final TableColumnMetaData OWNER = new TableColumnMetaData("Project Owner", String.class, false, 125);
   private final IntegrationEntityLookup lookup;

   public SharedItemsInfoTableModel(IntegrationEntityLookup var1) {
      super(ITEM, PROJECT, OWNER);
      this.lookup = var1;
   }

   @Override
   protected Object[] getColumnDataFromObject(Object var1) {
      if (var1 instanceof SharedItemInfo) {
         SharedItemInfo var2 = (SharedItemInfo)var1;
         IntegrationEntity var3 = this.lookup.getEntity(var2.getId());
         if (var3 == null) {
            return null;
         }

         String var4 = var2.getProjectOwnerUserName();
         if (var4 == null) {
            var4 = "[The user no longer exists]";
         }

         return new Object[]{var3, var2.getProjectName(), var4};
      } else {
         this.throwIllegalRowObject(var1, SharedItemInfo.class);
         return null;
      }
   }
}
