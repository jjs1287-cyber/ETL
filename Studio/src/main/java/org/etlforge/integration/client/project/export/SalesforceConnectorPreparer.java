package org.etlforge.integration.client.project.export;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.project.jitterpack.ExportItemPreparer;
import org.etlforge.integration.client.project.jitterpack.PostExportManager;
import org.etlforge.integration.data.entity.SalesforceConnector;

public final class SalesforceConnectorPreparer extends ExportItemPreparer {
   private final SalesforceConnector connector;

   @ExtensionObjectConstructor
   public SalesforceConnectorPreparer(SalesforceConnector var1) {
      this.connector = var1;
   }

   @Override
   public void prepareItem(PostExportManager var1) {
      this.blankOutPassword(this.connector, "password", var1);
      this.blankOutPassword(this.connector, "security_token", var1); this.blankOutPassword(this.connector, "client_secret", var1);
   }
}
