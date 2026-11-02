package org.etlforge.integration.client.mapping.serverapi.ldap;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;

public interface GetObjectClassesCallback extends ServerCallback {
   void objectClassesRetrieved(ObjectClassDefinition[] var1);
}
