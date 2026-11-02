package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardNavigationKeyBindings;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.NoStructure;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.util.name.IllegalNameException;

final class TransformationWizardStartPage extends AbstractWizardPage {
   private final StartPageUi ui;

   public TransformationWizardStartPage(TransformationWizard var1, Transformation var2, Folder var3) {
      super(var1, Strings.get("StartPage.SubTitle"));
      this.ui = this.createUi(var2, var3);
      this.ui.setItemLookup(var1.getItemLookup());
      this.setCanContinue(this.ui.isInputValid());
      this.ui.installUndoRedo(this.getUndoRedoInstaller());
   }

   public TransformationWizard getWizard() {
      return (TransformationWizard)super.getWizard();
   }

   public WizardType getSourceType() {
      return this.ui.getSourceType();
   }

   public WizardType getTargetType() {
      return this.ui.getTargetType();
   }

   public boolean isNoneSelectedAsSource() {
      return this.getSourceType() == WizardTypes.NONE;
   }

   public boolean isHostedWebService() {
      WizardType var1 = this.getSourceType();
      WizardType var2 = this.getTargetType();
      return var1 == WizardTypes.WEBSERVICE_REQUEST && var2 == WizardTypes.WEBSERVICE_RESPONSE;
   }

   public void applyToTransformation(Transformation var1) {
      this.applyName(var1);
      if (this.getSourceType() == WizardTypes.NONE) {
         var1.setInputStructure(new NoStructure());
      }

      if (this.getTargetType() == WizardTypes.NONE) {
         var1.setOutputStructure(new NoStructure());
      }
   }

   private void applyName(Transformation var1) {
      try {
         var1.setName(this.ui.getTransformationName());
      } catch (IllegalNameException var3) {
         this.getWizard().handlePageException("Invalid name.", var3);
      }
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   public boolean prepareForForwardNavigation() {
      this.updateWizardTitle();
      return true;
   }

   private StartPageUi createUi(Transformation var1, Folder var2) {
      WizardConfiguration var3 = this.getWizard().getConfiguration();
      final StartPageUi var4 = new StartPageUi(var1, var2, var3.getSupportedInputTypes(), var3.getSupportedOutputTypes());
      var4.addChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            TransformationWizardStartPage.this.setCanContinue(var4.isInputValid());
            TransformationWizardStartPage.this.getWizard().updateNavigationControls();
         }
      });
      var4.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            TransformationWizardStartPage.this.getWizard().nextPageRequested();
         }
      });

      for (JComponent var8 : var4.getKeyBindingTargets()) {
         WizardNavigationKeyBindings.install(this.getWizard(), var8);
      }

      return var4;
   }

   private void updateWizardTitle() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TransformationWizardStartPage.this.getWizard().setTitle(TransformationWizardStartPage.this.ui.getTransformationName());
         }
      });
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Transformation Wizard", "");
      var1.add(this.ui, true, false);
      return var1.asNorthBorderLayout();
   }
}
