package org.etlforge.integration.client.ui.connector.autodesk.function;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.GetSchemasCallback;
import org.etlforge.integration.data.connector.AutoDeskEndpoint;
import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.integration.etlforgeconnector.SimpleConnectorObject;

public final class SchemaRequestData {
   private final AutoDeskEndpoint endpoint;
   private final AutoDeskFunction.FunctionType type;
   private final String objectName;

   public SchemaRequestData(AutoDeskFunction var1) {
      this(var1.getEndpoint(), var1.getFunctionType(), var1.getWorkspaceNameAndId());
   }

   public SchemaRequestData(AutoDeskEndpoint var1, AutoDeskFunction.FunctionType var2, String var3) {
      this.endpoint = var1;
      this.type = var2;
      this.objectName = var3;
   }

   public void sendRequest(ConnectorCallInfoProvider var1, GetSchemasCallback var2) {
      ArrayList var3 = Lists.newArrayList();
      var3.add(this.type);
      var3.add(new SimpleConnectorObject(this.objectName));
      var1.getSingleSchemas(this.endpoint, var3, var2);
   }

   public String getXsdNamePart() {
      return this.type.name().toLowerCase() + "_" + this.objectName;
   }
}
