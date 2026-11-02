package org.etlforge.integration.client.mapping.serverapi.ldap;

import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.ldap.LdapConnectionParameters;
import org.etlforge.integration.ldap.structure.NumericOid;

public interface LdapStructureSearchParameters {
   IntegrationEntityId getSourceOrTargetId();

   LdapConnectionParameters getConnectionParameters();

   NumericOid[] getObjectClasses();

   boolean skipObsoleteAttributes();
}
