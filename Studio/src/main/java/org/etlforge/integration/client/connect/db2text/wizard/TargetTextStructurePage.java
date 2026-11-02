package org.etlforge.integration.client.connect.db2text.wizard;

import com.google.common.base.Supplier;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.connect.text.TextTargetPageUi;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

final class TargetTextStructurePage extends AbstractWizardPage {
   private final TextTargetPageUi<DatabaseStructure> ui;

   public TargetTextStructurePage(DatabaseToTextWizard var1, EntityExplorerSupport var2) {
      super(var1, "Target Text Structure");
      this.ui = new TextTargetPageUi<>(
         var2,
         new TableToTextStructureFactory(var1.getFolders()),
         new EditorPanel(var1.getWindow()),
         "ETLForge can create a Text Structure that matches your source table."
      );
      this.ui.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            TargetTextStructurePage.this.updateCanContinue();
         }
      });
      this.updateCanContinue();
   }

   private void updateCanContinue() {
      this.setCanContinue(this.ui.isInputValid());
   }

   public void setSourceStructure(DatabaseStructure var1) {
      this.ui.setStructureFactoryInput(var1);
   }

   public TextDocument getSelectedDocument() {
      return this.ui.getSelectedDocument();
   }

   @Override
   public boolean prepareForForwardNavigation() {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            return TargetTextStructurePage.this.ui.allowsRemoval();
         }
      });
   }

   @Override
   protected void dispose() {
      this.ui.dispose();
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Select A Text Structure", "Select an existing Text Document or create a new one:");
      var1.add(this.ui, true, true);
      JPanel var2 = var1.asCenterBorderLayout();
      this.getUndoRedoInstaller().installIntoComponentTree(var2);
      return var2;
   }
}
