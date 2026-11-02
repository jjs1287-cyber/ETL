package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.structure.editor.ldap.LdapStructureWizardPage;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;

public final class SearchParametersDefinitionPage extends AbstractWizardPage implements LdapStructureWizardPage {
   private final SearchParametersDefinitionPageUi ui = this.createUi();

   public SearchParametersDefinitionPage(Wizard var1) {
      super(var1, PackageResources.SearchParamsPage.TITLE);
      this.setCanContinue(true);
      this.setHelpId("integration.transformation.wizard.ldap");
      this.layoutPage();
   }

   @Override
   public void applyTo(LdapStructure var1) {
      this.ui.applyTo(var1);
   }

   public void populate(LdapLocation var1, LdapStructureEntryNode[] var2) {
      this.ui.setStructureEntries(var1, var2);
   }

   public void setStructureToEdit(LdapStructure var1) {
      this.ui.setStructureToEdit(var1);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   protected void dispose() {
      this.ui.dispose();
   }

   private SearchParametersDefinitionPageUi createUi() {
      SearchParametersDefinitionPageUi var1 = new SearchParametersDefinitionPageUi();
      var1.setUndoRedoInstaller(this.getUndoRedoInstaller());
      return var1;
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(PackageResources.SearchParamsPage.MAIN_CAPTION, PackageResources.SearchParamsPage.SUB_CAPTION);
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }
}
