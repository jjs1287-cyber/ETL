package com.etlforge.integration.client.ui.admin.log;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.admin.server.DownloadServerFileCallback;
import com.etlforge.integration.client.admin.server.GetServerFileListCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.admin.server.ServerFileLocator;
import com.etlforge.integration.client.ui.admin.file.DownloadServerFileJob;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.ui.admin.core.console.AdminConsolePart;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.table.SortStatusPreferencesMemento;

public abstract class AbstractLogFilesConsole implements AdminConsolePart {
   private final String id;
   private final ServerAdministration serverApi;
   private final ServerLogFilesViewer viewer;
   private final AbstractLogFilesConsole.RefreshAction refreshAction;
   private volatile boolean hasBeenActivated;
   private volatile boolean isReceptiveToConnectionChanges;

   protected AbstractLogFilesConsole(String var1, ServerAdministration var2) {
      this.id = ArgCheck.checkNotEmpty(var1, "id");
      this.serverApi = Preconditions.checkNotNull(var2, "serverApi");
      this.viewer = this.createViewer();
      this.refreshAction = new AbstractLogFilesConsole.RefreshAction();
      this.viewer.setRefreshAction(this.refreshAction);
   }

   @Override
   public final String getId() {
      return this.id;
   }

   @Override
   public final Icon getIcon() {
      return null;
   }

   @Override
   public void populatePopupMenu(KongaPopupMenu var1) {
   }

   @Override
   public final void serverConnectionChanged(ServerConnectionEvent var1) {
      if (var1.isConnected() && this.hasBeenActivated && this.isReceptiveToConnectionChanges) {
         this.downloadFileList();
      }
   }

   @Override
   public final void decorate(UiCanvas var1) {
      this.viewer.decorate(var1);
   }

   @Override
   public final void requestFocus() {
      this.viewer.requestFocus();
   }

   @Override
   public final void dispose() {
      this.viewer.dispose();
   }

   @Override
   public final void setActive(boolean var1) {
      if (var1 && !this.hasBeenActivated) {
         this.hasBeenActivated = true;
         this.downloadFileList();
      }
   }

   protected String getDownloadFileListToolTip() {
      return "";
   }

   protected abstract UiJob createDownloadFileListJob(ServerAdministration var1, GetServerFileListCallback var2);

   private ServerLogFilesViewer createViewer() {
      ServerLogFilesViewer var1 = new ServerLogFilesViewer();
      var1.setDownloadFileListAction(new AbstractLogFilesConsole.DownloadFileListAction());
      var1.setViewFileAction(new AbstractLogFilesConsole.ViewSelectedFileAction());
      var1.setSortStatusMemento(new SortStatusPreferencesMemento(this.getClass()));
      return var1;
   }

   private void downloadFile(final ServerFileLocator var1) {
      DownloadServerFileCallback.Adapter var2 = new DownloadServerFileCallback.Adapter() {
         @Override
         public void fileWasDownloaded(File var1x) {
            AbstractLogFilesConsole.this.viewer.openFile(var1, var1x);
            AbstractLogFilesConsole.this.updateRefreshAction(var1);
         }
      };
      DownloadServerFileJob var3 = new DownloadServerFileJob(var1.getPath(), this.serverApi, var2);
      var3.submit(this.viewer.getFileContentWaitService(), 0);
   }

   private void updateRefreshAction(final ServerFileLocator var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AbstractLogFilesConsole.this.refreshAction.setSelection(SelectionFactory.newOrderedSelection(var1));
         }
      });
   }

   private void downloadFileList() {
      GetServerFileListCallback.Adapter var1 = new GetServerFileListCallback.Adapter() {
         @Override
         public void fileListRetrieved(ServerFileLocator[] var1) {
            AbstractLogFilesConsole.this.viewer.setDebugLogFileList(var1);
            AbstractLogFilesConsole.this.isReceptiveToConnectionChanges = true;
         }
      };
      UiJob var2 = this.createDownloadFileListJob(this.serverApi, var1);
      var2.submit(this.viewer.getWaitService(), 0);
   }

   private class DownloadFileListAction extends AbstractAction {
      public DownloadFileListAction() {
         super(EnterpriseAdminResources.getString("Log.Download.Name"));
         this.putValue("ShortDescription", AbstractLogFilesConsole.this.getDownloadFileListToolTip());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         AbstractLogFilesConsole.this.downloadFileList();
      }
   }

   private class RefreshAction extends AbstractSingleObjectSelectionAction<ServerFileLocator> {
      public RefreshAction() {
         super(ServerFileLocator.class);
         ActionDecorators.REFRESH.decorate(this);
         this.setEnabled(false);
      }

      protected void run(ServerFileLocator var1, ActionEvent var2) {
         AbstractLogFilesConsole.this.downloadFile(var1);
      }
   }

   private class ViewSelectedFileAction extends AbstractSingleObjectSelectionAction<ServerFileLocator> {
      public ViewSelectedFileAction() {
         super(ServerFileLocator.class, EnterpriseAdminResources.getString("Log.View.Name"));
         this.putValue("ShortDescription", EnterpriseAdminResources.getString("Log.View.ToolTip"));
      }

      protected void run(ServerFileLocator var1, ActionEvent var2) {
         AbstractLogFilesConsole.this.downloadFile(var1);
      }
   }
}
