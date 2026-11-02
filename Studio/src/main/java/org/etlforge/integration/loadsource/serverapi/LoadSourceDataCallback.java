package org.etlforge.integration.loadsource.serverapi;

import org.etlforge.integration.client.server.ServerCallback;

public interface LoadSourceDataCallback extends ServerCallback {
   void sourceDataTreeGenerated(LoadSourceDataResult var1);
}
