package org.etlforge.ui.widget.text;

import com.google.common.base.Preconditions;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;

public final class FileDropSupport {
   private final JTextComponent text;
   private final FileDropStrategy dropStrategy;
   private final TransferHandler oldTransferHandler;

   private FileDropSupport(JTextComponent var1, FileDropStrategy var2) {
      this.text = var1;
      this.dropStrategy = var2;
      this.oldTransferHandler = var1.getTransferHandler();
      var1.setTransferHandler(new FileDropSupport.FileDropTransferHandler());
   }

   public static FileDropSupport install(JTextComponent var0, FileDropStrategy var1) {
      Preconditions.checkNotNull(var0, "text");
      Preconditions.checkNotNull(var1, "dropStrategy");
      return new FileDropSupport(var0, var1);
   }

   public void uninstall() {
      this.text.setTransferHandler(this.oldTransferHandler);
   }

   private class FileDropTransferHandler extends TransferHandler {
      private FileDropTransferHandler() {
      }

      @Override
      public boolean canImport(JComponent var1, DataFlavor[] var2) {
         if (var1 == FileDropSupport.this.text) {
            for (DataFlavor var6 : var2) {
               if (var6.isFlavorJavaFileListType()) {
                  return FileDropSupport.this.dropStrategy.canDrop(FileDropSupport.this.text);
               }
            }
         }

         return FileDropSupport.this.oldTransferHandler != null ? FileDropSupport.this.oldTransferHandler.canImport(var1, var2) : false;
      }

      @Override
      public boolean importData(JComponent var1, Transferable var2) {
         try {
            if (var2.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
               List var3 = (List)var2.getTransferData(DataFlavor.javaFileListFlavor);
               boolean var4 = FileDropSupport.this.dropStrategy.handleDrop(FileDropSupport.this.text, var3);
               if (var4) {
                  FileDropSupport.this.text.requestFocusInWindow();
               }

               return var4;
            }
         } catch (UnsupportedFlavorException var5) {
            return false;
         } catch (IOException var6) {
            return false;
         }

         return FileDropSupport.this.oldTransferHandler != null ? FileDropSupport.this.oldTransferHandler.importData(var1, var2) : super.importData(var1, var2);
      }

      @Override
      public void exportAsDrag(JComponent var1, InputEvent var2, int var3) {
         if (FileDropSupport.this.oldTransferHandler != null) {
            FileDropSupport.this.oldTransferHandler.exportAsDrag(var1, var2, var3);
         } else {
            super.exportAsDrag(var1, var2, var3);
         }
      }

      @Override
      public void exportToClipboard(JComponent var1, Clipboard var2, int var3) throws IllegalStateException {
         if (FileDropSupport.this.oldTransferHandler != null) {
            FileDropSupport.this.oldTransferHandler.exportToClipboard(var1, var2, var3);
         } else {
            super.exportToClipboard(var1, var2, var3);
         }
      }

      @Override
      public int getSourceActions(JComponent var1) {
         return FileDropSupport.this.oldTransferHandler != null ? FileDropSupport.this.oldTransferHandler.getSourceActions(var1) : super.getSourceActions(var1);
      }

      @Override
      public Icon getVisualRepresentation(Transferable var1) {
         return FileDropSupport.this.oldTransferHandler != null
            ? FileDropSupport.this.oldTransferHandler.getVisualRepresentation(var1)
            : super.getVisualRepresentation(var1);
      }
   }
}
