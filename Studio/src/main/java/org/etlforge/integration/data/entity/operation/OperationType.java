package org.etlforge.integration.data.entity.operation;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;
import java.util.List;
import org.etlforge.application.Application;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.OperationFunctionCollector;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.util.version.Version;

public abstract class OperationType implements Comparable<OperationType>, EntityFilter {
   private static ImmutableSet<OperationType> types;
   private static OperationType defaultType;
   private static boolean loaded;
   private final int id;
   private final String name;
   private final String displayName;
   private final String description;
   private final ImmutableSet<PipelineActivityType> supportedActivityTypes;

   public static synchronized void load() {
      if (!loaded) {
         List var0 = Application.getBeansOfType(OperationTypes.class);
         if (var0.size() != 1) {
            FatalConfigurationError.throwError(9, OperationType.class, null, "Missing or conflicting OperationTypes beans in the application context.");
         }

         OperationTypes var1 = (OperationTypes)var0.get(0);
         types = ImmutableSet.copyOf(var1);
         defaultType = Iterables.get(var1, 0);
         loaded = true;
      }
   }

   @VisibleForTesting
   public static synchronized void load(OperationType... var0) {
      if (!loaded) {
         types = ImmutableSet.copyOf(var0);
         defaultType = var0[0];
         loaded = true;
      }
   }

   public static synchronized OperationType fromId(int var0) {
      for (OperationType var2 : types) {
         if (var0 == var2.id) {
            return var2;
         }
      }

      throw new IllegalArgumentException("Unknown operation type id: " + var0);
   }

   public static synchronized OperationType valueOf(String var0) {
      for (OperationType var2 : types) {
         if (var2.name.equals(var0)) {
            return var2;
         }
      }

      throw new IllegalArgumentException("Unknown operation type name: " + var0);
   }

   public static synchronized ImmutableSet<OperationType> values() {
      return types;
   }

   public static synchronized ImmutableSet<OperationType> getSelectableTypes() {
      return FluentIterable.from(types).filter(new Predicate<OperationType>() {
         public boolean apply(OperationType var1) {
            return var1.isSelectable();
         }
      }).toImmutableSet();
   }

   public static synchronized OperationType getDefaultType() {
      return defaultType;
   }

   protected OperationType(int var1, String var2, String var3, String var4, ImmutableSet<PipelineActivityType> var5) {
      this.id = var1;
      this.name = var2;
      this.displayName = var3;
      this.description = var4;
      this.supportedActivityTypes = var5;
   }

   public final int getId() {
      return this.id;
   }

   public final String name() {
      return this.name;
   }

   public final String getDescription() {
      return this.description;
   }

   public final ImmutableSet<PipelineActivityType> getSupportedActivityTypes() {
      return this.supportedActivityTypes;
   }

   public int compareTo(OperationType var1) {
      return Ints.compare(this.id, var1.id);
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return var1 instanceof Operation && ((Operation)var1).getOperationType() == this;
   }

   public boolean isStartedByEngine() {
      return true;
   }

   public abstract boolean isEmailNotificationsSupported();

   public abstract boolean isChunkable();

   public abstract boolean hasSourceHistory();

   public abstract RunnableStatus checkRunnable(OperationExecutionMode var1);

   public boolean hasPreConnectionPoint() {
      return true;
   }

   public boolean hasPostConnectionPoint(OperationPipeline var1) {
      return true;
   }

   public abstract EntityFilter getSourceFilter();

   public abstract EntityFilter getTargetFilter();

   public abstract OperationPipeline createNewTemplate();

   public abstract OperationPipelineFactory getPipelineFactory();

   public abstract OperationPipelineValidator getPipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3);

   public abstract PipelineAutoCompleter getPipelineAutoCompleter(IntegrationEntityLookup var1);

   public EntityFilterProvider<Source> getSourceActivityFilterProvider(OperationPipeline var1) {
      throw new UnsupportedOperationException();
   }

   public EntityFilterProvider<Target> getTargetActivityFilterProvider(OperationPipeline var1) {
      throw new UnsupportedOperationException();
   }

   public EntityFilterProvider<Transformation> getTransformationActivityFilterProvider(OperationPipeline var1) {
      throw new UnsupportedOperationException();
   }

   public NewTransformationPreparer getNewTransformationPreparer() {
      throw new UnsupportedOperationException();
   }

   public ImmutableList<SystemTransformation> getApplicableSystemTransformations(Operation var1, OperationPipeline var2, TransformationActivity var3) {
      return ImmutableList.of();
   }

   public OperationFunctionCollector getFunctionCollector() {
      return OperationFunctionCollector.NONE;
   }

   public abstract ActivityInsertionPolicy getInsertionPolicy(OperationPipeline var1);

   public abstract ActivityRemovingPolicy getRemovingPolicy(OperationPipeline var1);

   public final boolean isCompatibleWith(Source var1) {
      EntityFilter var2 = this.getSourceFilter();
      return var2.apply(var1);
   }

   public final boolean isCompatibleWith(Target var1) {
      EntityFilter var2 = this.getTargetFilter();
      return var2.apply(var1);
   }

   public boolean isSelectable() {
      return true;
   }

   public Version getVersionIntroduced() {
      return VersionFactory.newVersion(1, 0, 0);
   }

   public String getIndefiniteArticle() {
      return "a";
   }

   @Override
   public final String toString() {
      return this.displayName;
   }

   @Override
   public final boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof OperationType && this.id == ((OperationType)var1).id;
   }

   @Override
   public final int hashCode() {
      return this.id;
   }
}
