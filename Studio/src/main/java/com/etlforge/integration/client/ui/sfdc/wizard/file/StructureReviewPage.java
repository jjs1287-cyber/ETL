package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.entity.wizard.EditorPanelService;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.ui.UiThreadSupplier;

final class StructureReviewPage extends AbstractWizardPage {
   private final TextDocument document;
   private final EditorPanelService displayer;

   public StructureReviewPage(SalesforceWizard<?> var1, TextDocument var2) {
      super(var1, "Review " + EntityType.Document.getDisplayName());
      this.document = var2;
      EditorPanel var3 = new EditorPanel(var1.getWindow());
      var3.setToolbarMode(EditorPanel.ToolbarMode.NONE);
      this.displayer = new EditorPanelService(var1.getProjectExplorer(), var3);
      this.setCanContinue(true);
   }

   @Override
   protected void onFirstDisplay() {
      this.displayer.openEditorFor(this.document);
   }

   @Override
   protected void dispose() {
      this.displayer.forceCloseWithoutSaving();
   }

   @Override
   public boolean prepareForForwardNavigation() {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            return StructureReviewPage.this.displayer.saveCurrentEditor();
         }
      });
   }

   @Override
   public boolean prepareForBackwardNavigation() {
      return this.displayer.prepareForRemoval(false);
   }

   @Override
   public void requestFocus() {
      this.displayer.requestFocus();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getTitle());
      var1.add(this.displayer, true, true);
      return var1.asCenterBorderLayout();
   }
}
