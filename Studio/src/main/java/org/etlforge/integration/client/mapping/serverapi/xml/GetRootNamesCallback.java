package org.etlforge.integration.client.mapping.serverapi.xml;

import java.util.Collection;
import org.etlforge.integration.client.server.ServerCallback;

public interface GetRootNamesCallback extends ServerCallback {
   void rootNamesRetrieved(Collection<String> var1);
}
