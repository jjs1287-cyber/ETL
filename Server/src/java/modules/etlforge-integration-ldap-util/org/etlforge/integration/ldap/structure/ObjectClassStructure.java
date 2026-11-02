package org.etlforge.integration.ldap.structure;

import org.etlforge.util.name.Nameable;

public interface ObjectClassStructure extends UniqueLdapEntity, Nameable {
   @Override
   NumericOid getOid();

   @Override
   String getName();

   LdapStructureAttribute[] getAttributes();
}
