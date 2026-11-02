package com.etlforge.integration.client.ui.admin.log;

import com.etlforge.integration.client.admin.server.ServerFileLocator;
import com.etlforge.integration.client.ui.admin.file.ServerFileContentViewer;
import com.etlforge.integration.client.ui.admin.file.ServerFileLocatorTable;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.io.File;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.etlforge.system.Disposable;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.ui.widget.table.SortStatusMemento;

public final class ServerLogFilesViewer implements UiProvider, Disposable {
   private final KongaLayeredPane ui = new KongaLayeredPane();
   private final ServerFileLocatorTable fileTable = new ServerFileLocatorTable();
   private final ServerFileContentViewer fileViewer;
   private final JButton downloadFileListButton;
   private SelectionAction viewFileAction;
   private final JLabel caption;

   public ServerLogFilesViewer() {
      this.fileTable.addSelectionListener(new ServerLogFilesViewer.ViewSelectedFileTimer());
      this.fileViewer = new ServerFileContentViewer();
      this.downloadFileListButton = this.createButton(EnterpriseAdminResources.getString("Log.Download.Name"));
      this.caption = new JLabel(EnterpriseAdminResources.getString("Log.View.Select.Caption"));
      this.caption.setEnabled(false);
   }

   @Override
   public void dispose() {
      this.fileViewer.dispose();
   }

   public void setDebugLogFileList(final ServerFileLocator[] var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ServerLogFilesViewer.this.fileTable.setFiles(var1);
            ServerLogFilesViewer.this.caption.setEnabled(var1.length > 0);
            ServerLogFilesViewer.this.openFile(null, null);
         }
      });
   }

   public void openFile(final ServerFileLocator var1, final File var2) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ServerLogFilesViewer.this.fileViewer.setSelectedFile(var1, var2);
         }
      });
   }

   public void setRefreshAction(Action var1) {
      this.fileViewer.setRefreshAction(var1);
   }

   public void setSortStatusMemento(SortStatusMemento var1) {
      this.fileTable.setSortStatusMemento(var1);
   }

   public WaitService getWaitService() {
      return this.ui;
   }

   public WaitService getFileContentWaitService() {
      return this.fileViewer.getWaitService();
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder().north(this.layoutControlPanel()).center(this.fileTable);
      final KongaSplitPane var3 = KongaSplitPane.verticalSplit(var2, this.fileViewer);
      var3.setDividerLocation(0.5);
      this.ui.setBaseComponent(var3);
      var1.display(this.ui);
      DelayedJob.createAndSchedule(150, new Runnable() {
         @Override
         public void run() {
            var3.setDividerLocation(0.5);
         }
      });
   }

   @Override
   public void requestFocus() {
      this.fileTable.requestFocus();
   }

   public void setDownloadFileListAction(Action var1) {
      this.downloadFileListButton.setAction(var1);
   }

   public void setViewFileAction(SelectionAction var1) {
      this.viewFileAction = var1;
      var1.setSelectionSource(this.fileTable);
   }

   private JButton createButton(String var1) {
      KongaButton var2 = new KongaButton(var1);
      var2.setEnabled(false);
      return var2;
   }

   private UiProvider layoutControlPanel() {
      return Horizontal.box().addAllWithSpace(10, this.downloadFileListButton, this.caption).withEmptyBorder(5);
   }

   private class ViewSelectedFileTimer implements SelectionListener, Runnable {
      private DelayedJob timer;
      private ServerFileLocator selected;

      private ViewSelectedFileTimer() {
      }

      @Override
      public void run() {
         if (ServerLogFilesViewer.this.viewFileAction != null) {
            ServerLogFilesViewer.this.viewFileAction.actionPerformed(null);
         }
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         ServerFileLocator var2 = SelectionUtils.getSingleSelection(var1.getSelection(), ServerFileLocator.class);
         if (var2 != this.selected) {
            this.stopTimer();
            if (var2 != null) {
               this.selected = var2;
               this.startTimer();
            }
         }
      }

      private void stopTimer() {
         if (this.timer != null) {
            this.timer.cancel();
         }
      }

      private void startTimer() {
         this.timer = DelayedJob.createAndSchedule(200, this);
      }
   }
}
