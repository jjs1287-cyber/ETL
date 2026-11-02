package org.etlforge.integration.client.ui.interchange.entity.document.wizard;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.DefaultComponentAction;

final class TreeMapperWizardStartPageUi implements UiProvider {
   private final TreeMapperWizardStartPage page;
   private final DestinationStructureDefiner sourceDefiner;
   private final DestinationStructureDefiner targetDefiner;
   private boolean isSourceAvailable;
   private boolean isTargetAvailable;

   public TreeMapperWizardStartPageUi(TreeMapperWizardStartPage var1, IntegrationEntity var2) {
      this.page = var1;
      this.isSourceAvailable = true;
      this.isTargetAvailable = true;
      ChangeListener var3 = new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent var1) {
            TreeMapperWizardStartPageUi.this.updateNavigationEnabledState();
         }
      };
      this.sourceDefiner = this.createSourceDefiner(var2, var3);
      this.targetDefiner = this.createTargetDefiner(var2, var3);
   }

   private DestinationStructureDefiner createSourceDefiner(IntegrationEntity var1, ChangeListener var2) {
      return new DestinationStructureDefiner("Create document from &source structure", false, var1, var2);
   }

   private DestinationStructureDefiner createTargetDefiner(IntegrationEntity var1, ChangeListener var2) {
      return new DestinationStructureDefiner("Create document from &target structure", false, var1, var2);
   }

   public void setSourceStructureConversionNotSupported(String var1) {
      this.isSourceAvailable = false;
      this.sourceDefiner.setNotAvailable(var1);
   }

   public void setTargetStructureConversionNotSupported(String var1) {
      this.isTargetAvailable = false;
      this.targetDefiner.setNotAvailable(var1);
   }

   public void setInitialSelection() {
      if (this.isSourceAvailable && !this.isTargetAvailable) {
         this.sourceDefiner.select();
      } else if (this.isTargetAvailable && !this.isSourceAvailable) {
         this.targetDefiner.select();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.createDisplay());
   }

   private JComponent createDisplay() {
      JPanel var1 = new OneColumnPanel().nonOpaque().withInsets(5, 5, 20, 5).of(this.sourceDefiner, this.targetDefiner).invisible();
      this.installKeyBindings(var1);
      return var1;
   }

   private void installKeyBindings(JComponent var1) {
      DefaultComponentAction.whenAncestorOfFocusOwner(var1, new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            TreeMapperWizardStartPageUi.this.page.getWizard().nextPageRequested();
         }
      });
   }

   @Override
   public void requestFocus() {
      if (this.isSourceAvailable) {
         this.sourceDefiner.requestFocus();
      } else if (this.isTargetAvailable) {
         this.targetDefiner.requestFocus();
      }
   }

   public boolean doSourceStructureConversion() {
      return doStructureConversion(this.sourceDefiner);
   }

   public boolean doTargetStructureConversion() {
      return doStructureConversion(this.targetDefiner);
   }

   private static boolean doStructureConversion(DestinationStructureDefiner var0) {
      return var0.isActivated() && var0.isValid();
   }

   public void applyToSourceStructure(TextStructure var1) {
      applyToStructure(var1, this.sourceDefiner);
   }

   public void applyToTargetStructure(TextStructure var1) {
      applyToStructure(var1, this.targetDefiner);
   }

   private static void applyToStructure(TextStructure var0, DestinationStructureDefiner var1) {
      assert var1.isActivated() && var1.isValid();
      var1.contribute(var0);
   }

   public void contributeToSourceDocument(TextDocument var1) {
      this.contributeToDocument(var1, this.sourceDefiner);
   }

   public void contributeToTargetDocument(TextDocument var1) {
      this.contributeToDocument(var1, this.targetDefiner);
   }

   private void contributeToDocument(TextDocument var1, DestinationStructureDefiner var2) {
      assert var2.isActivated() && var2.isValid();
      var2.contribute(var1);
   }

   private void updateNavigationEnabledState() {
      this.page.setCanContinue(this.isInputValid());
   }

   private boolean isInputValid() {
      String var1 = null;
      if (this.sourceDefiner.isActivated()) {
         if (!this.sourceDefiner.isValid()) {
            return false;
         }

         var1 = this.sourceDefiner.getName();
      }

      String var2 = null;
      if (this.targetDefiner.isActivated()) {
         if (!this.sourceDefiner.isValid()) {
            return false;
         }

         var2 = this.targetDefiner.getName();
      }

      return (var1 != null || var2 != null) && KongaStringUtils.stringsDiffer(var1, var2);
   }
}
