package org.etlforge.integration.dataelement.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.apache.commons.lang.ArrayUtils;
import org.etlforge.integration.dataelement.DataElementTemplate;

public class DeTemplateTransferable implements Transferable {
   private static final DataFlavor[] FLAVORS = new DataFlavor[]{new DeTemplateDataFlavor(), DataFlavor.stringFlavor};
   private final DataElementTemplate deTemplate;

   public DeTemplateTransferable(DataElementTemplate var1) {
      this.deTemplate = var1;
   }

   @Override
   public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException, IOException {
      if (var1 instanceof DeTemplateDataFlavor) {
         return this.deTemplate;
      } else if (var1.equals(DataFlavor.stringFlavor)) {
         return this.deTemplate.getName();
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
