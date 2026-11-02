package org.etlforge.ui.dnd;

import com.google.common.base.Preconditions;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

public abstract class DelegatingTransferHandler extends TransferHandler {
   protected final TransferHandler delegate;

   public DelegatingTransferHandler(TransferHandler var1) {
      Preconditions.checkNotNull(var1, "delegate");
      this.delegate = var1;
   }

   @Override
   public boolean canImport(JComponent var1, DataFlavor[] var2) {
      return this.delegate.canImport(var1, var2);
   }

   @Override
   public boolean canImport(TransferSupport var1) {
      return this.delegate.canImport(var1);
   }

   @Override
   public void exportAsDrag(JComponent var1, InputEvent var2, int var3) {
      this.delegate.exportAsDrag(var1, var2, var3);
   }

   @Override
   public void exportToClipboard(JComponent var1, Clipboard var2, int var3) throws IllegalStateException {
      this.delegate.exportToClipboard(var1, var2, var3);
   }

   @Override
   public int getSourceActions(JComponent var1) {
      return this.delegate.getSourceActions(var1);
   }

   @Override
   public Icon getVisualRepresentation(Transferable var1) {
      return this.delegate.getVisualRepresentation(var1);
   }

   @Override
   public boolean importData(JComponent var1, Transferable var2) {
      return this.delegate.importData(var1, var2);
   }

   @Override
   public boolean importData(TransferSupport var1) {
      return this.delegate.importData(var1);
   }
}
