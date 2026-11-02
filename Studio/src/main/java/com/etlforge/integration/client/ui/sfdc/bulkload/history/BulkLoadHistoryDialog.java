package com.etlforge.integration.client.ui.sfdc.bulkload.history;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.LayeredKongaDialog;

public final class BulkLoadHistoryDialog {
   private final BulkLoadHistoryView view;
   private final BulkLoadHistoryController controller;

   public BulkLoadHistoryDialog(BulkLoadHistoryView var1, BulkLoadHistoryController var2) {
      this.view = Preconditions.checkNotNull(var1);
      this.controller = Preconditions.checkNotNull(var2);
   }

   public void open(Window var1, boolean var2) {
      LayeredKongaDialog var3 = this.createDialog(var1, var2, this.view);
      this.view.setWaitService(var3);
      var3.addWindowListener(new BulkLoadHistoryDialog.Disposer(this.controller));
      var3.pack();
      var3.manageLocation(this.getClass());
      this.startDownload(this.controller);
      var3.setVisible(true);
   }

   private LayeredKongaDialog createDialog(Window var1, boolean var2, BulkLoadHistoryView var3) {
      LayeredKongaDialog var4 = new LayeredKongaDialog(var1, "Bulk Load History", var2 ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
      var4.getDefaultLayout().center(var3);
      JPanel var5 = var4.getButtonPanel(KongaDialog.CLOSE_ONLY, 0);
      var5.setBorder(Empty.border(10, 0, 0, 0));
      var4.getDefaultLayout().south(var5);
      var4.getDefaultLayout().withBorder(Empty.border(10));
      var4.setCloseButtonAsDefault();
      return var4;
   }

   private void startDownload(final BulkLoadHistoryController var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var1.downloadHistory();
         }
      });
   }

   private static class Disposer extends WindowAdapter {
      private final BulkLoadHistoryController controller;

      public Disposer(BulkLoadHistoryController var1) {
         this.controller = var1;
      }

      @Override
      public void windowClosing(final WindowEvent var1) {
         this.controller.cancelOutstandingJobs();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               var1.getWindow().dispose();
            }
         });
      }
   }
}
