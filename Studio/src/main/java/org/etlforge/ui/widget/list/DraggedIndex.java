package org.etlforge.ui.widget.list;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

final class DraggedIndex implements Transferable {
   public static final DataFlavor FLAVOR = createFlavor();
   private final int index;
   private final String stringRep;

   private static DataFlavor createFlavor() {
      try {
         String var0 = "application/x-java-jvm-local-objectref;class=" + DraggedIndex.class.getName();
         return new DataFlavor(var0);
      } catch (ClassNotFoundException var1) {
         throw new RuntimeException();
      }
   }

   public DraggedIndex(int var1) {
      this(var1, null);
   }

   public DraggedIndex(int var1, String var2) {
      this.index = var1;
      this.stringRep = var2;
   }

   public int getIndex() {
      return this.index;
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return this.stringRep == null ? new DataFlavor[]{FLAVOR} : new DataFlavor[]{FLAVOR, DataFlavor.stringFlavor};
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor var1) {
      return var1 == FLAVOR ? true : var1 == DataFlavor.stringFlavor && this.stringRep != null;
   }

   @Override
   public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException {
      if (var1 == FLAVOR) {
         return this.index;
      } else if (var1 == DataFlavor.stringFlavor && this.stringRep != null) {
         return this.stringRep;
      } else {
         throw new UnsupportedFlavorException(var1);
      }
   }
}
