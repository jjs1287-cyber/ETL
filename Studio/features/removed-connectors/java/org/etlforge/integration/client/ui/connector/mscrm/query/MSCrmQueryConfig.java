package org.etlforge.integration.client.ui.connector.mscrm.query;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.ui.connector.FunctionProperty;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJobFactory;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectLookupFactory;
import org.etlforge.integration.client.ui.connector.mscrm.function.AbstractMSCrmFunctionConfig;
import org.etlforge.integration.client.ui.connector.mscrm.query.wizard.MSCrmQueryWizardFactory;
import org.etlforge.integration.data.connector.MSCrmQuery;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

public final class MSCrmQueryConfig extends AbstractMSCrmFunctionConfig<MSCrmQuery> {
   private final MSCrmObjectLookupFactory objectLookupFactory;
   private final FileUploadApi fileUploadApi;

   public MSCrmQueryConfig(
      MSCrmQueryWizardFactory var1, TreeMapperInfoProvider var2, MappingStructureFactory var3, MSCrmObjectLookupFactory var4, FileUploadApi var5
   ) {
      super(EntityType.MSCrmQuery, var1, var2, var3);
      this.objectLookupFactory = var4;
      this.fileUploadApi = var5;
      this.setSearchValuesCollector(new MSCrmQuerySearchValues());
   }

   protected Callable<Schemas> getSchemaGenerator(MSCrmQuery var1) {
      return new SchemaGenerator(this.objectLookupFactory, var1);
   }

   @Override
   protected FileUploadApi getFileUploadApi() {
      return this.fileUploadApi;
   }

   protected List<? extends FunctionProperty> getProperties(MSCrmQuery var1, RefreshFunctionJobFactory var2) {
      ArrayList var3 = Lists.newArrayList(super.getProperties(var1, var2));
      QueryXmlProperty var4 = new QueryXmlProperty(var1, this.objectLookupFactory, var2);
      var3.add(var4);
      return var3;
   }
}
