package org.etlforge.integration.client.ui.connector.netsuite.search;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.connector.netsuite.serverapi.NetSuiteDesignInfoProvider;
import org.etlforge.integration.client.connector.serverapi.TestNetSuiteQueryApi;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.ui.connector.FunctionProperty;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJobFactory;
import org.etlforge.integration.client.ui.connector.netsuite.function.AbstractNetSuiteFunctionConfig;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteSearchObjectSavedSearchLookup;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.NetSuiteQueryWizardFactory;
import org.etlforge.integration.data.connector.NetSuiteQuery;
import org.etlforge.integration.data.entity.EntityType;

public final class NetSuiteQueryConfig extends AbstractNetSuiteFunctionConfig<NetSuiteQuery> {
   private final NetSuiteDefinitionQueryBuilderLookup searchDefLookup;
   private final NetSuiteCustomFieldsQueryBuilderLookup customFieldsLookup;
   private final NetSuiteSearchObjectSavedSearchLookup savedSearchLookup;
   private final TestNetSuiteQueryApi testQueryApi;

   public NetSuiteQueryConfig(
      NetSuiteQueryWizardFactory var1,
      TreeMapperInfoProvider var2,
      NetSuiteDesignInfoProvider var3,
      MappingStructureFactory var4,
      NetSuiteDefinitionQueryBuilderLookup var5,
      NetSuiteCustomFieldsQueryBuilderLookup var6,
      NetSuiteSearchObjectSavedSearchLookup var7,
      TestNetSuiteQueryApi var8
   ) {
      super(EntityType.NetSuiteQuery, var1, var2, var3, var4);
      this.searchDefLookup = var5;
      this.customFieldsLookup = var6;
      this.testQueryApi = var8;
      this.savedSearchLookup = var7;
      this.setSearchValuesCollector(new NetSuiteQuerySearchValues());
   }

   protected List<? extends FunctionProperty> getProperties(NetSuiteQuery var1, RefreshFunctionJobFactory var2) {
      ArrayList var3 = Lists.newArrayList(super.getProperties(var1, var2));
      var3.add(new SearchXmlProperty(var1, this.searchDefLookup, this.customFieldsLookup, this.savedSearchLookup, this.testQueryApi));
      return var3;
   }
}
