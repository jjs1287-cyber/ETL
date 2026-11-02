package org.etlforge.integration.client.ui.structure.editor.ldap;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.data.structure.LdapStructure;

public interface LdapStructureWizardPage extends WizardPage {
   void applyTo(LdapStructure var1);
}
