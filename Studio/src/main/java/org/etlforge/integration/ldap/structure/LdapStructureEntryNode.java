package org.etlforge.integration.ldap.structure;

import java.util.List;
import org.etlforge.util.name.Nameable;

public interface LdapStructureEntryNode extends Nameable {
   LdapStructureEntry getEntry();

   @Override
   String getName();

   List<LdapStructureAttribute> getAttributes();

   List<LdapStructureAttribute> getSelectedAttributes();
}
