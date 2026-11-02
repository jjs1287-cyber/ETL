package org.etlforge.integration.activity.ui.monitor;

import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.log.HtmlLogMessageDisplayer;
import org.etlforge.integration.activity.ui.log.OperationLogTable;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;
import org.etlforge.integration.activity.ui.log.OperationLogTableModel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.window.WindowUtils;

final class LogMonitor implements UiProvider {
   private final OperationLogTable table = new OperationLogTable(emptyModel());

   public LogMonitor() {
      this.table.setVisibleRowCount(2);
      new KongaRowTable(this.table).setDefaultAction(new LogMonitor.ShowMessageAction());
   }

   private static OperationLogTableModel emptyModel() {
      return new OperationLogTableModel(
         OperationLogTableColumn.OPERATION,
         OperationLogTableColumn.STATUS,
         OperationLogTableColumn.TIME_RECEIVED,
         OperationLogTableColumn.TIME_STARTED,
         OperationLogTableColumn.TIME_DONE,
         OperationLogTableColumn.DURATION
      );
   }

   public void update(List<OperationLogEntry> var1) {
      final OperationLogTableModel var2 = emptyModel();
      var2.addRows(var1);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LogMonitor.this.table.setModel(var2);
         }
      });
   }

   public void reset() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LogMonitor.this.table.setModel(LogMonitor.emptyModel());
         }
      });
   }

   @Override
   public final void decorate(UiCanvas var1) {
      var1.display(new JScrollPane(this.table));
   }

   @Override
   public final void requestFocus() {
      this.table.requestFocusInWindow();
   }

   private class ShowMessageAction extends AbstractSingleObjectSelectionAction<OperationLogEntry> {
      public ShowMessageAction() {
         super(OperationLogEntry.class);
      }

      protected boolean isEnabledFor(OperationLogEntry var1) {
         return var1 != null;
      }

      protected void run(final OperationLogEntry var1, ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ShowMessageAction.this.showMessage(var1);
            }
         });
      }

      private void showMessage(OperationLogEntry var1) {
         HtmlLogMessageDisplayer var2 = new HtmlLogMessageDisplayer();
         var2.displayDetails(var1);
         KongaDialog var3 = this.createDialog(var2);
         var3.setVisible(true);
      }

      private KongaDialog createDialog(HtmlLogMessageDisplayer var1) {
         KongaDialog var2 = new KongaDialog(this.getOwner(), "Log Message");
         var2.standardLayout(var1, KongaDialog.CLOSE_ONLY);
         var2.manageLocation(OperationMonitor.class);
         WindowUtils.ensureMinimumSize(var2, 400, 300);
         var2.setCloseButtonAsDefault();
         return var2;
      }

      private Window getOwner() {
         Window var1 = SwingUtilities.getWindowAncestor(LogMonitor.this.table);
         return var1 == null ? UiUtils.getActiveWindow() : var1;
      }
   }
}
