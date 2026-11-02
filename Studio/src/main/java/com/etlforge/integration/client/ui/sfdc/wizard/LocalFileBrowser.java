package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.lang.reflect.Constructor;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.integration.client.ui.entity.LastLocalFile;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.lang.KongaClassUtils;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;
import org.etlforge.ui.util.focus.Focusable;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.Property;

public final class LocalFileBrowser<T extends IntegrationEntity & Locatable> implements UiProvider, Disposable {
   private final Class<T> type;
   private final FileSelectorPanel fileSelector;
   private final JComponent ui;
   private T projectItem;
   private LocalFileValidator fileValidator = LocalFileValidator.OK;
   private final LastLocalFile lastLocalFile;

   public static LastLocalFile lastSourceFile() {
      return LastLocalFile.forSource();
   }

   public LocalFileBrowser(Class<T> var1, FileSelectorPanel.Mode var2) {
      this.type = var1;
      this.lastLocalFile = LastLocalFile.forType(var1);
      this.fileSelector = this.createFileSelector(var2);
      this.ui = InvisiblePanel.of(this.fileSelector);
   }

   private FileSelectorPanel createFileSelector(FileSelectorPanel.Mode var1) {
      DefaultFileChooser var2 = new DefaultFileChooser(ApplicationFileChooserFactory.getFactory());
      FileSelectorPanelFactory var3 = FileSelectorFactory.getFactory();
      FileSelectorPanel var4 = var3.singleFilePanel(var2, var1, "Select a local file", this.lastLocalFile.getRecentFileStore());
      var4.setUseTitleLabel(false);
      return var4;
   }

   public void suggestFileName(String var1) {
      ArgCheck.checkNotEmpty(var1);
      File var2 = this.lastLocalFile.getSuggestedParent();
      File var3 = new File(var2, var1);
      this.fileSelector.setSelectedFile(var3);
   }

   public void setFileExtensions(Iterable<String> var1) {
      this.fileSelector.setFileFilter(new SuffixFileChooserFilter(ImmutableList.copyOf(var1)));
   }

   public void setFileValidator(LocalFileValidator var1) {
      this.fileValidator = Preconditions.checkNotNull(var1);
   }

   public boolean isValid() {
      return this.getSelectedFile() != null;
   }

   public File getSelectedFile() {
      return this.fileSelector.getSelectedFile();
   }

   public JComponent getUi() {
      return this.ui;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.fileSelector.requestFocus();
   }

   @Override
   public void dispose() {
      this.fileSelector.dispose();
   }

   public Binding bindWith(Property<File> var1) {
      return this.fileSelector.bindWith(var1);
   }

   public T asProjectItem() {
      File var1 = this.getSelectedFile();
      if (this.projectItem == null) {
         this.createProjectItem();
      }

      if (var1 != null) {
         this.projectItem.setName(var1.getName());
         this.projectItem.setLocation(new LocalFileLocation(var1));
      } else {
         this.projectItem.setLocation(new LocalFileLocation());
      }

      return this.projectItem;
   }

   private void createProjectItem() {
      try {
         Constructor var1 = KongaClassUtils.getSingleArgumentConstructor(this.type, String.class);
         this.projectItem = (T)var1.newInstance("");
         this.projectItem.setTransient(true);
      } catch (Exception var2) {
         Throwables.propagate(var2);
      }
   }

   public String validateLocation() {
      File var1 = this.getSelectedFile();
      return this.fileValidator.validate(var1);
   }

   public Focusable asFocusable() {
      return this.fileSelector;
   }
}
