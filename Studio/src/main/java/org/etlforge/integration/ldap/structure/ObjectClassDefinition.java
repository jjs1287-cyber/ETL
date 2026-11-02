package org.etlforge.integration.ldap.structure;

import org.etlforge.util.name.Nameable;

public interface ObjectClassDefinition extends UniqueLdapEntity, Nameable {
   @Override
   NumericOid getOid();

   @Override
   String getName();

   String getDescription();

   ObjectClassType getType();

   boolean isObsolete();

   @Override
   boolean equals(Object var1);
}
