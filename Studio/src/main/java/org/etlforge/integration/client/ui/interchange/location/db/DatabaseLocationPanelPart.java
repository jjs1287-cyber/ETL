package org.etlforge.integration.client.ui.interchange.location.db;

import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanelPart;
import org.etlforge.integration.data.location.DatabaseLocation;

interface DatabaseLocationPanelPart extends DataLocationPanelPart<DatabaseLocation> {
   void addToLayout(DatabaseLocationPanelLayout var1);
}
