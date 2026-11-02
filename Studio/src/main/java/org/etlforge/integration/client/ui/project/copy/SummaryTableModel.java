package org.etlforge.integration.client.ui.project.copy;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import org.etlforge.integration.client.project.copy.DeepCopyResult;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

final class SummaryTableModel extends TypedAbstractKongaTableModel<DeepCopyResult.Copy> {
   public static final TableColumnMetaData ORIGINAL = createItemDescriptor("Original");
   public static final TableColumnMetaData COPY = createItemDescriptor("Copy");

   private static TableColumnMetaData createItemDescriptor(String var0) {
      return new TableColumnMetaData(var0, IntegrationEntity.class, false, 250);
   }

   public SummaryTableModel(DeepCopyResult var1) {
      super(DeepCopyResult.Copy.class, ORIGINAL, COPY);

      for (DeepCopyResult.Copy var3 : getSummaryInOrder(var1)) {
         this.addRow(var3);
      }
   }

   private static Iterable<DeepCopyResult.Copy> getSummaryInOrder(DeepCopyResult var0) {
      Ordering var1 = Ordering.from(new EntityTypeAndNameSorter()).onResultOf(new Function<DeepCopyResult.Copy, IntegrationEntity>() {
         public IntegrationEntity apply(DeepCopyResult.Copy var1) {
            return var1.original;
         }
      });
      return var1.sortedCopy(var0.getSummary());
   }

   protected Object[] getColumnDataForTypedObject(DeepCopyResult.Copy var1) {
      return new Object[]{var1.original, var1.copy};
   }
}
