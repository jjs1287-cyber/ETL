package org.etlforge.integration.client.ui.structure.editor.ldap;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ldap.ui.SelectedObjectClasses;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.structure.LdapStructureEntry;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;

public final class ClassStructureReviewPage extends AbstractWizardPage implements LdapStructureWizardPage {
   private final ClassStructureReviewModel model;
   private final ClassStructureReviewUi ui;

   public ClassStructureReviewPage(Wizard var1, ClassStructureReviewModel var2) {
      super(var1, PackageResources.Wizard.StructureReviewer.TITLE);
      this.model = var2;
      this.ui = new ClassStructureReviewUi(this, var2);
      this.setHelpId("integration.transformation.wizard.ldap");
      this.setCanContinue(true);
      this.layoutPage();
   }

   public void setObjectClasses(SelectedObjectClasses var1) {
      this.model.setSelectedClasses(var1);
   }

   @Override
   public void applyTo(LdapStructure var1) {
      this.ui.applyTo(var1);
   }

   public LdapStructureEntry[] getDefinedEntries() {
      return this.model.getTreeModel().getDefinedEntries();
   }

   public LdapStructureEntryNode[] getDefinedEntryNodes() {
      return this.model.getTreeModel().getDefinedEntryNodes();
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(
         PackageResources.Wizard.StructureReviewer.MAIN_CAPTION, PackageResources.Wizard.StructureReviewer.SUB_CAPTION
      );
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }
}
