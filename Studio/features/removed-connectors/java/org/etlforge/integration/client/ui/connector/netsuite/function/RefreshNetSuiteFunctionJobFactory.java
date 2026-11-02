package org.etlforge.integration.client.ui.connector.netsuite.function;

import java.util.concurrent.Callable;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJob;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJobFactory;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteSearchObjectSavedSearchLookup;
import org.etlforge.integration.client.ui.connector.netsuite.search.NetSuiteQueryHelper;
import org.etlforge.integration.client.ui.interchange.entity.MappingStructureRefresher;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.connector.NetSuiteQuery;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

public class RefreshNetSuiteFunctionJobFactory extends RefreshFunctionJobFactory {
   private final NetSuiteDefinitionQueryBuilderLookup searchDefLookup;
   private final NetSuiteCustomFieldsQueryBuilderLookup customFieldsLookup;
   private final NetSuiteSearchObjectSavedSearchLookup savedSearchLookup;
   private final NetSuiteDefinitionDesignLookup defLookup;
   private final NetSuiteCustomFieldsDesignLookup cfLookup;
   private final ConnectorCallInfoProvider infoProvider;

   public RefreshNetSuiteFunctionJobFactory(
      FileStore var1,
      FileUploadApi var2,
      MappingStructureRefresher var3,
      NetSuiteDefinitionQueryBuilderLookup var4,
      NetSuiteCustomFieldsQueryBuilderLookup var5,
      NetSuiteSearchObjectSavedSearchLookup var6,
      NetSuiteDefinitionDesignLookup var7,
      NetSuiteCustomFieldsDesignLookup var8,
      ConnectorCallInfoProvider var9
   ) {
      super(var1, var2, var3);
      this.searchDefLookup = var4;
      this.customFieldsLookup = var5;
      this.savedSearchLookup = var6;
      this.infoProvider = var9;
      this.defLookup = var7;
      this.cfLookup = var8;
   }

   @Override
   public Callable<Schemas> getSchemaGenerator(ConnectorCall var1) {
      NetSuiteFunction var2 = (NetSuiteFunction)var1;
      return this.getSchemaGeneratorImpl(var2);
   }

   private Callable<Schemas> getSchemaGeneratorImpl(NetSuiteFunction var1) {
      NetSuiteSchemaGenerator var2 = new NetSuiteSchemaGenerator(this.defLookup, this.cfLookup, this.infoProvider);
      return var2.getSchemaGenerator(var1);
   }

   @Override
   protected RefreshFunctionJob getRefreshFunctionJob(ConnectorCall var1) {
      RefreshFunctionJob var2 = super.getRefreshFunctionJob(var1);
      if (var1 instanceof NetSuiteQuery) {
         NetSuiteQuery var3 = (NetSuiteQuery)var1;
         NetSuiteQueryHelper var4 = new NetSuiteQueryHelper(var3, this.searchDefLookup, this.savedSearchLookup, this.customFieldsLookup);
         return new RefreshNetSuiteFunctionJob(var2, var4);
      } else {
         return new RefreshNetSuiteFunctionJob(var2);
      }
   }
}
