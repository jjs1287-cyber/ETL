package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.util.id.KongaGuid;
import org.etlforge.util.persist.Persistor;

public final class OperationPipelineMemento {
   private final OperationType opType;
   private final ImmutableList<OperationPipelineMemento.Entry> entries;

   public OperationPipelineMemento(OperationPipeline var1) {
      this.opType = var1.getType();
      this.entries = createEntries(var1);
   }

   private static ImmutableList<OperationPipelineMemento.Entry> createEntries(OperationPipeline var0) {
      Builder var1 = ImmutableList.builder();

      for (PipelineActivity var3 : var0.getActivities()) {
         var1.add(new OperationPipelineMemento.Entry(var3));
      }

      return var1.build();
   }

   public OperationPipelineMemento(OperationType var1, Persistor var2) {
      this.opType = var1;
      this.entries = this.restoreEntries(var2);
   }

   private ImmutableList<OperationPipelineMemento.Entry> restoreEntries(Persistor var1) {
      Builder var2 = ImmutableList.builder();
      Persistor var3 = var1.getFirstChild("Activities");

      for (Persistor var5 : var3.getAllChildren()) {
         OperationPipelineMemento.Entry var6 = this.restoreEntry(var5);
         var2.add(var6);
      }

      return var2.build();
   }

   private OperationPipelineMemento.Entry restoreEntry(Persistor var1) {
      PipelineActivityType var2 = PipelineActivityType.fromName(var1.getName());
      String var3 = var1.getString("role");
      KongaGuid var4 = KongaGuid.createGuid(var1.getString("activityId"));
      IntegrationEntityId var5 = this.restoreContentId(var2, var1);
      boolean var6 = restoreReadOnly(var1);
      return new OperationPipelineMemento.Entry(var2, var4, var5, var3, var6);
   }

   private IntegrationEntityId restoreContentId(PipelineActivityType var1, Persistor var2) {
      String var3 = var2.getString("contentId");
      if (var3 == null) {
         return null;
      }

      Optional var4 = Optional.fromNullable(var2.getInteger("type"));
      return var1.createEntityId(var3, var4);
   }

   private static boolean restoreReadOnly(Persistor var0) {
      Boolean var1 = var0.getBoolean("readOnly");
      return var1 == null ? false : var1;
   }

   public OperationPipeline restorePipeline(IntegrationEntityLookup var1) {
      ArrayList var2 = Lists.newArrayList();

      for (OperationPipelineMemento.Entry var4 : this.entries) {
         PipelineActivity var5 = this.restoreActivity(var4, var1);
         var2.add(var5);
      }

      return new OperationPipeline(this.opType, var2);
   }

   private PipelineActivity restoreActivity(OperationPipelineMemento.Entry var1, IntegrationEntityLookup var2) {
      PipelineActivity var3 = var1.type.restoreEmptyActivity(var1.activityId, var1.role);
      if (var3 instanceof EntityActivity) {
         EntityActivity var4 = (EntityActivity)var3;
         if (var1.contentId != null) {
            var4.setContent(var1.contentId, var2);
         }

         var4.setReadOnly(var1.readOnly);
      }

      return var3;
   }

   public void writeTo(Persistor var1) {
      var1.putString("opType", this.opType.name());
      Persistor var2 = var1.createChild("Activities");

      for (OperationPipelineMemento.Entry var4 : this.entries) {
         var4.writeTo(var2);
      }
   }

   private static class Entry {
      public final PipelineActivityType type;
      public final KongaGuid activityId;
      public final IntegrationEntityId contentId;
      public final String role;
      public final boolean readOnly;

      public Entry(PipelineActivity var1) {
         this.type = var1.getType();
         this.activityId = var1.getId();
         this.role = var1.getRole();
         Object var2 = var1.getContent();
         this.contentId = var2 instanceof IntegrationEntity ? ((IntegrationEntity)var2).getID() : null;
         this.readOnly = var1.isReadOnly();
      }

      public Entry(PipelineActivityType var1, KongaGuid var2, IntegrationEntityId var3, String var4, boolean var5) {
         this.type = var1;
         this.activityId = var2;
         this.contentId = var3;
         this.role = var4;
         this.readOnly = var5;
      }

      public void writeTo(Persistor var1) {
         Persistor var2 = var1.createChild(this.type.name());
         var2.putString("role", this.role);
         var2.putString("activityId", this.activityId.toString());
         if (this.contentId != null) {
            var2.putString("contentId", this.contentId.toString());
            var2.putInteger("type", this.contentId.getEntityType().getId());
         }

         if (this.readOnly) {
            var2.putBoolean("readOnly", this.readOnly);
         }
      }
   }
}
