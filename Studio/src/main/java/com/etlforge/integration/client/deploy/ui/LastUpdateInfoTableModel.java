package com.etlforge.integration.client.deploy.ui;

import com.etlforge.integration.client.deploy.api.LastUpdateInfo;
import java.util.Date;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

class LastUpdateInfoTableModel extends TypedAbstractKongaTableModel<LastUpdateInfo> {
   public static final TableColumnMetaData ITEM = new TableColumnMetaData("Item", IntegrationEntity.class, false, 300);
   public static final TableColumnMetaData USER = new TableColumnMetaData("User", String.class, false, 150);
   public static final TableColumnMetaData WHEN = new TableColumnMetaData("When", Date.class, false, 150);
   private final IntegrationEntityLookup lookup;

   public LastUpdateInfoTableModel(IntegrationEntityLookup var1) {
      super(LastUpdateInfo.class, ITEM, USER, WHEN);
      this.lookup = var1;
   }

   protected Object[] getColumnDataForTypedObject(LastUpdateInfo var1) {
      IntegrationEntity var2 = this.lookup.getEntity(var1.getId());
      if (var2 == null) {
         return null;
      }

      String var3 = var1.getUserName();
      if (var3 == null) {
         var3 = "[The user no longer exists]";
      }

      return new Object[]{var2, var3, var1.when()};
   }
}
