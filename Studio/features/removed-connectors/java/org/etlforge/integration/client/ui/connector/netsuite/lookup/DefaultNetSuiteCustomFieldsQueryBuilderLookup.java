package org.etlforge.integration.client.ui.connector.netsuite.lookup;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchCustomFields;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchDesignException;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchFieldDictionary;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchObjectCustomFieldsCreator;
import org.etlforge.integration.client.ui.connector.netsuite.search.metadata.NetSuiteSearchCustomFieldMetaData;
import org.etlforge.integration.client.ui.connector.netsuite.search.metadata.NetSuiteSearchCustomFieldMetaDataFactory;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteDesignCustomFieldAdapter;
import org.etlforge.integration.design.netsuite.cf.ConnectorNetSuiteCustomFields;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;

public class DefaultNetSuiteCustomFieldsQueryBuilderLookup implements NetSuiteCustomFieldsQueryBuilderLookup {
   private final NetSuiteCustomFieldsDesignLookup cfLookup;
   private final NetSuiteDefinitionDesignLookup defLookup;

   public DefaultNetSuiteCustomFieldsQueryBuilderLookup(NetSuiteCustomFieldsDesignLookup var1, NetSuiteDefinitionDesignLookup var2) {
      this.cfLookup = Preconditions.checkNotNull(var1);
      this.defLookup = Preconditions.checkNotNull(var2);
   }

   @Override
   public NetSuiteSearchCustomFields lookupCustomFields(NetSuiteEndpoint var1, String var2, NetSuiteSearchFieldDictionary var3) throws NetSuiteSearchDesignException {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      Preconditions.checkNotNull(var3);

      try {
         NetSuiteSearchCustomFieldMetaData var4 = this.askServer(var1, var2);
         NetSuiteSearchObjectCustomFieldsCreator var5 = new NetSuiteSearchObjectCustomFieldsCreator(var3, var4);
         return var5.newInstance();
      } catch (Exception var7) {
         Throwables.propagateIfInstanceOf(var7, NetSuiteSearchDesignException.class);
         throw new NetSuiteSearchDesignException(var7);
      }
   }

   private NetSuiteSearchCustomFieldMetaData askServer(NetSuiteEndpoint var1, String var2) throws Exception {
      NetSuiteDefinitionDesign var3 = this.defLookup.lookupDefinition(var1);
      this.cfLookup.setDesignInput(var1, NetSuiteFunction.FunctionType.QUERY);
      NetSuiteCustomFieldsDesignLookup var4 = this.cfLookup;
      NetSuiteDesignCustomFieldAdapter var5 = new NetSuiteDesignCustomFieldAdapter(var3, var4);
      ConnectorNetSuiteCustomFields var6 = var5.getSearchCustomFields(var2);
      return NetSuiteSearchCustomFieldMetaDataFactory.create(var2, var6, var3);
   }
}
