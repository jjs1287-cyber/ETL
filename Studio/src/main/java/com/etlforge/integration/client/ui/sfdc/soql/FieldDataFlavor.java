package com.etlforge.integration.client.ui.sfdc.soql;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class FieldDataFlavor extends DataFlavor {
   public FieldDataFlavor() {
      super(FieldTransferable.class, "SObject.Field");
   }

   @Override
   public Reader getReaderForText(Transferable var1) throws UnsupportedFlavorException, IOException {
      if (var1.isDataFlavorSupported(this)) {
         Object var2 = var1.getTransferData(this);
         String var3 = "";
         if (var2 instanceof SObjectSchema.Field) {
            var3 = ((SObjectSchema.Field)var2).getName();
         } else if (var2 instanceof SObject.Field) {
            var3 = ((SObject.Field)var2).getName();
         } else {
            System.err.println("Unexpected transfer data: " + (var2 == null ? "[null]" : var2.getClass().getName()));
         }

         return new StringReader(var3);
      } else {
         return super.getReaderForText(var1);
      }
   }
}
