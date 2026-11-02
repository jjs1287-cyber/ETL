package org.etlforge.integration.ldap.structure;

import org.etlforge.util.name.Nameable;

public interface LdapStructureAttribute extends UniqueLdapEntity, Nameable {
   @Override
   NumericOid getOid();

   @Override
   String getName();

   String getDescription();

   String getUsageDescription();

   AttributeSyntaxInfo getSyntaxInfo();

   AttributeUse getUse();

   AttributeCardinality getCardinality();

   boolean isReadOnly();
}
