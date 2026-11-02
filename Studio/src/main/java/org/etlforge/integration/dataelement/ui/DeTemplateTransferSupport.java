package org.etlforge.integration.dataelement.ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.dataelement.DataElementTemplate;
import org.etlforge.ui.dnd.DelegatingTransferHandler;

public final class DeTemplateTransferSupport {
   private static final DataFlavor flavor = new DeTemplateDataFlavor();

   public static void installDropHandler(JTextComponent var0) {
      installDropHandler(var0, "[%s]");
   }

   public static void installDropHandler(JTextComponent var0, String var1) {
      TransferHandler var2 = var0.getTransferHandler();
      if (!(var2 instanceof DeTemplateTransferSupport.ImportHandler)) {
         var0.setTransferHandler(new DeTemplateTransferSupport.ImportHandler(var1, var2));
      }
   }

   public static void installRejector(JTextComponent var0) {
      TransferHandler var1 = var0.getTransferHandler();
      if (!(var1 instanceof DeTemplateTransferSupport.Rejector)) {
         var0.setTransferHandler(new DeTemplateTransferSupport.Rejector(var1));
      }
   }

   public static boolean isRejecting(JTextComponent var0) {
      return var0.getTransferHandler() instanceof DeTemplateTransferSupport.Rejector;
   }

   private DeTemplateTransferSupport() {
   }

   private static final class ImportHandler extends DelegatingTransferHandler {
      private final String pattern;

      public ImportHandler(String var1, TransferHandler var2) {
         super(var2);
         this.pattern = var1;
      }

      @Override
      public boolean canImport(TransferSupport var1) {
         JTextComponent var2 = (JTextComponent)var1.getComponent();
         return var2.isEditable() && var2.isEnabled() && (var1.isDataFlavorSupported(DeTemplateTransferSupport.flavor) || this.delegate.canImport(var1));
      }

      @Override
      public boolean importData(TransferSupport var1) {
         if (var1.isDataFlavorSupported(DeTemplateTransferSupport.flavor)) {
            DataElementTemplate var2 = this.getDraggedDataElement(var1);
            if (var2 != null) {
               return this.importDataElement(var1, var2);
            }
         }

         return this.delegate.importData(var1);
      }

      private DataElementTemplate getDraggedDataElement(TransferSupport var1) {
         try {
            Object var2 = var1.getTransferable().getTransferData(DeTemplateTransferSupport.flavor);
            if (var2 instanceof DataElementTemplate) {
               return (DataElementTemplate)var2;
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         return null;
      }

      private boolean importDataElement(TransferSupport var1, DataElementTemplate var2) {
         final String var3 = String.format(this.pattern, var2.getName());
         final Component var4 = var1.getComponent();
         var4.requestFocusInWindow();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ((JTextComponent)var4).replaceSelection(var3);
            }
         });
         return true;
      }
   }

   private static final class Rejector extends DelegatingTransferHandler {
      public Rejector(TransferHandler var1) {
         super(var1);
      }

      @Override
      public boolean canImport(TransferSupport var1) {
         return var1.isDataFlavorSupported(DeTemplateTransferSupport.flavor) ? false : this.delegate.canImport(var1);
      }
   }
}
