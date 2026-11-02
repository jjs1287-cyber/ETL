package org.etlforge.ui.util.file;

import java.io.File;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class FileSelectorBinding extends AbstractBinding<File> {
   private final FileSelectorPanel selector;
   private final FileSelectorBinding.UiListener uiListener = new FileSelectorBinding.UiListener();

   public FileSelectorBinding(FileSelectorPanel var1, AbstractPropertyBasedModel var2, String var3) {
      super(var2, var3);
      this.selector = var1;
      var1.addFileSelectionListener(this.uiListener);
   }

   public FileSelectorBinding(FileSelectorPanel var1, Property<File> var2) {
      super(var2);
      this.selector = var1;
      var1.addFileSelectionListener(this.uiListener);
   }

   protected void updateUiWithValue(File var1) {
      this.selector.setSelectedFile(var1);
   }

   protected File getValueFromUi() {
      return this.selector.getSelectedFile();
   }

   @Override
   protected void disposeUiListener() {
      this.selector.removeFileSelectionListener(this.uiListener);
   }

   private class UiListener implements FileSelectionListener {
      private UiListener() {
      }

      @Override
      public void selectionChanged(File var1) {
         if (FileSelectorBinding.this.isUiToModelEnabled()) {
            FileSelectorBinding.this.syncModel();
         }
      }
   }
}
