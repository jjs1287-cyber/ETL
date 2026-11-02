package com.etlforge.integration.client.ui.admin.system;

import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.admin.server.ServerConfigurationFile;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.text.editor.SearchableTextComponent;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.ui.admin.core.console.AdminConsolePart;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.ui.widget.text.undo.TextUndoRedoControllerFactory;

public final class ServerConfigurationFileViewer implements AdminConsolePart {
   private final ServerAdministration serverApi;
   private final KongaLayeredPane container;
   private final JLabel caption;
   private final Action downloadAction;
   private final Action uploadAction;
   private final Action revertAction;
   private final Action searchAction;
   private final KongaTextArea textArea;
   private UndoRedoController undoRedo;
   private ServerConfigurationFileViewer.ServerVersion serverVersion;
   private boolean hasBeenActivated;

   public ServerConfigurationFileViewer(ServerAdministration var1) {
      this.serverApi = var1;
      this.container = new KongaLayeredPane();
      this.textArea = new KongaTextArea();
      this.textArea.setLineWrap(false);
      this.textArea.setEditable(false);
      this.textArea.setFont(TextStyles.DefaultMonoSpaced.getFont());
      this.caption = TextStyles.DefaultBold.makeLabel("etlforge.conf");
      this.caption.setEnabled(false);
      this.downloadAction = new ServerConfigurationFileViewer.DownloadAction();
      this.uploadAction = new ServerConfigurationFileViewer.UploadAction();
      this.revertAction = new ServerConfigurationFileViewer.RevertAction();
      this.searchAction = this.createSearchAction();
   }

   @Override
   public String getId() {
      return "ServerConfigFileConsole";
   }

   @Override
   public void serverConnectionChanged(ServerConnectionEvent var1) {
   }

   @Override
   public Icon getIcon() {
      return null;
   }

   @Override
   public String getTitle() {
      return "etlforge.conf";
   }

   @Override
   public void populatePopupMenu(KongaPopupMenu var1) {
      var1.addActions(this.downloadAction, this.uploadAction, this.revertAction, null, this.searchAction);
   }

   @Override
   public void setActive(boolean var1) {
      if (var1 && !this.hasBeenActivated) {
         this.hasBeenActivated = true;
         ActionUtils.run(this.downloadAction, this, "download");
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.createToolbar()).center(this.textArea.asUiProvider());
      this.container.setBaseComponent(var2.container());
      var1.display(this.container);
   }

   private BoxBuilder createToolbar() {
      return Horizontal.box()
         .withBorder(Empty.border(5, 10, 5, 0))
         .add(this.caption)
         .glue()
         .addAllWithSpace(10, this.downloadAction, this.uploadAction, this.revertAction, this.searchAction);
   }

   @Override
   public void requestFocus() {
      this.textArea.requestFocusInWindow();
   }

   @Override
   public void dispose() {
      if (this.undoRedo != null) {
         this.undoRedo.dispose();
      }
   }

   private Action createSearchAction() {
      SearchableTextComponent var1 = new SearchableTextComponent(this.textArea, "Search in etlforge.conf", true);
      return var1.getFindAction();
   }

   private class CaptionUpdater {
      private final ServerConfigurationFile file;

      public CaptionUpdater(ServerConfigurationFile var2) {
         this.file = var2;
      }

      public void update() {
         String var1 = "etlforge.conf";
         if (this.file != null) {
            var1 = var1 + " @ " + this.getServerName();
         }

         ServerConfigurationFileViewer.this.caption.setText(var1);
         ServerConfigurationFileViewer.this.caption.setEnabled(this.file != null);
      }

      private String getServerName() {
         return StringUtils.substringBeforeLast(this.file.getServerInfo().getName(), ":");
      }
   }

   private final class ContentsDisplayer implements Runnable {
      private final ServerConfigurationFile file;

      public ContentsDisplayer(ServerConfigurationFile var2) {
         this.file = var2;
      }

      @Override
      public void run() {
         this.displayContent();
      }

      private void displayContent() {
         ServerConfigurationFileViewer.this.serverVersion = new ServerConfigurationFileViewer.ServerVersion(this.file.getContents());
         ServerConfigurationFileViewer.this.textArea.setText(ServerConfigurationFileViewer.this.serverVersion.getTextToDisplay());
         ServerConfigurationFileViewer.this.textArea.setCaretPosition(0);
         this.updateActions();
         this.installUndoRedo();
         ServerConfigurationFileViewer.this.textArea.setEditable(true);
      }

      private void installUndoRedo() {
         if (ServerConfigurationFileViewer.this.undoRedo != null) {
            ServerConfigurationFileViewer.this.undoRedo.dispose();
         }

         TextUndoRedoControllerFactory var1 = UndoRedoControllers.textFactory();
         ServerConfigurationFileViewer.this.undoRedo = var1.newController(ServerConfigurationFileViewer.this.textArea);
      }

      private void updateActions() {
         ServerConfigurationFileViewer.this.uploadAction.setEnabled(true);
         ServerConfigurationFileViewer.this.revertAction.setEnabled(true);
      }
   }

   private final class DownloadAction extends AbstractAction {
      public DownloadAction() {
         super("&Download etlforge.conf");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ServerConfigurationFileViewer.this.new Workflow().execute();
            }
         });
      }
   }

   private final class RevertAction extends AbstractAction {
      public RevertAction() {
         super("&Revert");
         this.putValue("ShortDescription", "Restore to the version that was downloaded from the server.");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ServerConfigurationFileViewer.this.textArea.setText(ServerConfigurationFileViewer.this.serverVersion.getTextToDisplay());
         ServerConfigurationFileViewer.this.textArea.setCaretPosition(0);
      }
   }

   private static class ServerVersion {
      private final String contents;
      private final boolean windowsLineBreaks;

      public ServerVersion(String var1) {
         this.contents = var1;
         this.windowsLineBreaks = var1.contains("\r\n");
      }

      public String getTextToDisplay() {
         String var1 = this.windowsLineBreaks ? this.contents.replace("\r\n", "\n") : this.contents;
         assert !var1.contains("\r\n");
         return var1;
      }

      public String sanitizeUpload(String var1) {
         assert !var1.contains("\r\n");
         return this.windowsLineBreaks ? var1.replace("\n", "\r\n") : var1;
      }
   }

   private final class UploadAction extends AbstractAction {
      public UploadAction() {
         super("&Upload etlforge.conf");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               UploadAction.this.submitJob();
            }
         });
      }

      private void submitJob() {
         String var1 = ServerConfigurationFileViewer.this.serverVersion.sanitizeUpload(ServerConfigurationFileViewer.this.textArea.getText());
         UploadServerConfigurationFileJob var2 = new UploadServerConfigurationFileJob(ServerConfigurationFileViewer.this.serverApi, var1);
         var2.submit(ServerConfigurationFileViewer.this.container, 0);
      }
   }

   private final class Workflow extends KongaSwingWorker<ServerConfigurationFile, Void> {
      private Workflow() {
      }

      protected ServerConfigurationFile doInBackground() throws Exception {
         DownloadConfigurationFileJob var1 = new DownloadConfigurationFileJob(ServerConfigurationFileViewer.this.serverApi);
         var1.submit(ServerConfigurationFileViewer.this.container, 0);
         return var1.getFile();
      }

      protected void onSuccess(ServerConfigurationFile var1) {
         this.updateCaption(var1);
         if (var1 != null) {
            this.displayFile(var1);
         }
      }

      private void updateCaption(ServerConfigurationFile var1) {
         ServerConfigurationFileViewer.this.new CaptionUpdater(var1).update();
      }

      private void displayFile(ServerConfigurationFile var1) {
         new BusyWorker(ServerConfigurationFileViewer.this.container, ServerConfigurationFileViewer.this.new ContentsDisplayer(var1)).run();
      }
   }
}
