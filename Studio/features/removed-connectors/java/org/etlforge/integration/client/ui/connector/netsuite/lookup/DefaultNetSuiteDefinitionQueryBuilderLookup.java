package org.etlforge.integration.client.ui.connector.netsuite.lookup;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteDefinitionQueryBuilder;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteDefinitionQueryBuilderCreator;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchDesignException;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;

public final class DefaultNetSuiteDefinitionQueryBuilderLookup implements NetSuiteDefinitionQueryBuilderLookup {
   private final NetSuiteDefinitionDesignLookup lookup;

   public DefaultNetSuiteDefinitionQueryBuilderLookup(NetSuiteDefinitionDesignLookup var1) {
      this.lookup = Preconditions.checkNotNull(var1);
   }

   @Override
   public NetSuiteDefinitionQueryBuilder lookupDefinition(NetSuiteEndpoint var1) throws NetSuiteSearchDesignException {
      Preconditions.checkNotNull(var1);
      NetSuiteDefinitionDesign var2 = this.lookup.lookupDefinition(var1);
      NetSuiteDefinitionQueryBuilderCreator var3 = new NetSuiteDefinitionQueryBuilderCreator(var2);
      return var3.createInstance();
   }
}
