package com.etlforge.integration.client.ui.script.fcn.db.sql;

import com.google.common.collect.Lists;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.database.ui.dnd.TransferableDatabaseColumns;
import org.etlforge.integration.client.database.ui.dnd.TransferableDatabaseObjects;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.dataelement.DataElementTemplate;
import org.etlforge.integration.dataelement.ui.DeTemplateDataFlavor;
import org.etlforge.ui.dnd.DelegatingTransferHandler;

final class SqlStringAreaTransferHandler extends DelegatingTransferHandler {
   private final SqlStringArea sqlArea;
   private final DeTemplateDataFlavor dataElementFlavor;

   public SqlStringAreaTransferHandler(SqlStringArea var1, TransferHandler var2) {
      super(var2);
      this.sqlArea = var1;
      this.dataElementFlavor = new DeTemplateDataFlavor();
   }

   @Override
   public boolean canImport(TransferSupport var1) {
      return this.isSupportedDropLocation(var1) && (this.isSupportedFlavor(var1) || this.delegate.canImport(var1));
   }

   private boolean isSupportedDropLocation(TransferSupport var1) {
      JTextComponent.DropLocation var2 = (JTextComponent.DropLocation)var1.getDropLocation();
      return var2.getIndex() > 0 || this.sqlArea.isEmpty();
   }

   private boolean isTableDrop(TransferSupport var1) {
      return var1.isDataFlavorSupported(TransferableDatabaseObjects.FLAVOR);
   }

   private boolean isSupportedFlavor(TransferSupport var1) {
      return this.isTableDrop(var1) || this.isColumnDrop(var1) || this.isDataElementDrop(var1);
   }

   private boolean isColumnDrop(TransferSupport var1) {
      return var1.isDataFlavorSupported(TransferableDatabaseColumns.FLAVOR);
   }

   private boolean isDataElementDrop(TransferSupport var1) {
      return var1.isDataFlavorSupported(this.dataElementFlavor);
   }

   @Override
   public boolean importData(TransferSupport var1) {
      if (this.isTableDrop(var1)) {
         return this.dropTable(var1);
      } else if (this.isColumnDrop(var1)) {
         return this.dropColumns(var1);
      } else {
         return this.isDataElementDrop(var1) ? this.dropDataElement(var1) : this.delegate.importData(var1);
      }
   }

   private boolean dropTable(TransferSupport var1) {
      List var2 = getDraggedItems(var1, TransferableDatabaseObjects.FLAVOR, DatabaseObject.class);
      if (var2 != null && var2.size() == 1) {
         this.sqlArea.insertTable((DatabaseObject)var2.get(0));
         return true;
      } else {
         return false;
      }
   }

   private static <T> List<T> getDraggedItems(TransferSupport var0, DataFlavor var1, Class<T> var2) {
      try {
         ArrayList var3 = Lists.newArrayList();
         Object var4 = var0.getTransferable().getTransferData(var1);
         if (var4 instanceof List) {
            for (Object var7 : (List)var4) {
               if (var2.isInstance(var7)) {
                  var3.add(var2.cast(var7));
               }
            }
         } else if (var2.isInstance(var4)) {
            var3.add(var2.cast(var4));
         }

         return var3;
      } catch (Exception var8) {
         var8.printStackTrace();
         return null;
      }
   }

   private boolean dropColumns(TransferSupport var1) {
      List var2 = getDraggedItems(var1, TransferableDatabaseColumns.FLAVOR, DatabaseColumn.class);
      if (var2 != null && !var2.isEmpty()) {
         this.sqlArea.insertColumns(var2);
         return true;
      } else {
         return false;
      }
   }

   private boolean dropDataElement(TransferSupport var1) {
      List var2 = getDraggedItems(var1, this.dataElementFlavor, DataElementTemplate.class);
      if (var2 != null && var2.size() == 1) {
         this.sqlArea.insertGlobalDataElement((DataElementTemplate)var2.get(0));
         return true;
      } else {
         return false;
      }
   }
}
