package org.etlforge.integration.client.ui.interchange.location.http;

import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanelPart;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.ui.layout.TwoColumnLayout;

interface HttpLocationPanelPart extends DataLocationPanelPart<HttpLocation> {
   void layout(TwoColumnLayout var1);
}
