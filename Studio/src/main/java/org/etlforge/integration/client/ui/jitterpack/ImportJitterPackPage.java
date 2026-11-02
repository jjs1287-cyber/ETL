package org.etlforge.integration.client.ui.jitterpack;

import com.google.common.base.Preconditions;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.widget.ApplicationToolBar;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorIdGenerator;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.integration.client.project.jitterpack.ImporterFactory;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.UnpackCallback;
import org.etlforge.integration.client.ui.jitterpack.resources.JitterpackResources;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.file.FileSelectionListener;
import org.etlforge.ui.widget.button.KongaButton;

public class ImportJitterPackPage extends GenericEditor {
   private final ImportJitterPackUi ui;
   private final DoImportAction importAction;

   public ImportJitterPackPage(ImportJitterPackUi var1, ImporterFactory var2, UnpackCallback var3) {
      super(EditorIdGenerator.get(ImportJitterPackPage.class), Strings.getJitterPackString("Import.Title"), Strings.get("Import.SubTitle"));
      Preconditions.checkNotNull(var1, "ui");
      Preconditions.checkNotNull(var3, "unpackCallback");
      this.ui = var1;
      this.importAction = new DoImportAction(var1, var2, var3);
      this.importAction.setWaitService(this.getWaitService());
      this.setIcon(JitterpackResources.IMPORT_16);
      this.setHelpId("Import_Jitterpak");
   }

   public final void reset() {
      this.ui.reset();
   }

   @Override
   public final void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.prepareForDisplay();
   }

   @Override
   public final boolean removeFromWindow(boolean var1) {
      boolean var2 = super.removeFromWindow(var1);
      if (var2) {
         this.ui.dispose();
      }

      return var2;
   }

   @Override
   public final void requestFocus() {
      this.ui.requestFocus();
   }

   private JButton createImportButtonForToolBar() {
      KongaButton var1 = new KongaButton(this.importAction);
      ApplicationToolBar.configureTextOnlyButton(var1, true);
      Fonts.convertToBold(var1);
      return var1;
   }

   private void prepareForDisplay() {
      this.setupUi();
      this.setMainPanel(this.createMainPanel());
      this.createTitlePanel();
      this.setToolbarButtons(this.createToolBarButtons());
      this.setToolBar(this.createToolBar());
   }

   protected List<JButton> createToolBarButtons() {
      ArrayList var1 = new ArrayList();
      var1.add(this.createImportButtonForToolBar());
      var1.add(null);
      return var1;
   }

   private void setupUi() {
      this.ui.setDoImportAction(this.importAction);
      this.ui.addJitterPackSelectionListener(new JitterPackSelectionListener() {
         @Override
         public void selectionChanged(JitterPack var1) {
            ImportJitterPackPage.this.updateImportActionEnabledState();
         }
      });
      this.ui.addDestinationFolderListener(new FileSelectionListener() {
         @Override
         public void selectionChanged(File var1) {
            ImportJitterPackPage.this.updateImportActionEnabledState();
         }
      });
      this.updateImportActionEnabledState();
   }

   private void updateImportActionEnabledState() {
      this.importAction.setEnabled(this.ui.isInputValid());
   }

   private JComponent createMainPanel() {
      JPanel var1 = BorderLayoutBuilder.fillWith(this.ui);
      var1.setOpaque(false);
      return var1;
   }
}

