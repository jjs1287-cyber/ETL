package com.etlforge.integration.client.ui.sfdc.soql;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class SObjectDataFlavor extends DataFlavor {
   public SObjectDataFlavor() {
      super(SObjectTransferable.class, "SObject");
   }

   @Override
   public Reader getReaderForText(Transferable var1) throws UnsupportedFlavorException, IOException {
      if (var1.isDataFlavorSupported(this)) {
         Object var2 = var1.getTransferData(this);
         String var3 = "";
         if (var2 instanceof SObjectSchema) {
            var3 = ((SObjectSchema)var2).getName();
         } else if (var2 instanceof SObject) {
            var3 = ((SObject)var2).getLocalName();
         } else {
            System.err.println("Unexpected transfer data: " + (var2 == null ? "[null]" : var2.getClass().getName()));
         }

         return new StringReader(var3);
      } else {
         return super.getReaderForText(var1);
      }
   }
}
