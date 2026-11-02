package org.etlforge.integration.client.ui.jitterpack;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.widget.ApplicationToolBar;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorIdGenerator;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.integration.client.project.jitterpack.Exporter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.button.KongaButton;

public class ExportJitterPackPage extends GenericEditor {
   private final IntegrationProject project;
   private final ExportJitterPackUi ui;
   private final Action exportAction;

   public ExportJitterPackPage(IntegrationProject var1, ApplicationWindow var2, ExportJitterPackUi var3, Exporter var4) {
      super(EditorIdGenerator.get(ExportJitterPackPage.class), "Export Backup", var1.getName());
      Preconditions.checkNotNull(var2, "appWin");
      Preconditions.checkNotNull(var3, "ui");
      this.project = var1;
      this.ui = var3;
      this.exportAction = new DoExportAction(var2, var3, var4, null, null);
      this.createToolBarButtons();
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.prepareForDisplay();
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      if (super.removeFromWindow(var1)) {
         this.ui.dispose();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public Object getObject() {
      return this.project;
   }

   private void createToolBarButtons() {
      ArrayList var1 = new ArrayList();
      var1.add(this.createExportButtonForToolBar());
      var1.add(null);
      this.setToolbarButtons(var1);
   }

   private JButton createExportButtonForToolBar() {
      KongaButton var1 = new KongaButton(this.exportAction);
      ApplicationToolBar.configureTextOnlyButton(var1, true);
      return var1;
   }

   private void prepareForDisplay() {
      this.setupUi();
      this.setMainPanel(this.createMainPanel());
      this.createTitlePanel();
      this.setToolBar(this.createToolBar());
   }

   private void setupUi() {
      this.ui.setDoExportAction(this.exportAction);
      this.ui.setSendUndoEvents(false);
      this.ui.setSendInputChangeEvents(true);
      this.ui.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            ExportJitterPackPage.this.updateExportActionEnabledState();
         }
      });
      this.ui.addUndoableEditListener(this);
      this.updateExportActionEnabledState();
   }

   private void updateExportActionEnabledState() {
      this.exportAction.setEnabled(this.ui.isInputValid());
   }

   private JComponent createMainPanel() {
      JPanel var1 = BorderLayoutBuilder.fillWith(this.ui);
      var1.setOpaque(false);
      return var1;
   }
}

