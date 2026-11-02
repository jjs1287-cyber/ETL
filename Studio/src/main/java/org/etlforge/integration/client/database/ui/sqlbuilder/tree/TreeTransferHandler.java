package org.etlforge.integration.client.database.ui.sqlbuilder.tree;

import com.google.common.collect.Lists;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import org.jdesktop.swingx.JXTreeTable;
import org.etlforge.integration.client.database.ui.dnd.TransferableDatabaseColumns;
import org.etlforge.integration.client.database.ui.dnd.TransferableDatabaseObjects;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

final class TreeTransferHandler extends TransferHandler {
   @Override
   public boolean canImport(TransferSupport var1) {
      return false;
   }

   @Override
   protected Transferable createTransferable(JComponent var1) {
      JXTreeTable var2 = (JXTreeTable)var1;
      DatabaseObject var3 = null;
      ArrayList var4 = null;

      for (int var8 : var2.getSelectedRows()) {
         Object var9 = var2.getValueAt(var8, 0);
         if (var9 instanceof DatabaseObject) {
            if (var3 != null) {
               return null;
            }

            var3 = (DatabaseObject)var9;
         } else {
            if (!(var9 instanceof DatabaseColumn)) {
               return null;
            }

            if (var3 != null) {
               return null;
            }

            DatabaseColumn var10 = (DatabaseColumn)var9;
            if (var4 == null) {
               var4 = Lists.newArrayList();
            }

            for (Object __etlItem50 : var4) {
               DatabaseColumn var12 = (DatabaseColumn) __etlItem50;
               if (var12.getDatabaseObject() != var10.getDatabaseObject()) {
                  return null;
               }
            }

            var4.add(var10);
         }
      }

      if (var3 != null) {
         return new TransferableDatabaseObjects(Collections.singletonList(var3));
      } else {
         return var4 != null ? new TransferableDatabaseColumns(var4) : null;
      }
   }

   @Override
   public int getSourceActions(JComponent var1) {
      return 1;
   }
}
