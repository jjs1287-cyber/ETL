package org.etlforge.integration.client.ui.connector.sap;

import org.etlforge.integration.data.connector.SapEndpoint;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class SapEndpointToolTip extends IntegrationEntityToolTip {
   public SapEndpointToolTip(SapEndpoint var1) {
      super(var1);
      this.addConnectionParams(var1);
   }

   private void addConnectionParams(SapEndpoint var1) {
      EndpointParameters var2 = var1.getConnectionParameters();
      if (var2 != null) {
         this.addEntry("Host", var2.getServer().getHost());
         this.addEntry("User", var2.getUserCredentials().getUsername());
      }
   }
}
