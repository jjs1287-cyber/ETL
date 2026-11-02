package org.etlforge.integration.client.mapping.serverapi.ldap;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;

public interface GetLdapStructuresCallback extends ServerCallback {
   void structuresRetrieved(ObjectClassStructure[] var1);
}
