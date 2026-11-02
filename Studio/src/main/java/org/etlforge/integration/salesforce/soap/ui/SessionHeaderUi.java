package org.etlforge.integration.salesforce.soap.ui;

import com.google.common.base.Supplier;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.salesforce.soap.header.SessionHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;

public final class SessionHeaderUi implements SoapHeaderUi {
   private final Supplier<SalesforceConnectorId> connectorIdSupplier;

   public SessionHeaderUi(Supplier<SalesforceConnectorId> var1) {
      this.connectorIdSupplier = var1;
   }

   @Override
   public SoapHeader getDefinedSoapHeader() {
      SalesforceConnectorId var1 = this.connectorIdSupplier.get();
      return new SessionHeader(var1);
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
   }

   @Override
   public void addToParent(InputPanel var1) {
   }

   @Override
   public void requestFocus() {
   }
}
