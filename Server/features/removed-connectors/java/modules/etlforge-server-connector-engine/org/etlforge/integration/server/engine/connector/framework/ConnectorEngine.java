package org.etlforge.integration.server.engine.connector.framework;

import java.util.List;

public interface ConnectorEngine {
   ConnectorObjectList getConnectorObjectList(List<String> var1, String var2);

   ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1, List<String> var2);

   void executeConnectorFileUtf8(List<String> var1, String var2, String var3);
}
