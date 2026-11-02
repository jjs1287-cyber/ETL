package org.etlforge.application.ui.viewer.error.log;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.renderer.ListDecorTableRenderer;

public final class ErrorLogTable implements UiProvider {
   private final ErrorLogTableModel model = this.createModel();
   private final KongaRowTable table = this.createTable();
   private final ErrorLogTable.ViewDetailsAction viewDetailsAction = new ErrorLogTable.ViewDetailsAction(this.table.getAdaptee());

   public ErrorLogTable() {
      this.table.setDefaultAction(this.viewDetailsAction);
   }

   public void addError(final ErrorLogEntry var1) {
      Preconditions.checkNotNull(var1, "e");
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ErrorLogTable.this.model.addRowAt(0, var1);
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
      if (this.table.getSelection().isEmpty()) {
         this.table.selectFirstRow();
      }
   }

   private ErrorLogTableModel createModel() {
      List var1 = ErrorLog.getLoggedErrors();
      return new ErrorLogTableModel(var1);
   }

   private KongaRowTable createTable() {
      KongaTable var1 = this.createRawTable();
      return new KongaRowTable(var1);
   }

   private KongaTable createRawTable() {
      KongaTable var1 = new KongaTable(this.model);
      var1.setAutoCreateRowSorter(true);
      TableStyles.normal().makeOver(var1);
      this.installRenderer(var1);
      var1.setTransferHandler(new ErrorLogTable.TableTransferHandler());
      return var1;
   }

   private void installRenderer(KongaTable var1) {
      ListDecorTableRenderer var2 = new ListDecorTableRenderer();

      for (int var3 = 0; var3 < var1.getColumnCount(); var3++) {
         var1.setCellRendererAt(var3, var2);
      }
   }

   private static class TableTransferHandler extends TransferHandler {
      private TableTransferHandler() {
      }

      @Override
      protected Transferable createTransferable(JComponent var1) {
         KongaTable var2 = (KongaTable)var1;
         String var3 = this.getSelectionAsString(var2);
         return new StringSelection(var3);
      }

      private String getSelectionAsString(KongaTable var1) {
         ErrorLogTableModel var2 = (ErrorLogTableModel)var1.getModel();
         ArrayList var3 = Lists.newArrayList();

         for (int var7 : var1.getSelectedRows()) {
            var3.add(var2.getRowObjectAt(var7));
         }

         return ErrorLogEntry.toString(var3);
      }

      @Override
      public int getSourceActions(JComponent var1) {
         return 1;
      }
   }

   private static class ViewDetailsAction extends AbstractSingleObjectSelectionAction<ErrorLogEntry> {
      private final JComponent owner;

      public ViewDetailsAction(JComponent var1) {
         super(ErrorLogEntry.class);
         this.owner = var1;
         ActionDecorators.VIEW_DETAILS.decorate(this);
      }

      protected void run(final ErrorLogEntry var1, ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ViewDetailsAction.this.displayDetails(var1);
            }
         });
      }

      private void displayDetails(ErrorLogEntry var1) {
         ErrorLogEntryDetailsDisplayer var2 = new ErrorLogEntryDetailsDisplayer(var1);
         Window var3 = SwingUtilities.getWindowAncestor(this.owner);
         UiProviderDialogDisplayer.display(var3, var2, "Error Details", KongaDialog.CLOSE_ONLY, ErrorLogEntryDetailsDisplayer.class);
      }
   }
}
