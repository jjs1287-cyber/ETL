package org.etlforge.integration.data.connector;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.project.jitterpack.ExportItemPreparer;
import org.etlforge.integration.client.project.jitterpack.PostExportManager;

public final class ConnectorEndpointPreparer extends ExportItemPreparer {
   private final ConnectorEndpoint endpoint;

   @ExtensionObjectConstructor
   public ConnectorEndpointPreparer(ConnectorEndpoint var1) {
      this.endpoint = var1;
   }

   @Override
   public void prepareItem(PostExportManager var1) {
      this.blankOutPassword(this.endpoint, "password", var1);
   }
}
