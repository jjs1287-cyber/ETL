package org.etlforge.integration.client.ui.jitterpack;

import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.event.UndoableEditListener;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.integration.client.ui.jitterpack.resources.JitterpackResources;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanelException;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.util.preference.PreferencesKey;

final class JitterPackBrowser implements JitterPackLocatorUi {
   private final FileSelectorPanel locationBrowser;
   private boolean enabled = true;
   private final JitterPackSelectionListeners selectionListeners = new JitterPackSelectionListeners();

   public JitterPackBrowser(ApplicationWindow var1) {
      this.locationBrowser = this.createSelectorPanel(var1);
   }

   private FileSelectorPanel createSelectorPanel(ApplicationWindow var1) {
      FileSelectorPanelFactory var2 = FileSelectorFactory.getFactory();
      FileChooser var3 = ApplicationFileChooser.getInstance(var1);
      RecentFileStore var4 = this.getRecentFileStore();
      FileSelectorPanel var5 = var2.recentFilesPanel(var3, FileSelectorPanel.OPEN, Strings.getJitterPackString("Import.Browser.Title"), var4);
      var5.setFileFilter(JitterPackerUi.FILE_FILTER);
      LabelDecorator var6 = TextStyles.DefaultText.asLabelDecorator(JitterpackResources.IMPORT_16);
      var5.decorateTitle(var6);
      var5.addInputChangeListener(new JitterPackBrowser.SelectionNotifier());
      return var5;
   }

   private RecentFileStore getRecentFileStore() {
      PreferencesKey var1 = new PreferencesKey(ExportJitterPackUi.class, "SelectedFile");
      return new PreferencesFileStore(var1, 10);
   }

   @Override
   public JitterPack getJitterPack() {
      File var1 = this.locationBrowser.getSelectedFile();
      return var1 == null ? null : new JitterPack(var1, JitterPackDescriptor.UNKNOWN);
   }

   public void setSelectedJitterPack(JitterPack var1) {
      File var2 = var1 == null ? null : var1.getFile();
      this.locationBrowser.setSelectedFile(var2);
   }

   @Override
   public void addJitterPackSelectionListener(JitterPackSelectionListener var1) {
      this.selectionListeners.add(var1);
   }

   @Override
   public void removeJitterPackSelectionListener(JitterPackSelectionListener var1) {
      this.selectionListeners.remove(var1);
   }

   public void addActionListener(ActionListener var1) {
      this.locationBrowser.addActionListener(var1);
   }

   public void removeActionListener(ActionListener var1) {
      this.locationBrowser.removeActionListener(var1);
   }

   @Override
   public boolean isEnabled() {
      return this.enabled;
   }

   @Override
   public void setEnabled(boolean var1) {
      this.enabled = var1;
      this.locationBrowser.setEnabled(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.locationBrowser.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.locationBrowser.requestFocus();
   }

   public void decorateFields() {
      this.locationBrowser.decorateFields();
   }

   public void dispose() {
      this.locationBrowser.dispose();
   }

   public boolean isInputValid() {
      return this.locationBrowser.isInputValid();
   }

   public void setActive(boolean var1) {
      this.locationBrowser.setActive(var1);
   }

   public void setSendInputChangeEvents(boolean var1) {
      this.locationBrowser.setSendInputChangeEvents(var1);
   }

   public void addInputChangeListener(InputChangeListener var1) {
      this.locationBrowser.addInputChangeListener(var1);
   }

   public void removeInputChangeListener(InputChangeListener var1) {
      this.locationBrowser.removeInputChangeListener(var1);
   }

   public void setSendUndoEvents(boolean var1) {
      this.locationBrowser.setSendUndoEvents(var1);
   }

   public void addUndoableEditListener(UndoableEditListener var1) {
      this.locationBrowser.addUndoableEditListener(var1);
   }

   public void removeUndoableEditListener(UndoableEditListener var1) {
      this.locationBrowser.removeUndoableEditListener(var1);
   }

   public void validateInput() throws InputPanelException {
      this.locationBrowser.validateInput();
   }

   private final class SelectionNotifier implements InputChangeListener {
      private SelectionNotifier() {
      }

      @Override
      public void inputChanged(InputChangeEvent var1) {
         JitterPackBrowser.this.selectionListeners.call().selectionChanged(JitterPackBrowser.this.getJitterPack());
      }
   }
}

