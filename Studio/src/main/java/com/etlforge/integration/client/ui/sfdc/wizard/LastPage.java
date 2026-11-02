package com.etlforge.integration.client.ui.sfdc.wizard;

import com.etlforge.integration.client.sfdc.data.ObjectSummary;
import java.awt.EventQueue;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;

public final class LastPage<T extends IntegrationEntity & SalesforceWizardEntity> extends AbstractWizardPage {
   private final LastPageUi<T> ui;

   public LastPage(Wizard<T> var1, T var2, Folder var3) {
      super(var1, "Summary");
      this.ui = new LastPageUi<>((T)var2, var3);
      this.ui.addValidationListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            LastPage.this.updateCanContinue();
         }
      });
      this.updateCanContinue();
   }

   private void updateCanContinue() {
      this.setCanContinue(this.ui.isValid());
   }

   public void setWizardName(String var1) {
      this.ui.setName(var1);
   }

   public void setParent(Folder var1) {
      this.ui.setParent(var1);
   }

   public void setAutoMapperChoice(AutoMapperChoice var1) {
      this.ui.withAutoMaperChoice(var1);
   }

   public void setObjectSummary(ObjectSummary<T> var1) {
      this.ui.setObjectSummary(var1);
   }

   public boolean isAutoMapSelected() {
      return this.ui.isAutoMapSelected();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(null);
      var1.add(this.ui, true, true);
      JPanel var2 = var1.asCenterBorderLayout();
      this.getUndoRedoInstaller().installIntoComponentTree(var2);
      return var2;
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LastPage.this.ui.requestFocus();
         }
      });
   }
}
