package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import java.util.Collection;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.InterchangeDataRow;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.InterchangeDataTable;

public class EntityPipelinePlugin {
   public static final String D_ENTITY_TYPE_ID = "EntityTypeId";
   public static final String D_ENTITY_ID = "EntityId";
   public static final String D_PIPELINE_ENTITY_TYPE_ID = "PipelineEntityTypeId";
   public static final String D_RELATIVE_POSITION_ID = "RelativePositionId";
   public static final String D_PIPELINE_PLUGIN_GUID = "PipelinePluginGuid";
   public static final String D_EXECUTION_ORDER = "ExecutionOrder";
   public static final String D_IS_OPTIONAL = "Optional";
   public static final String D_VERSION = "Version";
   private static final String[] columnNames = new String[]{"EntityTypeId", "EntityId", "PipelineEntityTypeId", "RelativePositionId", "PipelinePluginGuid"};
   int EntityTypeId;
   String EntityId;
   long PipelineEntityTypeId;
   String RelativePositionId;
   String PipelinePluginGuid;

   private static String[] getFields(EntityPipelinePlugin var0) {
      return new String[]{
         Integer.toString(var0.EntityTypeId), var0.EntityId, Long.toString(var0.PipelineEntityTypeId), var0.RelativePositionId, var0.PipelinePluginGuid
      };
   }

   static EntityPipelinePlugin getInstanceForDelete(IntegrationEntity var0) {
      EntityPipelinePlugin var1 = new EntityPipelinePlugin();
      var1.EntityId = var0.getID().toString();
      var1.EntityTypeId = var0.getEntityType().getServerId();
      var1.PipelineEntityTypeId = -1L;
      var1.PipelinePluginGuid = "";
      var1.RelativePositionId = "";
      return var1;
   }

   static InterchangeDataTable getInterchangeDataTable(Collection<EntityPipelinePlugin> var0) {
      if (var0 != null && var0.size() != 0) {
         InterchangeDataTable var1 = new InterchangeDataTable();
         var1.setColumnNames((String[])columnNames.clone());
         InterchangeDataRow[] var2 = new InterchangeDataRow[var0.size()];
         int var3 = 0;

         for (EntityPipelinePlugin var5 : var0) {
            InterchangeDataRow var6 = new InterchangeDataRow();
            var6.setFields(getFields(var5));
            var2[var3++] = var6;
         }

         var1.setRows(var2);
         return var1;
      } else {
         return null;
      }
   }
}
