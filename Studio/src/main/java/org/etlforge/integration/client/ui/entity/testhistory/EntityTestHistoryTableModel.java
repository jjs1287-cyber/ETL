package org.etlforge.integration.client.ui.entity.testhistory;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.test.EntityTestResult;
import org.etlforge.integration.data.test.TestResult;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class EntityTestHistoryTableModel extends TypedAbstractKongaTableModel<EntityTestResult> {
   private static final TableColumnMetaData ENTITY = new TableColumnMetaData("Name", IntegrationEntity.class, false, 250);
   private IntegrationEntityLookup entityLookup;
   private final boolean includeDate;

   public EntityTestHistoryTableModel() {
      this(true);
   }

   public EntityTestHistoryTableModel(boolean var1) {
      super(EntityTestResult.class, createColumnDescriptors(var1));
      this.includeDate = var1;
   }

   private static TableColumnMetaData[] createColumnDescriptors(boolean var0) {
      ArrayList var1 = Lists.newArrayList(ENTITY);
      if (var0) {
         var1.add(new TableColumnMetaData("Date", DateCell.class, false, 150));
      }

      Collections.addAll(
         var1, new TableColumnMetaData("Result", TestResult.Outcome.class, false, 80), new TableColumnMetaData("Details", String.class, false, 300)
      );
      return (TableColumnMetaData[]) var1.toArray(new TableColumnMetaData[var1.size()]);
   }

   public void setEntityLookup(IntegrationEntityLookup var1) {
      this.entityLookup = var1;
   }

   public int getEntityColumnIndex() {
      return this.indexOfColumn(ENTITY);
   }

   protected Object[] getColumnDataForTypedObject(EntityTestResult var1) {
      IntegrationEntity var2 = this.entityLookup.getEntity(var1.getSubjectId());
      if (var2 == null) {
         return null;
      } else {
         return this.includeDate
            ? new Object[]{var2, new DateCell(new Date(var1.getTimestamp())), var1.getOutcome(), var1.getDetails()}
            : new Object[]{var2, var1.getOutcome(), var1.getDetails()};
      }
   }
}
