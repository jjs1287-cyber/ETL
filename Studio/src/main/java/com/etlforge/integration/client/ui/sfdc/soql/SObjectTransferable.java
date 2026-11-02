package com.etlforge.integration.client.ui.sfdc.soql;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.apache.commons.lang.ArrayUtils;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class SObjectTransferable implements Transferable {
   private static final DataFlavor[] FLAVORS = new DataFlavor[]{new SObjectDataFlavor(), DataFlavor.stringFlavor};
   private final SObjectSchema objectSchema;
   private final SObject preVersion5Object;

   public SObjectTransferable(SObjectSchema var1) {
      this.objectSchema = var1;
      this.preVersion5Object = null;
   }

   public SObjectTransferable(SObject var1) {
      this.preVersion5Object = var1;
      this.objectSchema = null;
   }

   @Override
   public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException, IOException {
      if (var1 instanceof SObjectDataFlavor) {
         return this.objectSchema != null ? this.objectSchema : this.preVersion5Object;
      } else if (var1.equals(DataFlavor.stringFlavor)) {
         return this.objectSchema != null ? this.objectSchema.getName() : this.preVersion5Object.getLocalName();
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
