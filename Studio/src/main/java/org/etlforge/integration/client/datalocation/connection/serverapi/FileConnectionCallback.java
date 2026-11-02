package org.etlforge.integration.client.datalocation.connection.serverapi;

import java.util.List;
import javax.annotation.Nullable;
import org.etlforge.integration.client.server.ServerCallback;

public interface FileConnectionCallback extends ServerCallback {
   void connected(@Nullable List<String> var1);
}
