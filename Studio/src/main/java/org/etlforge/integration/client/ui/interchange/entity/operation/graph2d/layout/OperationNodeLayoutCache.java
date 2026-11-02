package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.util.persist.Persistor;

final class OperationNodeLayoutCache {
   private static final String CACHED_OP = "CachedOp";
   private static final String OP_ID = "opId";
   private final Map<OperationId, OperationNodeLayoutCache.OperationTypeCache> operationCaches = Maps.newHashMap();

   public OperationNodeLayoutCache() {
   }

   public OperationNodeLayoutCache(Persistor var1) {
      this();

      try {
         HashMap var2 = Maps.newHashMap();

         for (Persistor var4 : var1.getChildren("CachedOp")) {
            OperationId var5 = new OperationId(var4.getString("opId"));
            OperationNodeLayoutCache.OperationTypeCache var6 = new OperationNodeLayoutCache.OperationTypeCache(var4);
            var2.put(var5, var6);
         }

         this.operationCaches.putAll(var2);
      } catch (RuntimeException var7) {
         ErrorLog.log(OperationNodeLayoutCache.class, "Failed to restore the OperationNodeLayoutCache", var7);
      }
   }

   public void add(OperationNodeLayout var1) {
      OperationId var2 = var1.getOperationNode().getOperationId();
      OperationNodeLayoutCache.OperationTypeCache var3 = this.operationCaches.get(var2);
      if (var3 == null) {
         var3 = new OperationNodeLayoutCache.OperationTypeCache();
         this.operationCaches.put(var2, var3);
      }

      var3.add(var1);
   }

   public OperationNodeLayoutMemento get(OperationId var1, OperationPipeline var2) {
      OperationNodeLayoutCache.OperationTypeCache var3 = this.operationCaches.get(var1);
      return var3 != null ? var3.get(var2) : null;
   }

   public void writeTo(Persistor var1) {
      for (Entry var3 : this.operationCaches.entrySet()) {
         Persistor var4 = var1.createChild("CachedOp");
         var4.putString("opId", ((OperationId)var3.getKey()).toString());
         ((OperationNodeLayoutCache.OperationTypeCache)var3.getValue()).writeTo(var4);
      }
   }

   private static class OperationTypeCache {
      private static final String TYPE = "Type";
      private static final String NAME = "name";
      private static final String MEMENTO = "Memento";
      private final Map<OperationType, OperationNodeLayoutMemento> mementos = Maps.newHashMap();

      public OperationTypeCache() {
      }

      public OperationTypeCache(Persistor var1) {
         this();

         try {
            HashMap var2 = Maps.newHashMap();

            for (Persistor var4 : var1.getChildren("Type")) {
               OperationType var5 = OperationType.valueOf(var4.getString("name"));
               Persistor var6 = var4.getFirstChild("Memento");
               OperationNodeLayoutMemento var7 = new OperationNodeLayoutMemento(var6);
               var2.put(var5, var7);
            }

            this.mementos.putAll(var2);
         } catch (RuntimeException var8) {
            ErrorLog.log(OperationNodeLayoutCache.class, "Failed to restore the OperationTypeCache", var8);
         }
      }

      public void add(OperationNodeLayout var1) {
         OperationType var2 = var1.getPipeline().getType();
         OperationNodeLayoutMemento var3 = new OperationNodeLayoutMemento(var1);
         this.mementos.put(var2, var3);
      }

      public OperationNodeLayoutMemento get(OperationPipeline var1) {
         return this.mementos.get(var1.getType());
      }

      public void writeTo(Persistor var1) {
         for (Entry var3 : this.mementos.entrySet()) {
            Persistor var4 = var1.createChild("Type");
            var4.putString("name", ((OperationType)var3.getKey()).name());
            Persistor var5 = var4.createChild("Memento");
            ((OperationNodeLayoutMemento)var3.getValue()).writeTo(var5);
         }
      }
   }
}
