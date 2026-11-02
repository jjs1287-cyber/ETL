package org.etlforge.integration.client.ldap.ui;

import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionSource;

public interface AttributeViewer extends UiProvider {
   LdapStructureAttribute[] getSelectedAttributes();

   SelectionSource asSelectionSource();

   void setDragEnabled();
}
