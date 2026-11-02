package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.HashSet;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.id.SalesforceBulkLoadId;
import org.etlforge.integration.data.entity.id.ScriptId;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.entity.id.WebServiceCallId;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.util.id.KongaGuid;

public abstract class PipelineActivityType {
   public static final PipelineActivityType SCRIPT = new PipelineActivityType(1, "SCRIPT", "Script", "Scripts") {
      @Override
      public PipelineActivity createEmptyActivity() {
         return new ScriptActivity(this.getDisplayName());
      }

      @Override
      public PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2) {
         return new ScriptActivity(var1, var2);
      }

      @Override
      public IntegrationEntityId createEntityId(String var1, Optional<Integer> var2) {
         return new ScriptId(var1);
      }

      @Override
      public boolean accepts(Object var1) {
         return var1 instanceof Script;
      }
   };
   public static final PipelineActivityType PLUGIN = new PipelineActivityType(2, "PLUGIN", "Plugin", "Plugins") {
      @Override
      public PipelineActivity createEmptyActivity() {
         throw new NotImplementedYetException();
      }

      @Override
      public PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2) {
         throw new NotImplementedYetException();
      }

      @Override
      public IntegrationEntityId createEntityId(String var1, Optional<Integer> var2) {
         throw new NotImplementedYetException();
      }

      @Override
      public boolean accepts(Object var1) {
         throw new NotImplementedYetException();
      }
   };
   public static final PipelineActivityType SOURCE = new PipelineActivityType(3, "SOURCE", "Source", "Sources") {
      @Override
      public PipelineActivity createEmptyActivity() {
         return new SourceActivity(this.getDisplayName());
      }

      @Override
      public PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2) {
         return new SourceActivity(var1, var2);
      }

      @Override
      public IntegrationEntityId createEntityId(String var1, Optional<Integer> var2) {
         return new SourceId(var1);
      }

      @Override
      public boolean accepts(Object var1) {
         return var1 instanceof Source;
      }
   };
   public static final PipelineActivityType TARGET = new PipelineActivityType(4, "TARGET", "Target", "Targets") {
      @Override
      public PipelineActivity createEmptyActivity() {
         return new TargetActivity(this.getDisplayName());
      }

      @Override
      public PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2) {
         return new TargetActivity(var1, var2);
      }

      @Override
      public IntegrationEntityId createEntityId(String var1, Optional<Integer> var2) {
         return new TargetId(var1);
      }

      @Override
      public boolean accepts(Object var1) {
         return var1 instanceof Target;
      }
   };
   public static final PipelineActivityType TRANSFORMATION = new PipelineActivityType(5, "TRANSFORMATION", "Transformation", "Transformations") {
      @Override
      public PipelineActivity createEmptyActivity() {
         return new TransformationActivity(this.getDisplayName());
      }

      @Override
      public PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2) {
         return new TransformationActivity(var1, var2);
      }

      @Override
      public IntegrationEntityId createEntityId(String var1, Optional<Integer> var2) {
         return new TransformationId(var1);
      }

      @Override
      public boolean accepts(Object var1) {
         return var1 instanceof Transformation;
      }
   };
   public static final PipelineActivityType WEBSERVICECALL = new PipelineActivityType(6, "WEBSERVICECALL", "Web Service Call", "Web Service Calls") {
      @Override
      public PipelineActivity createEmptyActivity() {
         return new WebServiceCallActivity(this.getDisplayName());
      }

      @Override
      public PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2) {
         return new WebServiceCallActivity(var1, var2);
      }

      @Override
      public IntegrationEntityId createEntityId(String var1, Optional<Integer> var2) {
         return new WebServiceCallId(var1);
      }

      @Override
      public boolean accepts(Object var1) {
         return var1 instanceof WebServiceCall;
      }
   };
   public static final PipelineActivityType BULK_LOAD = new PipelineActivityType(7, "BULK_LOAD", "Salesforce Bulk Load", "Salesforce Bulk Loads") {
      @Override
      public PipelineActivity createEmptyActivity() {
         return new BulkLoadActivity(this.getDisplayName());
      }

      @Override
      public PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2) {
         return new BulkLoadActivity(var1, var2);
      }

      @Override
      public IntegrationEntityId createEntityId(String var1, Optional<Integer> var2) {
         return new SalesforceBulkLoadId(var1);
      }

      @Override
      public boolean accepts(Object var1) {
         return var1 instanceof SalesforceBulkLoad;
      }
   };
   public static final PipelineActivityType XSLT_TRANSFORM = new PipelineActivityType(8, "XSLT_TRANSFORM", "XSLT Transform", "XSLT Transforms") {
      @Override public PipelineActivity createEmptyActivity() { return new XsltTransformActivity("XSLT"); }
      @Override public PipelineActivity restoreEmptyActivity(KongaGuid id, String role) { return new XsltTransformActivity(id, role); }
      @Override public IntegrationEntityId createEntityId(String id, Optional<Integer> type) { return new org.etlforge.integration.data.entity.id.XsltTransformId(id); }
      @Override public boolean accepts(Object value) { return value instanceof org.etlforge.integration.data.entity.XsltTransform; }
   };
   private static ImmutableMap<String, PipelineActivityType> nameToType;
   private static ImmutableMap<Integer, PipelineActivityType> idToType;
   private final int id;
   private final String name;
   private final String display;
   private final String plural;

   public static synchronized PipelineActivityType fromId(int var0) {
      loadLookupMaps();
      PipelineActivityType var1 = idToType.get(var0);
      if (var1 == null) {
         throw new IllegalArgumentException("Unknown PipelineActivityType ID: " + var0);
      } else {
         return var1;
      }
   }

   private static void loadLookupMaps() {
      if (nameToType == null) {
         HashSet var0 = Sets.newHashSet();

         for (OperationType var2 : OperationType.values()) {
            var0.addAll(var2.getSupportedActivityTypes());
         }

         Builder var5 = ImmutableMap.builder();
         Builder var6 = ImmutableMap.builder();

         for (Object __etlItem201 : var0) {
            PipelineActivityType var4 = (PipelineActivityType) __etlItem201;
            var5.put(var4.name, var4);
            var6.put(var4.id, var4);
         }

         nameToType = var5.build();
         idToType = var6.build();
      }
   }

   public static synchronized PipelineActivityType fromName(String var0) {
      loadLookupMaps();
      PipelineActivityType var1 = nameToType.get(var0);
      if (var1 == null) {
         throw new IllegalArgumentException("Unknown PipelineActivityType name: " + var0);
      } else {
         return var1;
      }
   }

   protected PipelineActivityType(int var1, String var2, String var3, String var4) {
      this.id = var1;
      this.name = var2;
      this.display = var3;
      this.plural = var4;
   }

   public int getActivityTypeId() {
      return this.id;
   }

   public String name() {
      return this.name;
   }

   public String getDisplayName() {
      return this.display;
   }

   public String getPlural() {
      return this.plural;
   }

   @Override
   public String toString() {
      return this.display;
   }

   public abstract PipelineActivity createEmptyActivity();

   public abstract PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2);

   public abstract IntegrationEntityId createEntityId(String var1, Optional<Integer> var2);

   public abstract boolean accepts(Object var1);

   @Override
   public final int hashCode() {
      return this.id;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof PipelineActivityType ? this.id == ((PipelineActivityType)var1).id : false;
      }
   }
}
