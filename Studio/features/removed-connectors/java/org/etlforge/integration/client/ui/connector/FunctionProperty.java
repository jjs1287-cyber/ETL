package org.etlforge.integration.client.ui.connector;

import org.etlforge.integration.client.ui.connector.function.ConnectorCallPage;
import org.etlforge.ui.layout.TwoColumnLayout;

public interface FunctionProperty {
   void associateWithPage(ConnectorCallPage var1);

   void layoutIn(TwoColumnLayout var1);
}
