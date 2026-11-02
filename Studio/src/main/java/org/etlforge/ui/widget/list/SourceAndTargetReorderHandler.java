package org.etlforge.ui.widget.list;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import org.etlforge.lang.PrimitiveConversions;

class SourceAndTargetReorderHandler extends TransferHandler {
   private final SourceAndTargetList owner;

   public SourceAndTargetReorderHandler(SourceAndTargetList var1) {
      this.owner = var1;
   }

   @Override
   public boolean canImport(TransferSupport var1) {
      if (var1.isDataFlavorSupported(SourceAndTargetReorderHandler.DraggedIndeces.FLAVOR)) {
         List var2 = this.getDraggedIndeces(var1);
         if (var2.isEmpty()) {
            return false;
         } else {
            return var2.size() == 1 ? true : this.isValidDropLocation(var1, var2);
         }
      } else {
         return false;
      }
   }

   private boolean isValidDropLocation(TransferSupport var1, List<Integer> var2) {
      int var3 = ((JList.DropLocation)var1.getDropLocation()).getIndex();
      return var3 < (Integer)var2.get(0) || var3 > (Integer)var2.get(var2.size() - 1);
   }

   @Override
   protected Transferable createTransferable(JComponent var1) {
      int[] var2 = ((JList)var1).getSelectedIndices();
      return this.isSingleInterval(var2) ? new SourceAndTargetReorderHandler.DraggedIndeces(PrimitiveConversions.toIntegerList(var2)) : null;
   }

   private boolean isSingleInterval(int[] var1) {
      int var2 = var1.length;
      return var2 > 0 && var1[var2 - 1] - var1[0] == var2 - 1;
   }

   @Override
   public int getSourceActions(JComponent var1) {
      return 2;
   }

   @Override
   public boolean importData(TransferSupport var1) {
      List var2 = this.getDraggedIndeces(var1);
      if (var2.isEmpty()) {
         return false;
      }

      this.moveDraggedItems(var1, var2);
      return true;
   }

   private List<Integer> getDraggedIndeces(TransferSupport var1) {
      try {
         if (var1.isDataFlavorSupported(SourceAndTargetReorderHandler.DraggedIndeces.FLAVOR)) {
            Object var2 = var1.getTransferable().getTransferData(SourceAndTargetReorderHandler.DraggedIndeces.FLAVOR);
            return (List<Integer>)var2;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return new ArrayList<>();
   }

   private void moveDraggedItems(TransferSupport var1, List<Integer> var2) {
      JList var3 = (JList)var1.getComponent();
      MutableListModel var4 = (MutableListModel)var3.getModel();
      List var5 = this.getItemsToMove(var4, var2);
      Collections.reverse(var2);

      for (Integer var7 : var2) {
         var4.removeItemAt(var7);
      }

      int var9 = this.getDropIndex(var1, var2);

      for (Object var8 : var5) {
         var4.addElementAt(var8, var9);
         var9++;
      }

      this.owner.targetListWasReordered();
   }

   private int getDropIndex(TransferSupport var1, List<Integer> var2) {
      JList.DropLocation var3 = (JList.DropLocation)var1.getDropLocation();
      int var4 = var3.getIndex();
      if (var4 > (Integer)var2.get(0)) {
         var4 -= var2.size();
      }

      return var4;
   }

   private List<Object> getItemsToMove(MutableListModel var1, List<Integer> var2) {
      ArrayList var3 = new ArrayList();

      for (Integer var5 : var2) {
         var3.add(var1.getElementAt(var5));
      }

      return var3;
   }

   private static class DraggedIndeces implements Transferable {
      public static final DataFlavor FLAVOR = new DataFlavor(List.class, "List of dragged indices");
      private final List<Integer> indeces;

      public DraggedIndeces(List<Integer> var1) {
         this.indeces = var1;
      }

      @Override
      public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException, IOException {
         if (this.isDataFlavorSupported(var1)) {
            return this.indeces;
         } else {
            throw new UnsupportedFlavorException(var1);
         }
      }

      @Override
      public DataFlavor[] getTransferDataFlavors() {
         return new DataFlavor[]{FLAVOR};
      }

      @Override
      public boolean isDataFlavorSupported(DataFlavor var1) {
         return FLAVOR.equals(var1);
      }
   }
}
