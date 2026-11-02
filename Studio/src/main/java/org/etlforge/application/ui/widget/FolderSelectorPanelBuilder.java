package org.etlforge.application.ui.widget;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.FileUi;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.util.preference.PreferencesKey;

public final class FolderSelectorPanelBuilder {
   private PreferencesKey prefKey;
   private FileSelectorPanel.Mode mode = FileSelectorPanel.Mode.OPEN;
   private File defaultFolder;
   private String defaultFileName;
   private String title;
   private boolean useTitleLabel;

   public FileSelectorPanel buildPanel(ApplicationWindow var1) {
      FileSelectorPanelFactory var2 = FileSelectorFactory.getFactory();
      RecentFileStore var3 = this.getRecentFileStore();
      FileSelectorPanel var4 = var2.recentFilesPanel(ApplicationFileChooser.getInstance(var1), this.mode, this.title, var3);
      var4.setSelectionMode(FileChooser.DIRECTORIES_ONLY);
      File var5 = var4.getSelectedFile();
      if (var5 == null) {
         this.preSelectDefaultFile(var3, var4);
      }

      var4.setUseTitleLabel(this.useTitleLabel);
      LabelDecorator var6 = TextStyles.DefaultText.asLabelDecorator(ApplicationResources.ICONS.getIcon("FolderClosed.16"));
      var4.decorateTitle(var6);
      return var4;
   }

   private void preSelectDefaultFile(RecentFileStore var1, FileSelectorPanel var2) {
      File var3 = this.getDefaultDestinationFolder(var1);
      if (Strings.isNullOrEmpty(this.defaultFileName)) {
         var2.setSelectedFile(var3);
      } else {
         var2.setSelectedFile(new File(var3, this.defaultFileName));
      }
   }

   public FolderSelectorPanelBuilder setMode(FileSelectorPanel.Mode var1) {
      this.mode = Preconditions.checkNotNull(var1);
      return this;
   }

   public FolderSelectorPanelBuilder setPreferenceKey(PreferencesKey var1) {
      this.prefKey = var1;
      return this;
   }

   public FolderSelectorPanelBuilder setDefaultFolder(File var1) {
      this.defaultFolder = var1;
      return this;
   }

   public FolderSelectorPanelBuilder setDefaultFileName(String var1) {
      this.defaultFileName = var1;
      return this;
   }

   public FolderSelectorPanelBuilder setTitle(String var1) {
      this.title = var1;
      if (StringUtils.isNotEmpty(var1)) {
         this.useTitleLabel = true;
      }

      return this;
   }

   private File getDefaultDestinationFolder(RecentFileStore var1) {
      FileUi var2 = var1.latestFile();
      return var2 != null ? var2.getFile() : this.defaultFolder;
   }

   private RecentFileStore getRecentFileStore() {
      return this.prefKey != null ? new PreferencesFileStore(this.prefKey, 10) : RecentFileStore.EMPTY;
   }
}
