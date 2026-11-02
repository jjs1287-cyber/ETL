package org.etlforge.integration.client.ui.interchange.location.panel;

import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.location.DataLocation;

public interface DataLocationPanelPart<T extends DataLocation> {
   void applyTo(T var1) throws IntegrationDataPanelException;

   void reset(T var1);

   void dispose();

   void addToLayout(DataLocationPanelLayout var1);
}
