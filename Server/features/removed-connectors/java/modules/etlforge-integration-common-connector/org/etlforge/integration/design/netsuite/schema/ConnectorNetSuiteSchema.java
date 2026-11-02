package org.etlforge.integration.design.netsuite.schema;

import java.util.Map;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObjects;
import org.etlforge.integration.design.netsuite.cf.ConnectorCustomFieldsSchema;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldsRequest;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.design.netsuite.definition.NetSuiteOperationRootElements;

public class ConnectorNetSuiteSchema {
   private final NetSuiteDefinitionDesign defAll;
   private final ConnectorCustomFieldsSchema result;
   private final NetSuiteCustomFieldsRequest standardObjects;

   public ConnectorNetSuiteSchema(NetSuiteDefinitionDesign var1, NetSuiteCustomFieldsRequest var2, ConnectorCustomFieldsSchema var3) {
      this.standardObjects = var2;
      this.result = var3;
      this.defAll = var1;
   }

   public Map<String, String> generate(NetSuiteOperationRootElements var1, NetSuiteObjects var2) {
      ConnectorNetSuiteSchemaCollector var3 = new ConnectorNetSuiteSchemaCollector(this.defAll.getNetSuiteDefinition(), var1, var2);
      Map var4 = var3.collect();
      ConnectorNetSuiteCustomFieldSchemaBuilder var5 = new ConnectorNetSuiteCustomFieldSchemaBuilder(this.defAll, this.standardObjects, this.result, var2);
      if (!var2.getNetSuiteSelectedObject().isDelete()) {
         assert this.standardObjects != null;
         var5.build();
      }

      ConnectorNetSuiteSchemaBuilder var6 = new ConnectorNetSuiteSchemaBuilder(
         var4, var2, var5.getModifiedCustomFieldSchemas(), this.defAll.getNetSuiteDefinition()
      );
      return var6.build();
   }
}
