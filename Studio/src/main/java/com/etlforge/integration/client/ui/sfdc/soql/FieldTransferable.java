package com.etlforge.integration.client.ui.sfdc.soql;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.apache.commons.lang.ArrayUtils;

public final class FieldTransferable implements Transferable {
   private static final DataFlavor[] FLAVORS = new DataFlavor[]{new FieldDataFlavor(), DataFlavor.stringFlavor};
   private final DraggedFields fields;
   private final com.etlforge.integration.client.ui.sfdc.soql.v4.DraggedFields version4Fields;

   public FieldTransferable(DraggedFields var1) {
      this.fields = var1;
      this.version4Fields = null;
   }

   public FieldTransferable(com.etlforge.integration.client.ui.sfdc.soql.v4.DraggedFields var1) {
      this.version4Fields = var1;
      this.fields = null;
   }

   @Override
   public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException, IOException {
      if (var1 instanceof FieldDataFlavor) {
         return this.fields != null ? this.fields : this.version4Fields;
      } else if (var1.equals(DataFlavor.stringFlavor)) {
         return this.fields != null ? this.fields.getListOfFieldNames() : this.version4Fields.getListOfFieldNames();
      } else {
         throw new UnsupportedFlavorException(var1);
      }
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return (DataFlavor[])FLAVORS.clone();
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor var1) {
      return ArrayUtils.contains(FLAVORS, var1);
   }
}
