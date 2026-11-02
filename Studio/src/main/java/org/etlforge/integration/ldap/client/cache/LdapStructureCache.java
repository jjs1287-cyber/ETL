package org.etlforge.integration.ldap.client.cache;

import org.etlforge.application.cache.DataCache;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;

public interface LdapStructureCache extends DataCache {
   String ID = "org.etlforge.integration.client.component.ldap.cache.LdapStructureCache";

   ObjectClassDefinition[] getObjectClasses();

   ObjectClassDefinition[] getObjectClasses(NumericOid[] var1);

   void storeObjectClasses(ObjectClassDefinition[] var1);

   String getObjectClassName(NumericOid var1);

   ObjectClassStructure getClassStructure(NumericOid var1);

   void storeClassStructures(ObjectClassStructure[] var1);
}
