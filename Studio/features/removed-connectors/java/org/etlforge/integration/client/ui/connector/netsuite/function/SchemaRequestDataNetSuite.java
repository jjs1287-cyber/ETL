package org.etlforge.integration.client.ui.connector.netsuite.function;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.GetSchemasCallback;
import org.etlforge.integration.client.connector.serverapi.MultipleSchemas;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionDesignLookup;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.design.netsuite.NetSuiteXsdSchemaFiles;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteDesignAdapter;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteDesignCustomFieldAdapter;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteSelectedObject;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchAdvancedProperties;
import org.etlforge.util.zip.CompressedAndBase64EncodedString;

public class SchemaRequestDataNetSuite extends SchemaRequestData {
   private final NetSuiteEndpoint netsuiteEndpoint;
   private final NetSuiteFunction.FunctionType functionType;
   private final NetSuiteConnectorObject connectorObject;
   private final NetSuiteSearchAdvancedProperties searchProperty;
   private final NetSuiteDefinitionDesignLookup defLookup;
   private final NetSuiteCustomFieldsDesignLookup cfLookup;

   public static SchemaRequestData multipleSchemasNetSuite(
      NetSuiteEndpoint var0,
      NetSuiteFunction.FunctionType var1,
      NetSuiteConnectorObject var2,
      NetSuiteSearchAdvancedProperties var3,
      NetSuiteDefinitionDesignLookup var4,
      NetSuiteCustomFieldsDesignLookup var5
   ) {
      return new SchemaRequestDataNetSuite(SchemaRequestData.Mode.MULTIPLE_GET, var0, var1, var2, var3, var4, var5);
   }

   public static SchemaRequestData refreshingMultipleSchemasNetSuite(
      NetSuiteEndpoint var0,
      NetSuiteFunction.FunctionType var1,
      NetSuiteConnectorObject var2,
      NetSuiteSearchAdvancedProperties var3,
      NetSuiteDefinitionDesignLookup var4,
      NetSuiteCustomFieldsDesignLookup var5
   ) {
      return new SchemaRequestDataNetSuite(SchemaRequestData.Mode.MULTIPLE_REFRESH, var0, var1, var2, var3, var4, var5);
   }

   public SchemaRequestDataNetSuite(
      SchemaRequestData.Mode var1,
      NetSuiteEndpoint var2,
      NetSuiteFunction.FunctionType var3,
      NetSuiteConnectorObject var4,
      NetSuiteSearchAdvancedProperties var5,
      NetSuiteDefinitionDesignLookup var6,
      NetSuiteCustomFieldsDesignLookup var7
   ) {
      super(var1, var2, var3);
      this.netsuiteEndpoint = var2;
      this.functionType = var3;
      this.connectorObject = var4;
      this.searchProperty = var3 == NetSuiteFunction.FunctionType.QUERY ? var5 : null;
      this.defLookup = var6;
      this.cfLookup = var7;
   }

   @Override
   public void askServerForSchemas(ConnectorCallInfoProvider var1, GetSchemasCallback var2) {
      switch (this.mode) {
         case MULTIPLE_GET:
            this.getMultipleSchemas(false, var2);
            break;
         case MULTIPLE_REFRESH:
            this.getMultipleSchemas(true, var2);
            break;
         default:
            throw new AssertionError();
      }
   }

   private void getMultipleSchemas(boolean var1, GetSchemasCallback var2) {
      this.cfLookup.setDesignInput(this.netsuiteEndpoint, this.functionType);
      NetSuiteDefinitionDesign var3 = this.defLookup.lookupDefinition(this.netsuiteEndpoint);
      NetSuiteSelectedObject var4 = new NetSuiteSelectedObject(this.getEntityType(), this.connectorObject, this.searchProperty);
      boolean var5 = this.functionType != NetSuiteFunction.FunctionType.DELETE;
      if (var5 && var1) {
         Set var6 = NetSuiteDesignCustomFieldAdapter.getCustomFieldKey(var3, var4);
         this.inValidateCache(var4.isCustomObject(), var6);
      }

      NetSuiteDesignAdapter var9 = new NetSuiteDesignAdapter(var3, this.cfLookup, var4);
      NetSuiteXsdSchemaFiles var7 = var9.getConnectorSchemaFiles();
      Schemas var8 = this.convertToSchemas(var7);
      var2.handleSchemas(var8);
   }

   private void inValidateCache(boolean var1, Set<String> var2) {
      for (String var4 : var2) {
         this.cfLookup.evict(var1, var4);
      }
   }

   private Schemas convertToSchemas(NetSuiteXsdSchemaFiles var1) {
      HashMap var2 = Maps.newHashMap();

      for (Entry var4 : var1.getSchemaFileMaps().entrySet()) {
         CompressedAndBase64EncodedString var5 = compress((String)var4.getValue());
         var2.put(var4.getKey(), var5);
      }

      return new MultipleSchemas(
         var1.getRequestMainSchemaFilename(),
         var1.getRequestRootElementExpandedName(),
         var1.getResponseMainSchemaFilename(),
         var1.getResponseRootElementExpandedName(),
         var2
      );
   }

   private static CompressedAndBase64EncodedString compress(String var0) {
      try {
         return CompressedAndBase64EncodedString.compress(var0);
      } catch (IOException var2) {
         var2.printStackTrace();
         throw new RuntimeException(var2);
      }
   }

   private EntityType getEntityType() {
      switch (this.functionType) {
         case CREATE:
            return EntityType.NetSuiteCreate;
         case UPDATE:
            return EntityType.NetSuiteUpdate;
         case UPSERT:
            return EntityType.NetSuiteUpsert;
         case DELETE:
            return EntityType.NetSuiteDelete;
         case QUERY:
            return EntityType.NetSuiteQuery;
         default:
            return EntityType.NetSuiteDelete;
      }
   }
}
