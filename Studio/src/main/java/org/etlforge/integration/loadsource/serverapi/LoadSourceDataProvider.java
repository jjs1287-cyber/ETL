package org.etlforge.integration.loadsource.serverapi;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.entity.id.TransformationId;

public interface LoadSourceDataProvider extends ServerCall {
   void loadSourceDataTree(LoadSourceDataInput var1, TransformationId var2, LoadSourceDataCallback var3);
}
