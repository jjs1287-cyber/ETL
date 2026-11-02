package com.etlforge.integration.client.ui.sfdc.soql;

import java.awt.datatransfer.DataFlavor;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.dnd.DelegatingTransferHandler;
import org.etlforge.ui.text.DocumentUtils;

public final class QueryAreaTransferHandler extends DelegatingTransferHandler {
   private final DataFlavor sObjectFlavor = new SObjectDataFlavor();
   private final DataFlavor fieldFlavor = new FieldDataFlavor();
   private final SoqlInputInserter inserter;

   public QueryAreaTransferHandler(SoqlInputInserter var1, TransferHandler var2) {
      super(var2);
      this.inserter = var1;
   }

   @Override
   public boolean canImport(TransferSupport var1) {
      return this.isAllowedDropLocation(var1) && (var1.isDataFlavorSupported(this.sObjectFlavor) || this.delegate.canImport(var1));
   }

   private boolean isAllowedDropLocation(TransferSupport var1) {
      JTextComponent.DropLocation var2 = (JTextComponent.DropLocation)var1.getDropLocation();
      JTextComponent var3 = (JTextComponent)var1.getComponent();
      Document var4 = var3.getDocument();
      int var5 = var2.getIndex();
      if (var5 == 0) {
         return var4.getLength() == 0;
      } else {
         return var5 == var4.getLength() - 1 ? true : !this.isInTheMiddleOfIdentifier(var4, var5);
      }
   }

   private boolean isInTheMiddleOfIdentifier(Document var1, int var2) {
      char var3 = DocumentUtils.getText(var1, var2 - 1, 1).charAt(0);
      char var4 = DocumentUtils.getText(var1, var2, 1).charAt(0);
      return var3 == '.' && var4 == '.' ? false : isPartOfIdentifier(var3) && isPartOfIdentifier(var4);
   }

   private static boolean isPartOfIdentifier(char var0) {
      return Character.isJavaIdentifierPart(var0) || var0 == '.';
   }

   @Override
   public boolean importData(TransferSupport var1) {
      if (var1.isDataFlavorSupported(this.sObjectFlavor)) {
         SObjectSchema var2 = this.getDraggedItem(var1, this.sObjectFlavor, SObjectSchema.class);
         if (var2 != null) {
            this.inserter.insertSObject(var2);
            return true;
         }
      } else if (var1.isDataFlavorSupported(this.fieldFlavor)) {
         DraggedFields var3 = this.getDraggedItem(var1, this.fieldFlavor, DraggedFields.class);
         if (var3 != null) {
            this.inserter.insertFields(var3.sobject, var3.fields);
            return true;
         }
      }

      return this.delegate.importData(var1);
   }

   private <T> T getDraggedItem(TransferSupport var1, DataFlavor var2, Class<T> var3) {
      try {
         Object var4 = var1.getTransferable().getTransferData(var2);
         if (var3.isInstance(var4)) {
            return (T)var3.cast(var4);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return null;
   }
}
