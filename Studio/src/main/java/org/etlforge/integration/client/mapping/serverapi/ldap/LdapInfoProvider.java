package org.etlforge.integration.client.mapping.serverapi.ldap;

import org.etlforge.integration.client.mapping.serverapi.GetMappingStructureCallback;
import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.structure.LdapStructure;

public interface LdapInfoProvider extends ServerCall {
   void getLdapMappingStructure(LdapStructure var1, boolean var2, GetMappingStructureCallback var3);

   void getObjectClasses(LdapClassSearchParameters var1, GetObjectClassesCallback var2);

   void getLdapStructures(LdapStructureSearchParameters var1, GetLdapStructuresCallback var2);
}
