package org.etlforge.integration.client.ui.connector.netsuite.endpoint;

import com.google.common.base.Optional;
import org.etlforge.integration.client.ui.connector.netsuite.function.RefreshNetSuiteFunctionJobFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequenceFactory;

public class NetSuiteVersionChangeSaveConseqenceFactory implements SaveConsequenceFactory {
   private RefreshNetSuiteFunctionJobFactory refreshJobFactory = null;
   private final NetSuiteEndpointConfig endpointConfig;

   public NetSuiteVersionChangeSaveConseqenceFactory(NetSuiteEndpointConfig var1) {
      this.endpointConfig = var1;
   }

   @Override
   public Optional<? extends SaveConsequence> getSaveConsequence(IntegrationEntityPage var1) {
      if (this.refreshJobFactory == null) {
         this.refreshJobFactory = this.endpointConfig.getRefreshJobFactory();
      }

      NetSuiteEndpointPageWrapper var2 = new NetSuiteEndpointPageWrapper(var1);
      return Optional.of(new NetSuiteVersionChangeSaveConseqence(var2, this.refreshJobFactory));
   }
}
