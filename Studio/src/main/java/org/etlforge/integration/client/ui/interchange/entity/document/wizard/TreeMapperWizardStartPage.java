package org.etlforge.integration.client.ui.interchange.entity.document.wizard;

import javax.swing.JPanel;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.ui.layout.BorderLayoutBuilder;

final class TreeMapperWizardStartPage extends AbstractWizardPage {
   private final TreeMapperWizardStartPageUi ui;

   public TreeMapperWizardStartPage(TreeMapperToTextStructureWizard var1, IntegrationEntity var2) {
      super(var1, "");
      this.ui = new TreeMapperWizardStartPageUi(this, var2);
   }

   public boolean doSourceStructureConversion() {
      return this.ui.doSourceStructureConversion();
   }

   public boolean doTargetStructureConversion() {
      return this.ui.doTargetStructureConversion();
   }

   public void applyToSourceStructure(TextStructure var1) {
      this.ui.applyToSourceStructure(var1);
   }

   public void applyToTargetStructure(TextStructure var1) {
      this.ui.applyToTargetStructure(var1);
   }

   public void setSourceStructureConversionNotSupported(String var1) {
      this.ui.setSourceStructureConversionNotSupported(var1);
   }

   public void setTargetStructureConversionNotSupported(String var1) {
      this.ui.setTargetStructureConversionNotSupported(var1);
   }

   public void setInitialSelection() {
      this.ui.setInitialSelection();
   }

   @Override
   protected JPanel layoutPage() {
      return BorderLayoutBuilder.fillWith(this.ui);
   }
}
