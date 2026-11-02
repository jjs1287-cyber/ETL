package org.etlforge.application.ui.prefs;

import com.google.common.base.Objects;
import java.io.File;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.prefs.FilePreference;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.FileSelectorPanel;

public class FilePreferenceUi implements PreferenceUi {
   private final FilePreference preference;
   private final FileSelectorPanel fileBrowser;
   private final String label;

   public FilePreferenceUi(FilePreference var1, String var2) {
      this.preference = var1;
      this.fileBrowser = FileSelectorFactory.getFactory().singleFilePanel(null, FileSelectorPanel.OPEN, var2, null);
      this.label = var2;
      this.fileBrowser.setUseTitleLabel(false);
      this.restore();
   }

   public final void setSelectionMode(FileChooser.SelectionMode var1) {
      this.fileBrowser.setSelectionMode(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Horizontal.box().centerAligned().add(this.label + ":").rigid(10).add(this.fileBrowser).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.fileBrowser.requestFocus();
   }

   @Override
   public final void apply() {
      File var1 = this.fileBrowser.getSelectedFile();
      if (this.isValid(var1)) {
         this.preference.set(var1);
      }
   }

   protected boolean isValid(File var1) {
      return true;
   }

   @Override
   public final void restore() {
      this.fileBrowser.setSelectedFile(this.preference.get());
   }

   @Override
   public final void restoreDefault() {
      this.fileBrowser.setSelectedFile(this.preference.getDefaultValue());
   }

   @Override
   public final boolean isDirty() {
      File var1 = this.fileBrowser.getSelectedFile();
      File var2 = this.preference.get();
      return !Objects.equal(var1, var2);
   }

   @Override
   public final void addChangeListener(final ChangeListener var1) {
      this.fileBrowser.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1x) {
            var1.stateChanged(new ChangeEvent(var1x.getSource()));
         }
      });
   }

   @Override
   public void share() {
   }

   protected final FilePreference getPreference() {
      return this.preference;
   }

   protected final FileSelectorPanel getFileBrowser() {
      return this.fileBrowser;
   }

   protected final String getLabel() {
      return this.label;
   }
}
