package org.etlforge.integration.ldap.structure;

import java.util.Comparator;
import org.etlforge.integration.structure.crom.CROM;

public interface LdapToCromConverter {
   CROM toCrom(LdapStructureEntryNode var1, AttributeFilter var2);

   void setAttributeOrder(Comparator<LdapStructureAttribute> var1);
}
