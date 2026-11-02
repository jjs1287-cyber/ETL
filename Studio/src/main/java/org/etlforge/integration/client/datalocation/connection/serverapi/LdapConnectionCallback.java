package org.etlforge.integration.client.datalocation.connection.serverapi;

import org.etlforge.integration.client.server.ServerCallback;

public interface LdapConnectionCallback extends ServerCallback {
   void connected();
}
