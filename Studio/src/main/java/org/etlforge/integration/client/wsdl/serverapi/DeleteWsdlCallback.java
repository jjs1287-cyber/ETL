package org.etlforge.integration.client.wsdl.serverapi;

import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;

public interface DeleteWsdlCallback extends ServerCallback {
   void deleteComplete(List<String> var1);
}
