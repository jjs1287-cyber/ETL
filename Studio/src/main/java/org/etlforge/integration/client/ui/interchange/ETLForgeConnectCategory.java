package org.etlforge.integration.client.ui.interchange;

import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.icons.ClientIcons;

public final class ETLForgeConnectCategory extends TopLevelCategoryGroup {
   public ETLForgeConnectCategory(Iterable<TopLevelEntityCategory> var1, Iterable<TopLevelEntityCategory> var2) {
      super(
         "ETLForge Connect\u2122",
         KongaListUtils.concatenateToImmutableList(var1, var2),
         new SimpleIconProvider(ClientIcons.ETLFORGE_CONNECT_16, ClientIcons.ETLFORGE_CONNECT_24, ClientIcons.ETLFORGE_CONNECT_32)
      );
      this.setMenuItemName("ETLForge &Connect");
   }
}
