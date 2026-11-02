package com.etlforge.integration.client.ui.sfdc.soql.v4;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.sfdc.soql.FieldTransferable;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import org.etlforge.integration.salesforce.SObject;

final class FieldListTransferHandler extends TransferHandler {
   private final SObject object;

   public FieldListTransferHandler(SObject var1) {
      this.object = var1;
   }

   @Override
   protected final Transferable createTransferable(JComponent var1) {
      List var2 = this.getSelectedFields((JList)var1);
      if (var2.isEmpty()) {
         return null;
      }

      DraggedFields var3 = new DraggedFields(this.object, var2);
      return new FieldTransferable(var3);
   }

   private List<SObject.Field> getSelectedFields(JList var1) {
      ArrayList var2 = Lists.newArrayList();

      for (Object var6 : var1.getSelectedValues()) {
         var2.add((SObject.Field)var6);
      }

      return var2;
   }

   @Override
   public int getSourceActions(JComponent var1) {
      return 1;
   }
}
