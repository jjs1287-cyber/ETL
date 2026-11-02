package org.etlforge.integration.client.ui.interchange.location.ldap;

import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanelPart;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.ui.layout.LayoutContainer;

interface LdapLocationPanelPart extends DataLocationPanelPart<LdapLocation> {
   void layout(LayoutContainer var1);
}
