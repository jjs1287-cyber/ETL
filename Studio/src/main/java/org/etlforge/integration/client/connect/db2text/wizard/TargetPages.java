package org.etlforge.integration.client.connect.db2text.wizard;

import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.client.ui.entity.wizard.NewFileLocationCallback;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.functors.FileBasedLocationFilter;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.TextStructure;

final class TargetPages extends WizardPageList {
   private final TargetTextStructurePage documentSelectionPage;
   private final EntitySelectionPage<Target> targetSelectionPage;

   public TargetPages(DatabaseToTextWizard var1, EntityExplorerSupport var2) {
      this.documentSelectionPage = new TargetTextStructurePage(var1, var2);
      this.targetSelectionPage = this.createTargetSelectionPage(var1, var2);
      this.addPage(this.documentSelectionPage);
      this.addPage(this.targetSelectionPage);
   }

   private EntitySelectionPage<Target> createTargetSelectionPage(ApplicationWindowWizard<?> var1, EntityExplorerSupport var2) {
      EntitySelectionPage var3 = new EntitySelectionPage<>(var1, "Select a Target", var2, EntityType.Target, Target.class, new FileBasedLocationFilter(true));
      var3.setNewEntityCallback(new NewFileLocationCallback());
      var3.setMainCaption("Select a File Target");
      var3.setSubCaption("Select the location of the target file that will be used in this connect operation.");
      return var3;
   }

   public void setSourceStructure(DatabaseStructure var1) {
      this.documentSelectionPage.setSourceStructure(var1);
   }

   public TextStructure getTextStructure() {
      TextDocument var1 = this.getTextDocument();
      return var1 != null ? var1.getDataStructure() : null;
   }

   public TextDocument getTextDocument() {
      return this.documentSelectionPage.getSelectedDocument();
   }

   public Target getTarget() {
      return this.targetSelectionPage.getSelectedEntity();
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.targetSelectionPage;
   }
}
