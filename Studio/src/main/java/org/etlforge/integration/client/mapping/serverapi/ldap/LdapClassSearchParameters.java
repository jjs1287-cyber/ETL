package org.etlforge.integration.client.mapping.serverapi.ldap;

import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.ldap.LdapConnectionParameters;

public interface LdapClassSearchParameters {
   IntegrationEntityId getSourceOrTargetId();

   LdapConnectionParameters getConnectionParameters();

   String getSearchString();

   boolean skipObsoleteClasses();

   boolean skipAbstractClasses();
}
