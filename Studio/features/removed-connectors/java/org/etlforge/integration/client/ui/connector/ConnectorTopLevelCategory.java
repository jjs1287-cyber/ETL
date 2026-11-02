package org.etlforge.integration.client.ui.connector;

import java.util.List;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.SimpleIconProvider;
import org.etlforge.integration.client.ui.interchange.TopLevelCategoryGroup;
import org.etlforge.integration.client.ui.interchange.TopLevelEntityCategory;

public final class ConnectorTopLevelCategory extends TopLevelCategoryGroup {
   public ConnectorTopLevelCategory(List<TopLevelEntityCategory> var1) {
      super("Connectors", var1, new SimpleIconProvider(ClientIcons.CONNECTOR_16, ClientIcons.CONNECTOR_24, ClientIcons.CONNECTOR_32));
      this.setMenuItemName("Co&nnectors");
   }
}
