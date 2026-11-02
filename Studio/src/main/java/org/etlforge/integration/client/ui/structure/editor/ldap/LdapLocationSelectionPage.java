package org.etlforge.integration.client.ui.structure.editor.ldap;

import org.etlforge.integration.data.location.LdapLocation;

public interface LdapLocationSelectionPage extends LdapStructureWizardPage {
   void setSelectedLdapLocation(LdapLocation var1);

   LdapLocation getSelectedLdapLocation();
}
