package org.etlforge.integration.loadsource.client;

import com.google.common.base.Preconditions;
import java.io.File;
import java.util.List;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.util.file.FileUi;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.util.preference.BooleanKeyValue;
import org.etlforge.util.preference.PreferencesKey;

public class ProjectSourceDataFileInputProvider implements LoadSourceDataInputProvider {
   static final PreferencesKey DISPLAY_PLUGIN_SELECTOR = new PreferencesKey(ProjectSourceDataFileInputProvider.class, "DisplayPluginSelector");
   private final ServerManager serverManager;
   private final ApplicationWindow window;
   private final PluginSettingsStore pluginSettings;
   private RecentFileStore projectFileStore;
   private final boolean ediSource;

   public ProjectSourceDataFileInputProvider(ApplicationWindow var1, ServerManager var2, PluginSettingsStore var3, boolean var4) {
      this.window = Preconditions.checkNotNull(var1, "window");
      this.serverManager = Preconditions.checkNotNull(var2, "serverManager");
      this.pluginSettings = Preconditions.checkNotNull(var3, "pluginSettings");
      this.ediSource = var4;
   }

   public void setProjectFileStore(RecentFileStore var1) {
      this.projectFileStore = var1;
   }

   public LoadSourceDataFileInput getInput() {
      LoadSourceDataFileInput var1 = this.getInputFromUi();
      if (var1 != null) {
         this.pluginSettings.store(var1.getPlugins());
      }

      return var1;
   }

   private LoadSourceDataFileInput getInputFromUi() {
      SourceFileOption var1 = this.createProjectFileOption();
      SourceFileOption var2 = this.createSelectNewFileOption();
      SourceFileOption[] var3 = new SourceFileOption[]{var1, var2};
      ProjectSourceDataFileInputUi var4 = this.createUi(var3);
      DialogSourceDataFileInputProvider var5 = new DialogSourceDataFileInputProvider(this.window, var4);
      LoadSourceDataFileInput var6 = var5.selectInput();
      this.processSelection(var3, var6);
      return var6;
   }

   private SourceFileOption createProjectFileOption() {
      FileUi[] var1 = this.projectFileStore != null ? this.projectFileStore.recentFiles() : new FileUi[0];
      ProjectFileOption var2 = new ProjectFileOption(var1);
      var2.setActive(var1.length > 0);
      var2.setDeleteAction(new ProjectSourceDataFileInputProvider.DeleteFilesAction(var2));
      return var2;
   }

   private SourceFileOption createSelectNewFileOption() {
      return new SelectNewFileOption(this.window, new TestCenterFileStore(), new BooleanKeyValue(ProjectSourceDataFileInputProvider.class));
   }

   private ProjectSourceDataFileInputUi createUi(SourceFileOption[] var1) {
      PluginSelector var2 = new PluginSelector(this.serverManager, this.pluginSettings.load());
      if (this.ediSource) {
         var2.sourceIsEdi();
      }

      ProjectSourceDataFileInputUi var3 = new ProjectSourceDataFileInputUi(var1, var2, this.ediSource);
      var3.setSelectedOption(this.getInitiallySelectedOption(var1));
      return var3;
   }

   private SourceFileOption getInitiallySelectedOption(SourceFileOption[] var1) {
      for (SourceFileOption var5 : var1) {
         if (var5.isActive()) {
            return var5;
         }
      }

      return var1[var1.length - 1];
   }

   private void processSelection(SourceFileOption[] var1, LoadSourceDataFileInput var2) {
      if (var2 != null) {
         this.updateFileStore(var1, var2);
      }
   }

   private void updateFileStore(SourceFileOption[] var1, LoadSourceDataFileInput var2) {
      for (SourceFileOption var6 : var1) {
         if (var6.isActive()) {
            if (var6.includeSelectionInProject()) {
               this.updateRecentFiles(var2.getFile());
            }
            break;
         }
      }
   }

   private void updateRecentFiles(File var1) {
      if (var1 != null && this.projectFileStore != null) {
         this.projectFileStore.update(var1);
      }
   }

   private final class DeleteFilesAction extends AbstractSingleTypeSelectionAction<FileUi> {
      private final ProjectFileOption option;

      public DeleteFilesAction(ProjectFileOption var2) {
         super(FileUi.class, "Delete");
         this.putValue("SmallIcon", CommonIcons.DELETE_16);
         this.putValue("ShortDescription", "Delete the selected source files from the project");
         this.option = var2;
      }

      @Override
      protected void run(List<FileUi> var1) {
         boolean var2 = false;

         for (FileUi var4 : var1) {
            if (ProjectSourceDataFileInputProvider.this.projectFileStore.remove(var4.getFile())) {
               var2 = true;
            }
         }

         if (var2) {
            this.option.setFiles(ProjectSourceDataFileInputProvider.this.projectFileStore.recentFiles());
         }
      }
   }
}
