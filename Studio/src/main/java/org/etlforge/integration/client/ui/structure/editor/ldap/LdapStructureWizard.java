package org.etlforge.integration.client.ui.structure.editor.ldap;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.structure.LdapStructure;

public final class LdapStructureWizard extends ApplicationWindowWizard<LdapStructure> {
   private LdapStructure definedStructure;

   private LdapStructureWizard(ApplicationWindow var1) {
      super(var1);
      this.setTitle(PackageResources.Wizard.TITLE);
   }

   public static LdapStructureWizard newTargetWizard(LdapStructure var0, EntityExplorerSupport var1) {
      LdapStructureWizard var2 = new LdapStructureWizard(var1.getWindow());
      var2.setPageChain(LdapStructurePageChain.newEditTargetChain(var2, var0, var1));
      return var2;
   }

   public static LdapStructureWizard newSourceWizard(LdapStructure var0, EntityExplorerSupport var1) {
      LdapStructureWizard var2 = new LdapStructureWizard(var1.getWindow());
      var2.setPageChain(LdapStructurePageChain.newEditSourceChain(var2, var0, var1));
      return var2;
   }

   @Override
   protected boolean onWizardCancelled() {
      return true;
   }

   protected LdapStructure finishWizard() {
      this.definedStructure = ((LdapStructurePageChain)this.getPageChain()).getDefinedStructure();
      return this.definedStructure;
   }

   public LdapStructure getDefinedStructure() {
      return this.definedStructure;
   }
}
