package org.etlforge.integration.dataelement.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.etlforge.integration.dataelement.DataElementTemplate;

public class DeTemplateDataFlavor extends DataFlavor {
   public DeTemplateDataFlavor() {
      super(DeTemplateTransferable.class, "DataElementTemplate");
   }

   @Override
   public Reader getReaderForText(Transferable var1) throws UnsupportedFlavorException, IOException {
      if (var1.isDataFlavorSupported(this)) {
         Object var2 = var1.getTransferData(this);
         if (var2 instanceof DataElementTemplate) {
            return new StringReader(((DataElementTemplate)var2).getName());
         }
      }

      return super.getReaderForText(var1);
   }
}
