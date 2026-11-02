package org.etlforge.integration.client.ui.interchange.location.local;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.UndoableEditEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.datalocation.cache.LocalFileLocationCache;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.entity.LastLocalFile;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.panel.FileLocationPanel;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.ui.Size;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class LocalFileLocationPanel extends DataLocationPanel {
   private final JLabel lblNote = new JLabel("");
   private final FolderPathField parentFolderField;
   private final FileLocationPanel fileLocationPanel;
   private final LocalFileLocationPanel.BrowseAction browseAction;
   private LocalFileLocationCache cache;
   private ServerConnectionService serverConnectionService;
   private ServerConnectionListener serverConnectionListener;

   public void setServerManager(ServerManager var1) {
      this.serverConnectionService = var1;
      this.setupServerConnectionListener();
      this.serverConnectionService.addServerConnectionListener(this.serverConnectionListener);
   }

   private void setupServerConnectionListener() {
      this.serverConnectionListener = new ServerConnectionListener() {
         @Override
         public void serverConnectionChanged(ServerConnectionEvent var1) {
            LocalFileLocationPanel.this.setBrowseBasedOnConnection();
         }
      };
   }

   private void setBrowseBasedOnConnection() {
      String var1 = this.serverConnectionService.getCurrentServerName();
      if (var1 != null && var1.toUpperCase().contains("LOCALHOST")) {
         this.browseAction.setEnabled(true);
         this.lblNote.setText("");
      } else {
         this.browseAction.setEnabled(false);
         this.lblNote.setForeground(Color.RED);
         this.lblNote
            .setText("Browse is only enabled when you are connected to the " + ApplicationConfiguration.getCompanyName() + " server installed on this machine.");
      }
   }

   public LocalFileLocationPanel(LocalFileLocation var1, SourceTarget var2) {
      super(var1, var2);
      byte var3 = 40;
      LastLocalFile var4 = LastLocalFile.forType(var2);
      this.parentFolderField = createParentPathField(var3);
      this.fileLocationPanel = new FileLocationPanel(var1, var2, false, this, var3) {
         @Override
         protected boolean needsLoginControls() {
            return false;
         }
      };
      this.browseAction = new LocalFileLocationPanel.BrowseAction(var4.getRecentFileStore(), var2);
      this.reset();
      this.ensureParentFolderHasValue(var4);
      this.parentFolderField.addToParent(this);
   }

   private static FolderPathField createParentPathField(int var0) {
      FolderPathField var1 = new FolderPathField(
         "Folder", "&Folder - Local path on machine where ETLForge Integration Server is installed", Size.columns(var0)
      );
      var1.setAllowDriveLetter(true);
      var1.setAllowFileshares(true);
      var1.setRequired(true);
      return var1;
   }

   private void ensureParentFolderHasValue(LastLocalFile var1) {
      if (this.parentFolderField.isEmpty()) {
         File var2 = var1.getSuggestedParent();
         this.parentFolderField.setValue(var2.getAbsolutePath());
      }
   }

   public void setCache(LocalFileLocationCache var1) {
      this.cache = var1;
      this.parentFolderField.installAutoCompletion(new FolderPathHistory(var1));
   }

   @Override
   protected void reset() {
      LocalFileLocation var1 = this.getDisplayedObject();
      this.parentFolderField.setValue(var1.getFolderPath());
      this.fileLocationPanel.reset(var1);
   }

   public LocalFileLocation getDisplayedObject() {
      return (LocalFileLocation)super.getDisplayedObject();
   }

   @Override
   public void apply() throws IntegrationDataPanelException {
      LocalFileLocation var1 = this.getDisplayedObject();
      var1.setFolderPath(this.parentFolderField.getValue());
      this.fileLocationPanel.apply(var1);
      this.updateCache(var1);
   }

   private void updateCache(LocalFileLocation var1) {
      if (this.cache != null) {
         this.cache.update(var1);
      }
   }

   @Override
   public void display() {
      super.display();
      this.doLayout();
   }

   private void doLayout() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.container().setLayout(new FlowLayout(0));
      var1.addRow(InvisiblePanel.of(this.browseAction), this.lblNote);
      OneColumnPanel var2 = new OneColumnPanel();
      var2.withInsets(3, 5, 10, 10);
      var2.add(var1);
      var2.add(InputFieldFactory.getCapsule(this.parentFolderField));
      JPanel var3 = InvisiblePanel.newPanel();
      this.fileLocationPanel.layoutComponents(var3, null);
      var2.add(var3);
      this.wrap(var2);
      this.setBrowseBasedOnConnection();
   }

   @Override
   public void setInitialFocus() {
      this.parentFolderField.requestFocus();
   }

   @Override
   public void dispose() {
      super.dispose();
      this.serverConnectionService.removeServerConnectionListener(this.serverConnectionListener);
   }

   private class BrowseAction extends AbstractAction {
      private final RecentFileStore lastFile;
      private final SourceTarget st;

      public BrowseAction(RecentFileStore var2, SourceTarget var3) {
         super(ApplicationResources.getString("Browse.Action"));
         this.lastFile = var2;
         this.st = var3;
         this.putValue("ShortDescription", "Open a file browser and select the file you want to use.");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               BrowseAction.this.browse();
            }
         });
      }

      private void browse() {
         File var1 = this.selectFile();
         if (var1 != null) {
            String var2 = LocalFileLocationPanel.this.parentFolderField.getValue();
            String var3 = LocalFileLocationPanel.this.fileLocationPanel.getFileFilterString();
            LocalFileLocationPanel.BrowseEdit var4 = LocalFileLocationPanel.this.new BrowseEdit(var2, var3, var1);
            if (var4.isChange()) {
               this.updateUi(var4);
            }
         }
      }

      private File selectFile() {
         FileChooser var1 = ApplicationFileChooser.getInstance(null);
         var1.setSelectionMode(FileChooser.SelectionMode.FILES_AND_DIRECTORIES);
         return this.st == SourceTarget.Source
            ? var1.getFileToOpen(LocalFileLocationPanel.this.getUi(), (FileFilter)null, this.lastFile, "Select a source file")
            : var1.getFileToSave(LocalFileLocationPanel.this.getUi(), (FileFilter)null, this.lastFile, "Select a target file");
      }

      private void updateUi(LocalFileLocationPanel.BrowseEdit var1) {
         LocalFileLocationPanel.this.setSendInputChangeEvents(false);
         LocalFileLocationPanel.this.setSendInputChangeEvents(false);
         LocalFileLocationPanel.this.parentFolderField.setValue(var1.getNewFolder());
         LocalFileLocationPanel.this.fileLocationPanel.setFileFilterString(var1.getNewName());
         LocalFileLocationPanel.this.setSendInputChangeEvents(true);
         LocalFileLocationPanel.this.setSendInputChangeEvents(true);
         LocalFileLocationPanel.this.fireInputChanged(null, null);
         LocalFileLocationPanel.this.fireUndoableEditEvent(new UndoableEditEvent(this, var1));
      }
   }

   private class BrowseEdit extends AbstractUndoableEdit {
      private final String oldFolder;
      private final String oldName;
      private final String newFolder;
      private final String newName;

      public BrowseEdit(String var2, String var3, File var4) {
         this.oldFolder = var2;
         this.oldName = var3;
         if (var4.isDirectory()) {
            this.newFolder = var4.getAbsolutePath();
            this.newName = var3;
         } else {
            this.newFolder = var4.getParent();
            this.newName = var4.getName();
         }
      }

      public boolean isChange() {
         return !this.oldFolder.equals(this.newFolder) || !this.oldName.equals(this.newName);
      }

      public String getNewFolder() {
         return this.newFolder;
      }

      public String getNewName() {
         return this.newName;
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         this.update(this.oldFolder, this.oldName);
      }

      private void update(String var1, String var2) {
         LocalFileLocationPanel.this.setSendUndoEvents(false);
         LocalFileLocationPanel.this.parentFolderField.setValue(var1);
         LocalFileLocationPanel.this.fileLocationPanel.setFileFilterString(var2);
         LocalFileLocationPanel.this.setSendUndoEvents(true);
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         this.update(this.newFolder, this.newName);
      }

      @Override
      public String getPresentationName() {
         return "Selected File";
      }
   }
}
