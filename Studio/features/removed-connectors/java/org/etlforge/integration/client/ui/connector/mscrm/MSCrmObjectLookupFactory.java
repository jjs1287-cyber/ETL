package org.etlforge.integration.client.ui.connector.mscrm;

import org.etlforge.integration.client.connector.mscrm.serverapi.MSCrmDesignInfoProvider;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;

public final class MSCrmObjectLookupFactory {
   private final MSCrmObjectCache cache;
   private final MSCrmDesignInfoProvider infoProvider;

   public MSCrmObjectLookupFactory(MSCrmObjectCache var1, MSCrmDesignInfoProvider var2) {
      this.cache = var1;
      this.infoProvider = var2;
   }

   public MSCrmObjectLookup newLookup(MSCrmEndpoint var1) {
      return new ClientSideMSCrmObjectLookup(this.cache, this.infoProvider, var1);
   }
}
